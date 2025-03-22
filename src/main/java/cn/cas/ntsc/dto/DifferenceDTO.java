package cn.cas.ntsc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifferenceDTO {
    private Instant time;

    private String frameStatus;

    private String parentId;
    private String parentStatus;
    private String parentEventTime;

    private String childDuration;
    private String childEventTime;
    private Integer childId;
    private String childValueFirst;
    private String childValueSecond;
}
