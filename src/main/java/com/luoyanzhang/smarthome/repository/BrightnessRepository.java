package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Brightness;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BrightnessRepository extends CrudRepository<Brightness, Integer> {
    @Query(value = "SELECT * FROM brightness ORDER BY date_created DESC LIMIT 1", nativeQuery = true)
    Brightness findMostRecentBrightness();
}
