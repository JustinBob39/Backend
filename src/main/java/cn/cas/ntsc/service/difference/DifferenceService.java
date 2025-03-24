package cn.cas.ntsc.service.difference;

import cn.cas.ntsc.converter.difference.DifferenceConverter;
import cn.cas.ntsc.dao.difference.Difference;
import cn.cas.ntsc.dao.difference.DifferentStatus;
import cn.cas.ntsc.dto.difference.DifferenceDTO;

import cn.cas.ntsc.dto.difference.DifferentStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class DifferenceService {
    private static final String measureName = "Operator-InfluxDB_Difference";
    private static final String DBName = "difference";
    final private InfluxDB influxDB;

    @Autowired
    public DifferenceService(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public List<DifferenceDTO> queryRange(final Integer parentId, final Long rangeBegin, final Long rangeEnd) {
        // SELECT BOTTOM("water_level",3),"location"
        // FROM "h2o_feet" WHERE time >= '2015-08-18T00:00:00Z' AND time <= '2015-08-18T00:54:00Z'
        // GROUP BY time(24m) ORDER BY time DESC
        final Instant instantBegin = Instant.ofEpochMilli(rangeBegin);
        final Instant instantEnd = Instant.ofEpochMilli(rangeEnd);
        if (instantBegin.isAfter(instantEnd)) {
            return null;
        }
        final String timeBegin = instantBegin.toString();
        final String timeEnd = instantEnd.toString();
        final String queryString = String.format(
                "SELECT * FROM \"%s\" WHERE \"parentId\" = '%d' AND \"time\" >= '%s' AND \"time\" <= '%s' ORDER BY \"time\" DESC LIMIT 256",
                measureName, parentId, timeBegin, timeEnd
        );
        log.info("Executing SQL on {}.{}", DBName, measureName);
        log.info(queryString);
        final Query query = new Query(queryString, DBName);
        final QueryResult queryResult = influxDB.query(query);
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<Difference> pojoList = influxDBResultMapper.toPOJO(queryResult, Difference.class);
        return pojoList.stream().flatMap(difference -> DifferenceConverter.convert(difference).stream()).toList();
    }

    public List<DifferenceDTO> queryRecent(final Integer parentId) {
        final String queryString = String.format(
                "SELECT * FROM \"%s\" WHERE \"parentId\" = '%s' ORDER BY \"time\" DESC LIMIT 64",
                measureName, parentId
        );
        log.info("Executing SQL on {}.{}", DBName, measureName);
        log.info(queryString);
        final Query query = new Query(queryString, DBName);
        final QueryResult queryResult = influxDB.query(query);
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<Difference> pojoList = influxDBResultMapper.toPOJO(queryResult, Difference.class);
        return pojoList.stream().flatMap(difference -> DifferenceConverter.convert(difference).stream()).toList();
    }

    public DifferentStatusDTO queryStatus() {
        // last 15 min
        final Instant start = Instant.now().minusSeconds(15 * 60).truncatedTo(ChronoUnit.MINUTES);
        final String startString = start.toString();
        final Instant finish = Instant.now().truncatedTo(ChronoUnit.MINUTES);
        final String finishString = finish.toString();

        final DifferentStatusDTO differentStatusDTO = new DifferentStatusDTO();
        differentStatusDTO.setTimeStatus(new TreeMap<>());
        Instant currentTime = start;
        while (!currentTime.isAfter(finish)) {
            differentStatusDTO.getTimeStatus().put(currentTime, new ArrayList<>());
            differentStatusDTO.getTimeStatus().get(currentTime).addAll(Arrays.asList(0, 0, 0));
            currentTime = currentTime.plusSeconds(60);
        }

        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<String> status = List.of("NORMAL", "TIMEOUT", "INITIAL");
        final AtomicInteger idx = new AtomicInteger();
        status.forEach(st -> {
            final String queryString = String.format(
                    "SELECT COUNT(\"frameStatus\") FROM \"%s\" WHERE \"time\" >= '%s' AND \"time\" <= '%s' AND \"frameStatus\" = '%s' GROUP BY \"frameStatus\", time(1m)",
                    measureName, startString, finishString, st);
            log.info("Executing SQL on {}.{}", DBName, measureName);
            log.info(queryString);
            final Query query = new Query(queryString, DBName);
            final QueryResult queryResult = influxDB.query(query);
            final List<DifferentStatus> pojoList = influxDBResultMapper.toPOJO(queryResult, DifferentStatus.class);
            pojoList.forEach(pojo -> {
                differentStatusDTO.getTimeStatus().get(pojo.getTime()).set(idx.get(), pojo.getCount());
            });
            idx.getAndIncrement();
        });
        return differentStatusDTO;
    }
}
