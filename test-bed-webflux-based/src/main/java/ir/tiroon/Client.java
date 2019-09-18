package ir.tiroon;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Client {

    private final WebClient.Builder webClientBuilder;

    public Client(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> uploadAsset() {

        MultipartBodyBuilder mbuilder = new MultipartBodyBuilder();
        mbuilder.part("fieldPart", "fieldPart1");
        mbuilder.part("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf"));

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadWithFilePartAndFieldPart?fieldPart=123")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .syncBody(mbuilder.build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> uploadAsset2() {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadWithFilePartAndFieldPart?fieldPart=456")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fieldPart", "fieldPart2")
                        .with("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf")))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> uploadAsset3() {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadWithMonoMap?fieldPart=789")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fieldPart", "fieldPart3")
                        .with("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf")))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> uploadAsset4() {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadWithFlux?fieldPart=101112")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fieldPart", "fieldPart4")
                        .with("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf")))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> uploadAsset5() {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadWithMap?fieldPart=1234123")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fieldPart", "fieldPart5")
                        .with("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf")))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> uploadAsset6() {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8002/uploadLegacy")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fieldPart", "fieldPart6")
                        .with("filePart", new FileSystemResourceLoader().getResource("src/main/resources/abc.pdf")))
                .retrieve()
                .bodyToMono(String.class);
    }

    /////////////////////////////////////////////////////

    //this client method is to imitate behaviour of asset client upload method in fleetGW
    public Mono<String> uploadAsset7(MultiValueMap<String, ?> multipart) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8000/uploadCore")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipart))
                .retrieve()
                .bodyToMono(String.class);
    }

/////////////////////////////////////////////////
}
