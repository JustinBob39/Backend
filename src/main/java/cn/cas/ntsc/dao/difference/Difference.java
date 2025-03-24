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
public class Difference {
    @Column(name = "time")
    private Instant time;

    @Column(name = "frameStatus")
    private String frameStatus;

    @Column(name = "parentId", tag = true)
    private String parentId;

    @Column(name = "parentStatus")
    private String parentStatus;
    @Column(name = "parentEventTime")
    private String parentEventTime;

    @Column(name = "firstChildDuration")
    private String firstChildDuration;
    @Column(name = "firstChildEventTime")
    private String firstChildEventTime;
    @Column(name = "firstChildId")
    private Integer firstChildId;
    @Column(name = "firstChildValueFirst_Float")
    private Double firstChildValueFirst_Float;
    @Column(name = "firstChildValueFirst_String")
    private String firstChildValueFirst_String;
    @Column(name = "firstChildValueSecond_Float")
    private Double firstChildValueSecond_Float;
    @Column(name = "firstChildValueSecond_String")
    private String firstChildValueSecond_String;

    @Column(name = "secondChildDuration")
    private String secondChildDuration;
    @Column(name = "secondChildEventTime")
    private String secondChildEventTime;
    @Column(name = "secondChildId")
    private Integer secondChildId;
    @Column(name = "secondChildValueFirst_Float")
    private Double secondChildValueFirst_Float;
    @Column(name = "secondChildValueFirst_String")
    private String secondChildValueFirst_String;
    @Column(name = "secondChildValueSecond_Float")
    private Double secondChildValueSecond_Float;
    @Column(name = "secondChildValueSecond_String")
    private String secondChildValueSecond_String;

    @Column(name = "thirdChildDuration")
    private String thirdChildDuration;
    @Column(name = "thirdChildEventTime")
    private String thirdChildEventTime;
    @Column(name = "thirdChildId")
    private Integer thirdChildId;
    @Column(name = "thirdChildValueFirst_Float")
    private Double thirdChildValueFirst_Float;
    @Column(name = "thirdChildValueFirst_String")
    private String thirdChildValueFirst_String;
    @Column(name = "thirdChildValueSecond_Float")
    private Double thirdChildValueSecond_Float;
    @Column(name = "thirdChildValueSecond_String")
    private String thirdChildValueSecond_String;
}
