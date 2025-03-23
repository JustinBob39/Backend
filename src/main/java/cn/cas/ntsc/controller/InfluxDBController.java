package cn.cas.ntsc.controller;

import cn.cas.ntsc.HttpResult;
import cn.cas.ntsc.dto.DifferenceDTO;
import cn.cas.ntsc.dto.DifferentStatusDTO;
import cn.cas.ntsc.service.InfluxDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/influxdb/difference")
public class InfluxDBController {

    private final InfluxDBService influxDBService;

    public InfluxDBController(InfluxDBService influxDBService) {
        this.influxDBService = influxDBService;
    }

    @GetMapping("/range")
    public HttpResult queryRange(
            @RequestParam final String parentId,
            @RequestParam final Long rangeBegin,
            @RequestParam final Long rangeEnd) {
        log.info("range query called on {} from {} to {}", parentId, rangeBegin, rangeEnd);
        final List<DifferenceDTO> differenceDTOS = influxDBService.queryRange(parentId, rangeBegin, rangeEnd);
        if (differenceDTOS == null) {
            return HttpResult.failure("Time range error");
        }
        return HttpResult.success("success", differenceDTOS);
    }

    @GetMapping("/recent")
    public HttpResult queryRecent(@RequestParam final String parentId) {
        log.info("recent query called on {}", parentId);
        final List<DifferenceDTO> differenceDTOS = influxDBService.queryRecent(parentId);
        return HttpResult.success("success", differenceDTOS);
    }

    @GetMapping("/status")
    public HttpResult queryStatus() {
        log.info("status called");
        final DifferentStatusDTO statusDTO = influxDBService.queryStatus();
        return HttpResult.success("success", statusDTO);
    }
}
