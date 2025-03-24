package cn.cas.ntsc.dao.other;

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
@Measurement(name = "Operator-InfluxDB_Other")
public class Other {
    @Column(name = "time")
    private Instant time;

    @Column(name = "id", tag = true)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;
}
