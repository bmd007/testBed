
import com.cabonline.mission.dto.common.VehicleIdDto;
import com.cabonline.mission.dto.view.MissionStatusesDto;
import com.cabonline.mission.dto.view.VehicleDetailDto;
import com.cabonline.mission.dto.view.VehicleSummaryDto;
import com.cabonline.mission.dto.view.VehiclesDto;
import com.cabonline.mission.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api/views/vehicles")
public class VehicleViewResource {

    private VehicleViewService vehicleService;
    private MissionViewService missionService;

    public VehicleViewResource(VehicleViewService vehicleService,
                               MissionViewService missionService) {
        this.vehicleService = vehicleService;
        this.missionService = missionService;
    }

    @GetMapping
    public Mono<VehiclesDto> getVehicles() {
        return vehicleService.getAllFromGlobalStore();
    }

    @GetMapping("/{vehicleId}")
    public Mono<VehicleSummaryDto> getVehicleSummaryById(@PathVariable("vehicleId") String vehicleId) {
        return vehicleService.getById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("%s not found (%s doesn't exist).", "vehicle", vehicleId))));
    }

    @GetMapping("/{countryCode}/{regNo}/details")
    public Mono<VehicleDetailDto> getVehicleDetailById(@PathVariable("countryCode") String countryCode, @PathVariable("regNo") String regNo) {
        return getVehicleSummaryById(String.format("%s:%s", countryCode, regNo))
                .flatMapIterable(VehicleSummaryDto::getCurrentMissions)
                .flatMap(missionService::getById)
                .collect(toSet())
                .map(missions ->
                        VehicleDetailDto
                                .builder()
                                .withId(new VehicleIdDto(countryCode, regNo))
                                .withCurrentMissions(missions)
                                .build()
                );
    }
}
