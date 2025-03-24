package cn.cas.ntsc;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class InfluxDBInterfaceTest {
    @Test
    public void fakeDataDifference() throws InterruptedException {
        final String serverURL = "http://localhost:8086";
        final String database = "difference";
        final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, "admin", "admin");
        influxDB.setDatabase(database);
        influxDB.write(Point.measurement("Operator-InfluxDB_Difference")
                .time(Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("parentId", "1")
                .addField("frameStatus", "NORMAL")
                .addField("parentStatus", "NORMAL")
                .addField("parentEventTime", "250321000000")
                .addField("firstChildDuration", "10MIN")
                .addField("firstChildEventTime", "250321000000")
                .addField("firstChildId", 1)
                .addField("firstChildValueFirst_Float", 1.11)
                .addField("firstChildValueFirst_String", "1.11")
                .addField("firstChildValueSecond_Float", 2.22)
                .addField("firstChildValueSecond_String", "2.22")
                .addField("secondChildDuration", "5MIN")
                .addField("secondChildEventTime", "250321000000")
                .addField("secondChildId", 2)
                .addField("secondChildValueFirst_Float", 3.33)
                .addField("secondChildValueFirst_String", "3.33")
                .addField("secondChildValueSecond_Float", 4.44)
                .addField("secondChildValueSecond_String", "4.44")
                .addField("thirdChildDuration", "3MIN")
                .addField("thirdChildEventTime", "250321000000")
                .addField("thirdChildId", 3)
                .addField("thirdChildValueFirst_Float", 5.55)
                .addField("thirdChildValueFirst_String", "5.55")
                .addField("thirdChildValueSecond_Float", 6.66)
                .addField("thirdChildValueSecond_String", "6.66")
                .build());
        System.out.println("Difference normal insert");

        Thread.sleep(60 * 1000);
        influxDB.write(Point.measurement("Operator-InfluxDB_Difference")
                .time(Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("parentId", "1")
                .addField("frameStatus", "TIMEOUT")
                .addField("parentStatus", "NORMAL")
                .addField("parentEventTime", "250321000000")
                .addField("firstChildDuration", "10MIN")
                .addField("firstChildEventTime", "250321000000")
                .addField("firstChildId", 1)
                .addField("firstChildValueFirst_Float", 1.11)
                .addField("firstChildValueFirst_String", "1.11")
                .addField("firstChildValueSecond_Float", 2.22)
                .addField("firstChildValueSecond_String", "2.22")
                .addField("secondChildDuration", "5MIN")
                .addField("secondChildEventTime", "250321000000")
                .addField("secondChildId", 2)
                .addField("secondChildValueFirst_Float", 3.33)
                .addField("secondChildValueFirst_String", "3.33")
                .addField("secondChildValueSecond_Float", 4.44)
                .addField("secondChildValueSecond_String", "4.44")
                .addField("thirdChildDuration", "3MIN")
                .addField("thirdChildEventTime", "250321000000")
                .addField("thirdChildId", 3)
                .addField("thirdChildValueFirst_Float", 5.55)
                .addField("thirdChildValueFirst_String", "5.55")
                .addField("thirdChildValueSecond_Float", 6.66)
                .addField("thirdChildValueSecond_String", "6.66")
                .build());
        System.out.println("Difference timeout insert");

        Thread.sleep(60 * 1000);
        influxDB.write(Point.measurement("Operator-InfluxDB_Difference")
                .time(Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("parentId", "1")
                .addField("frameStatus", "INITIAL")
                .addField("parentStatus", "NORMAL")
                .addField("parentEventTime", "250321000000")
                .addField("firstChildDuration", "10MIN")
                .addField("firstChildEventTime", "250321000000")
                .addField("firstChildId", 1)
                .addField("firstChildValueFirst_Float", 1.11)
                .addField("firstChildValueFirst_String", "1.11")
                .addField("firstChildValueSecond_Float", 2.22)
                .addField("firstChildValueSecond_String", "2.22")
                .addField("secondChildDuration", "5MIN")
                .addField("secondChildEventTime", "250321000000")
                .addField("secondChildId", 2)
                .addField("secondChildValueFirst_Float", 3.33)
                .addField("secondChildValueFirst_String", "3.33")
                .addField("secondChildValueSecond_Float", 4.44)
                .addField("secondChildValueSecond_String", "4.44")
                .addField("thirdChildDuration", "3MIN")
                .addField("thirdChildEventTime", "250321000000")
                .addField("thirdChildId", 3)
                .addField("thirdChildValueFirst_Float", 5.55)
                .addField("thirdChildValueFirst_String", "5.55")
                .addField("thirdChildValueSecond_Float", 6.66)
                .addField("thirdChildValueSecond_String", "6.66")
                .build());
        System.out.println("Difference initial insert");

        influxDB.close();
    }

    @Test
    public void fakeDataOther() {
        final String serverURL = "http://localhost:8086";
        final String database = "other";
        final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, "admin", "admin");
        influxDB.setDatabase(database);
        influxDB.write(Point.measurement("Operator-InfluxDB_Other")
                .time(Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("id", "1")
                .addField("name", "Alice")
                .addField("age", 22)
                .build());
        influxDB.flush();
        influxDB.write(Point.measurement("Operator-InfluxDB_Other")
                .time(Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("id", "2")
                .addField("name", "Bob")
                .addField("age", 23)
                .build());
        influxDB.flush();

        influxDB.close();

        System.out.println("Other insert finish");
    }

    @Test
    public void getTimestamp() {
        Instant instant = Instant.now();
        System.out.println("10min before:");
        System.out.println(instant.minusSeconds(10 * 60).toEpochMilli());
        System.out.println("now:");
        System.out.println(instant.toEpochMilli());
    }
}
