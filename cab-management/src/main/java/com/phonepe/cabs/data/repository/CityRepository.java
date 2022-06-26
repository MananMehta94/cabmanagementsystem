package com.phonepe.cabs.data.repository;

import com.phonepe.cabs.data.dao.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CityRepository {

    @Autowired
    NamedParameterJdbcTemplate cabManagementNamedParameterJdbcTemplate;

    public void registerCity(City city){
        String registerCitySql = "insert into cities(city_name) values(:cityName)";
        Map<String, Object> params = new HashMap<>();
        params.put("cityName", city.getCityName());
        cabManagementNamedParameterJdbcTemplate.update(registerCitySql, params);
    }
}
