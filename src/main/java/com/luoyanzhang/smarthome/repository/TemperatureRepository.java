package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Integer> {

    @Query(value = "SELECT * FROM temperature ORDER BY date_created DESC LIMIT 10",
            nativeQuery = true)
    List<Temperature> findAllRecentTemperatureData();

    @Query(value = "SELECT * FROM temperature ORDER BY date_created DESC LIMIT 1", nativeQuery = true)
    Temperature findMostRecentTemperature();

    @Query(value = "SELECT AVG(data) FROM temperature WHERE Date(date_created) = DATE(NOW() - INTERVAL 1 DAY)", nativeQuery = true)
    Float findAvgYesterdayTemperature();

    @Query(value = "SELECT * FROM temperature WHERE date_created > (NOW() - INTERVAL 24 HOUR)", nativeQuery = true)
    List<Temperature> findAllLast24HourReadings();
}
