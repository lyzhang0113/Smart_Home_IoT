package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Temperature;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepository extends CrudRepository<Temperature, Integer> {
}
