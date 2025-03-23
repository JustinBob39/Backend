package cn.cas.ntsc.service;

import cn.cas.ntsc.converter.Converter;
import cn.cas.ntsc.dao.Difference;
import cn.cas.ntsc.dao.DifferentStatus;
import cn.cas.ntsc.dto.DifferenceDTO;

import cn.cas.ntsc.dto.DifferentStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class InfluxDBService {
    private static final String measureName = "Operator-InfluxDB";
    private static final String DBName = "difference";
    final private InfluxDB influxDB;

    @Autowired
    public InfluxDBService(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public List<DifferenceDTO> queryRange(final String parentId, final Long rangeBegin, final Long rangeEnd) {
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
        try {
            final int id = Integer.parseInt(parentId);
            // prevent SQL injection
        } catch (NumberFormatException e) {
            log.info("parentId format error...");
            return null;
        }
        final String queryString = String.format(
                "SELECT * FROM \"%s\" WHERE \"parentId\" = '%s' AND \"time\" >= '%s' AND \"time\" <= '%s' ORDER BY \"time\" DESC LIMIT 256",
                measureName, parentId, timeBegin, timeEnd
        );
        log.info("SQL execute on DB {}", DBName);
        log.info(queryString);
        final Query query = new Query(queryString, DBName);
        final QueryResult queryResult = influxDB.query(query);
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<Difference> pojoList = influxDBResultMapper.toPOJO(queryResult, Difference.class);
        return pojoList.stream().flatMap(d -> Converter.convert(d).stream()).toList();
    }

    public List<DifferenceDTO> queryRecent(final String parentId) {
        try {
            // prevent SQL injection
            final int id = Integer.parseInt(parentId);
        } catch (NumberFormatException e) {
            log.info("parentId format error.");
            return null;
        }
        final String queryString = String.format(
                "SELECT * FROM \"%s\" WHERE \"parentId\" = '%s' ORDER BY \"time\" DESC LIMIT 64",
                measureName, parentId
        );
        log.info("Execute SQL on {}", DBName);
        log.info(queryString);
        final Query query = new Query(queryString, DBName);
        final QueryResult queryResult = influxDB.query(query);
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<Difference> pojoList = influxDBResultMapper.toPOJO(queryResult, Difference.class);
        return pojoList.stream().flatMap(d -> Converter.convert(d).stream()).toList();
    }

    public DifferentStatusDTO queryStatus() {
        // last 15 min
        final Instant start = Instant.now().minusSeconds(15 * 60);
        final String startString = start.toString();
        final List<String> status = List.of("NORMAL", "TIMEOUT", "INITIAL");
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final DifferentStatusDTO differentStatusDTO = new DifferentStatusDTO();
        differentStatusDTO.setTimeStatus(new TreeMap<>());
        final AtomicInteger idx = new AtomicInteger();
        status.forEach(st -> {
            final String queryString = String.format(
                    "SELECT COUNT(\"frameStatus\") FROM \"%s\" WHERE \"time\" >= '%s' AND \"frameStatus\" = '%s' GROUP BY \"frameStatus\", time(1m)",
                    measureName, startString, st);
            final Query query = new Query(queryString, DBName);
            final QueryResult queryResult = influxDB.query(query);
            final List<DifferentStatus> pojoList = influxDBResultMapper.toPOJO(queryResult, DifferentStatus.class);
            pojoList.forEach(pojo -> {
                if (!differentStatusDTO.getTimeStatus().containsKey(pojo.getTime())) {
                    differentStatusDTO.getTimeStatus().put(pojo.getTime(), new ArrayList<>());
                    differentStatusDTO.getTimeStatus().get(pojo.getTime()).addAll(Arrays.asList(0, 0, 0));

                }
                differentStatusDTO.getTimeStatus().get(pojo.getTime()).set(idx.get(), pojo.getCount());
            });
            idx.getAndIncrement();
        });
        return differentStatusDTO;
    }
}
