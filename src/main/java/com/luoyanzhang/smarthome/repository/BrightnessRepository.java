package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Brightness;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrightnessRepository extends CrudRepository<Brightness, Integer> {
    @Query(value = "SELECT * FROM brightness ORDER BY date_created DESC LIMIT 1", nativeQuery = true)
    Brightness findMostRecentBrightness();

    @Query(value = "SELECT AVG(data) FROM brightness WHERE Date(date_created) = DATE(NOW() - INTERVAL 1 DAY)", nativeQuery = true)
    Float findAvgYesterdayBrightness();

    @Query(value = "SELECT * FROM brightness WHERE date_created > (NOW() - INTERVAL 24 HOUR)", nativeQuery = true)
    List<Brightness> findAllLast24HourReadings();
}
