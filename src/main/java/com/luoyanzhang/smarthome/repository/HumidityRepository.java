package com.luoyanzhang.smarthome.repository;

import com.luoyanzhang.smarthome.entity.Humidity;
import org.springframework.data.repository.CrudRepository;

public interface HumidityRepository extends CrudRepository<Humidity, Integer> {
}
