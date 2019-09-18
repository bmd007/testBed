
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

import com.cabonline.mission.exception.ServiceUnavailableException;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StreamsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Each view_resource class has 3 related data classes:<br>
 * <li>I is internal representation of view which is the equivalent to the schema by which data is saved on stores.
 * <li>M is the external representation of I (dto)
 * <li>E is a Dto that includes a list of Ms. So for getting more than one instance of I, instead of
 * <code>Flux&lt;M></code>, it will be <code>Mono&lt;E></code> <br>
 * For now we don't test this class separately. Instead we test each resource class that uses this class.
 **/
public class ViewService<E, M, I> {

    public static final String HIGH_LEVEL_QUERY_PARAM_NAME = "isHighLevelQuery";
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewService.class);
    private final String storeName;
    private final Class<E> externalClass;
    private final Class<M> middleClass;
    //TODO rename these 3 mappers
    private final Function<I, M> mapperFromItoE;
    private final Function<E, List<M>> listOfMfromEextractor;
    private final Function<List<M>, E> listOfMtoE;
    private final String pathPart;
    WebClient.Builder webClientBuilder;
    private String ip;
    private int port;
    private StreamsBuilderFactoryBean streams;
    private ViewResourcesClient commonClient;

    public ViewService(String ip, int port, StreamsBuilderFactoryBean streams,
                       String storeName, Class<E> externalClass, Class<M> middleClass,
                       Function<I, M> mapperFromItoE, Function<E, List<M>> listOfMfromEextractor, Function<List<M>, E> listOfMtoE,
                       String pathPart, ViewResourcesClient commonClient) {
        this.ip = ip;
        this.port = port;
        this.streams = streams;
        this.storeName = storeName;
        this.externalClass = externalClass;
        this.middleClass = middleClass;
        this.mapperFromItoE = mapperFromItoE;
        this.listOfMfromEextractor = listOfMfromEextractor;
        this.listOfMtoE = listOfMtoE;
        this.pathPart = pathPart;
        this.commonClient = commonClient;
        // We are not injecting here because the available web client does load balancing which is not wanted here
        this.webClientBuilder = WebClient.builder();
    }

    public Mono<E> getAll(boolean isHighLevelQuery) {
        var localData = getFromLocalStorage();
        if (isHighLevelQuery) {
            var remoteData = getAllFromRemoteStorage();
            var allData = Flux.concat(localData, remoteData);
            return allData.collectList().map(listOfMtoE);
        } else {
            return localData.collectList().map(listOfMtoE);
        }
    }
    //**
    // Use this method for fetching all of data ONLY if the assinged store is GLOBAL
    // **//
    public Mono<E> getAllFromGlobalStore() {
        return getFromLocalStorage().collectList().map(listOfMtoE);
    }

    private Flux<M> getAllFromRemoteStorage() {
        var metadataCollection = streams.getKafkaStreams().allMetadataForStore(storeName);
        return Flux.fromIterable(metadataCollection)
                .switchIfEmpty(Flux.error(() -> new ServiceUnavailableException("No metadata found for " + storeName)))
                .filter(this::isRemoteNode)
                .flatMap(this::getFromRemoteStorage)
                //TODO define correct error (and code) for the throwable below
                .doOnError(throwable -> Flux.error(() -> new ServiceUnavailableException("Error " + throwable.getMessage() + " when getting all for store" + storeName)));
    }

    private Flux<M> getFromRemoteStorage(StreamsMetadata metadata) {
        String url = String.format("http://%s:%d/api/views/%s?%s=false", metadata.host(), metadata.port(),
                pathPart, HIGH_LEVEL_QUERY_PARAM_NAME);
        return commonClient.getOne(externalClass, url).flatMapIterable(listOfMfromEextractor);
    }

    private Flux<M> getFromLocalStorage() {
        var store = waitUntilStoreIsQueryable();
        var it = store.all(); // hold a reference to close it later
        return Flux.fromIterable(() -> it)
                .filter(kv -> !isNullOrEmpty(kv.key))
                .filter(kv -> !isNull(kv.value))
                .map(kv -> kv.value)
                .map(mapperFromItoE)
                .doAfterTerminate(it::close);
    }

    private boolean isRemoteNode(StreamsMetadata metadata) {
        return !metadata.host().equals(ip) || metadata.port() != port;
    }

    public Mono<M> getById(String id) {
        var metadata = streams.getKafkaStreams().metadataForKey(storeName, id, new StringSerializer());

        if (metadata.equals(StreamsMetadata.NOT_AVAILABLE)) {
            LOGGER.error("Neither this or other instances has access to requested key. Metadata: {}", metadata);
            return Mono.empty();//No metadata for that key
        }

        if (metadata.host().equals(ip) && metadata.port() == port) {
            LOGGER.debug("Querying local store {} for id: {}", storeName, id);
            var store = waitUntilStoreIsQueryable();
            return Optional.ofNullable(store.get(id))
                    .map(mapperFromItoE)
                    .map(Mono::just)
                    .orElseGet(() -> Mono.empty());//No data for that key locally
        }

        var url = String.format("http://%s:%d/api/views/%s/%s", metadata.host(), metadata.port(), pathPart, id);
        LOGGER.debug("Querying other instance's {} store for id: {} from {}", storeName, id, url);
        return commonClient.getOne(middleClass, url)
                .switchIfEmpty(Mono.empty());//No data for that key remotely
    }

    public Mono<M> getByIdFromGlobalStore(String id) {
        var metadata = streams.getKafkaStreams().metadataForKey(storeName, id, new StringSerializer());

        if (metadata.equals(StreamsMetadata.NOT_AVAILABLE)) {
            LOGGER.error("Neither this or other instances has access to requested key. Metadata: {}", metadata);
            return Mono.empty();//No metadata for that key
        }

        LOGGER.debug("Querying local part of global store {} for id: {}", storeName, id);
        var store = waitUntilStoreIsQueryable();
        return Optional.ofNullable(store.get(id))
                .map(mapperFromItoE)
                .map(Mono::just)
                .orElseGet(() -> Mono.empty());//No data for that key locally
    }

    public ReadOnlyKeyValueStore<String, I> waitUntilStoreIsQueryable() {
        for (int i = 0; i < 10; i++) {
            try {
                return streams.getKafkaStreams().store(storeName, QueryableStoreTypes.keyValueStore());
            } catch (InvalidStateStoreException e2) {
                // store not yet ready for querying
                LOGGER.error(
                        "Invalid State Store Error while fetching the kafkaStream store: {}. A retry will happen after 300 ms",
                        storeName);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    LOGGER.error("interrupted while waiting for streams store to be ready");
                    throw new ServiceUnavailableException("interrupted while waiting for streams store to be ready");
                }
            }
        }
        throw new ServiceUnavailableException(
                "Invalid State Store Error while fetching the kafkaStream store. The service is not available at the moment");
    }
}
