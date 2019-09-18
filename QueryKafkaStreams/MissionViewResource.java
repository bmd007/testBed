
import com.cabonline.mission.dto.view.MissionStatusDto;
import com.cabonline.mission.dto.view.MissionStatusesDto;
import com.cabonline.mission.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/views/missions")
public class MissionViewResource {

    private MissionViewService service;

    public MissionViewResource(MissionViewService service) {
        this.service = service;
    }

    //isHighLevelQuery query param is related to inter instance communication and it should be true in normal operations or not defined
    @GetMapping
    public Mono<MissionStatusesDto> getMissions(@RequestParam(required = false, value = ViewService.HIGH_LEVEL_QUERY_PARAM_NAME,
            defaultValue = "true") boolean isHighLevelQuery) {
        return service.getAll(isHighLevelQuery);
    }

    @GetMapping("/{id}")
    public Mono<MissionStatusDto> getMissionById(@PathVariable("id") String id) {
        return service.getById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format("%s not found (%s doesn't exist).", "mission", id))));
    }
}
