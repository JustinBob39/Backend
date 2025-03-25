package cn.cas.ntsc.dto.difference;

import com.opencsv.bean.CsvBindByPosition;
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
    @CsvBindByPosition(position = 0)
    private Instant time;

    @CsvBindByPosition(position = 1)
    private String frameStatus;

    @CsvBindByPosition(position = 2)
    private Integer parentId; // convert id to city name, using map
    @CsvBindByPosition(position = 4)
    private String parentStatus;
    @CsvBindByPosition(position = 3)
    private String parentEventTime;

    @CsvBindByPosition(position = 9)
    private String childDuration;
    @CsvBindByPosition(position = 6)
    private String childEventTime;
    @CsvBindByPosition(position = 5)
    private Integer childId;
    @CsvBindByPosition(position = 7)
    private String childValueFirst;
    @CsvBindByPosition(position = 8)
    private String childValueSecond;
}
