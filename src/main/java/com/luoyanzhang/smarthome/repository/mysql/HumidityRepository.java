package com.luoyanzhang.smarthome.repository.mysql;

import com.luoyanzhang.smarthome.entity.mysql.Humidity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HumidityRepository extends CrudRepository<Humidity, Integer> {

    @Query(value = "SELECT * FROM humidity ORDER BY date_created DESC LIMIT 10",
    nativeQuery = true)
    List<Humidity> findAllRecentHumidityData();
}
