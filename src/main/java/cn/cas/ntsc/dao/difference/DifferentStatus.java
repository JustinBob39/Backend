package cn.cas.ntsc.dao.difference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "Operator-InfluxDB_Difference")
public class DifferentStatus {
    @Column(name = "time")
    private Instant time;

    @Column(name = "count")
    private Integer count;
}
