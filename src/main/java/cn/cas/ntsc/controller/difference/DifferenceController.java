package cn.cas.ntsc.controller.difference;

import cn.cas.ntsc.HttpResult;
import cn.cas.ntsc.dto.difference.DifferenceDTO;
import cn.cas.ntsc.dto.difference.DifferentStatusDTO;
import cn.cas.ntsc.service.difference.DifferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/influxdb/difference")
public class DifferenceController {

    private final DifferenceService differenceService;

    public DifferenceController(DifferenceService differenceService) {
        this.differenceService = differenceService;
    }

    @GetMapping("/range")
    public HttpResult queryRange(
            @RequestParam final Integer parentId,
            @RequestParam final Long rangeBegin,
            @RequestParam final Long rangeEnd) {
        log.info("range query called on {} from {} to {}", parentId, rangeBegin, rangeEnd);
        final List<DifferenceDTO> differenceDTOS = differenceService.queryRange(parentId, rangeBegin, rangeEnd);
        if (differenceDTOS == null) {
            return HttpResult.failure("Query parameter error");
        }
        return HttpResult.success("success", differenceDTOS);
    }

    @GetMapping("/recent")
    public HttpResult queryRecent(@RequestParam final Integer parentId) {
        log.info("recent query called on {}", parentId);
        final List<DifferenceDTO> differenceDTOS = differenceService.queryRecent(parentId);
        return HttpResult.success("success", differenceDTOS);
    }

    @GetMapping("/status")
    public HttpResult queryStatus() {
        log.info("status query called");
        final DifferentStatusDTO statusDTO = differenceService.queryStatus();
        return HttpResult.success("success", statusDTO);
    }
}
