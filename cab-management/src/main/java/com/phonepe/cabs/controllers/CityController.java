package com.phonepe.cabs.controllers;

import com.phonepe.cabs.data.dao.City;
import com.phonepe.cabs.data.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phonepe/city")
public class CityController {

    private final CityRepository cityRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCity(@RequestBody City city){
        cityRepository.registerCity(city);
    }

}
