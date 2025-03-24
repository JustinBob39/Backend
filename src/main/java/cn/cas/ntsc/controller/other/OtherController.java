package cn.cas.ntsc.controller.other;

import cn.cas.ntsc.HttpResult;
import cn.cas.ntsc.dto.other.OtherDTO;
import cn.cas.ntsc.service.other.OtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/influxdb/other")
public class OtherController {
    private final OtherService otherService;

    public OtherController(OtherService otherService) {
        this.otherService = otherService;
    }

    @GetMapping("/id")
    public HttpResult queryId(@RequestParam final Integer id) {
        log.info("id query called on {}", id);
        List<OtherDTO> otherDTOS = otherService.queryId(id);
        return HttpResult.success("success", otherDTOS);
    }
}
