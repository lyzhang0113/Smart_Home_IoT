package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Humidity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HumidityRepository extends CrudRepository<Humidity, Integer> {

    @Query(value = "SELECT * FROM humidity ORDER BY date_created DESC LIMIT 10",
            nativeQuery = true)
    List<Humidity> findAllRecentHumidityData();

    @Query(value = "SELECT * FROM humidity ORDER BY date_created DESC LIMIT 1", nativeQuery = true)
    Humidity findMostRecentHumidity();

    @Query(value = "SELECT AVG(data) FROM humidity WHERE Date(date_created) = DATE(NOW() - INTERVAL 1 DAY)", nativeQuery = true)
    Float findAvgYesterdayHumidity();
}
