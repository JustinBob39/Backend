package cn.cas.ntsc.service;

import cn.cas.ntsc.converter.Converter;
import cn.cas.ntsc.dao.Difference;
import cn.cas.ntsc.dto.DifferenceDTO;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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
}
