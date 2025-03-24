package cn.cas.ntsc.service.other;

import cn.cas.ntsc.converter.other.OtherConverter;
import cn.cas.ntsc.dao.other.Other;
import cn.cas.ntsc.dto.other.OtherDTO;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class OtherService {
    private static final String measureName = "Operator-InfluxDB_Other";
    private static final String DBName = "other";
    final private InfluxDB influxDB;

    @Autowired
    public OtherService(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public List<OtherDTO> queryId(final Integer id) {
        final String queryString = String.format("SELECT * FROM \"%s\" WHERE \"id\" = '%d'", measureName, id);
        log.info("Executing SQL on {}.{}", DBName, measureName);
        log.info(queryString);
        final Query query = new Query(queryString, DBName);
        final QueryResult queryResult = influxDB.query(query);
        final InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        final List<Other> pojoList = influxDBResultMapper.toPOJO(queryResult, Other.class);
        return pojoList.stream().flatMap(other -> Stream.of(OtherConverter.convert(other))).toList();
    }
}
