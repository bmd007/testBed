
import com.cabonline.mission.Stores;
import com.cabonline.mission.domain.Vehicle;
import com.cabonline.mission.domain.VehicleId;
import com.cabonline.mission.dto.view.VehicleSummaryDto;
import com.cabonline.mission.dto.view.VehiclesDto;
import com.cabonline.mission.mapper.VehicleMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import reactor.core.publisher.Mono;

@Service
public class VehicleViewService extends ViewService<VehiclesDto, VehicleSummaryDto, Vehicle> {

    private final static Function<VehiclesDto, List<VehicleSummaryDto>> LIST_EXTRACTOR = VehiclesDto::getVehicles;
    private final static Function<List<VehicleSummaryDto>, VehiclesDto> LIST_WRAPPER = VehiclesDto::new;
    private final static Function<Vehicle, VehicleSummaryDto> DTO_MAPPER = VehicleMapper::map;

    public VehicleViewService(StreamsBuilderFactoryBean streams,
                              @Value("${kafka.streams.server.config.app-ip}") String ip,
                              @Value("${kafka.streams.server.config.app-port}") int port,
                              ViewResourcesClient commonClient) {
        super(ip, port, streams, Stores.VEHICLE_GLOBAL_STATE_STORE,
                VehiclesDto.class, VehicleSummaryDto.class,
                DTO_MAPPER, LIST_EXTRACTOR, LIST_WRAPPER,
                "vehicles", commonClient);
    }

    public Mono<VehicleSummaryDto> getById(VehicleId id) {
        var stringId = String.format("%s:%s", id.getCountryCode(), id.getRegNo());
        return getByIdFromGlobalStore(stringId);
    }

}
