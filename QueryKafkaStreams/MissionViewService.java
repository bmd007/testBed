
import com.cabonline.mission.Stores;
import com.cabonline.mission.domain.Mission;
import com.cabonline.mission.dto.view.MissionStatusDto;
import com.cabonline.mission.dto.view.MissionStatusesDto;
import com.cabonline.mission.mapper.MissionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class MissionViewService extends ViewService<MissionStatusesDto, MissionStatusDto, Mission> {

    final static Function<MissionStatusesDto, List<MissionStatusDto>> LIST_EXTRACTOR = MissionStatusesDto::getMissions;
    final static Function<List<MissionStatusDto>, MissionStatusesDto> LIST_WRAPPER = MissionStatusesDto::new;
    final static Function<Mission, MissionStatusDto> DTO_MAPPER = MissionMapper::map;

    public MissionViewService(StreamsBuilderFactoryBean streams,
                              @Value("${kafka.streams.server.config.app-ip}") String ip,
                              @Value("${kafka.streams.server.config.app-port}") int port,
                              ViewResourcesClient commonClient) {
        super(ip, port, streams, Stores.MISSION_STATE,
                MissionStatusesDto.class, MissionStatusDto.class,
                DTO_MAPPER, LIST_EXTRACTOR, LIST_WRAPPER,
                "missions", commonClient);
    }
}
