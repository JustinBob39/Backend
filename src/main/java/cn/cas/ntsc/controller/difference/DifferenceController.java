package cn.cas.ntsc.controller.difference;

import cn.cas.ntsc.HttpResult;
import cn.cas.ntsc.util.CustomColumnPositionStrategy;
import cn.cas.ntsc.dto.difference.DifferenceDTO;
import cn.cas.ntsc.dto.difference.DifferentStatusDTO;
import cn.cas.ntsc.service.difference.DifferenceService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
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

    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response, @RequestParam final Integer parentId) {
        final String filename = "data-" + parentId + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        try {
            final CustomColumnPositionStrategy<DifferenceDTO> mappingStrategy = new CustomColumnPositionStrategy<>();
            mappingStrategy.setType(DifferenceDTO.class);
            final StatefulBeanToCsv<DifferenceDTO> writer = new StatefulBeanToCsvBuilder<DifferenceDTO>(response.getWriter()).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withMappingStrategy(mappingStrategy).build();
            log.info("export query called on {}", parentId);
            final List<DifferenceDTO> differenceDTOS = differenceService.queryRecent(parentId);
            writer.write(differenceDTOS);
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e.getMessage());
        }
    }
}
