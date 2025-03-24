package cn.cas.ntsc.dto.difference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifferentStatusDTO {
    private Map<Instant, List<Integer>> timeStatus;
}
