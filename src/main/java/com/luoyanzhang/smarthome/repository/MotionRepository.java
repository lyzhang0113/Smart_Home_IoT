package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Motion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MotionRepository extends CrudRepository<Motion, Integer> {
    @Query(value = "SELECT * FROM motion ORDER BY date_created DESC LIMIT 1", nativeQuery = true)
    Motion findMostRecentMotion();
}
