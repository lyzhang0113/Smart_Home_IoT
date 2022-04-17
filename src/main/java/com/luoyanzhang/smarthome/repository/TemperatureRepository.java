package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Integer> {

    @Query(value = "SELECT * FROM temperature ORDER BY date_created DESC LIMIT 10",
    nativeQuery = true)
    List<Temperature> findAllRecentTemperatureData();
}
