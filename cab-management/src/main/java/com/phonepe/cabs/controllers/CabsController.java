package com.phonepe.cabs.controllers;

import com.phonepe.cabs.data.dao.Booking;
import com.phonepe.cabs.data.dao.Cab;
import com.phonepe.cabs.data.dao.CabState;
import com.phonepe.cabs.data.dao.TravelLog;
import com.phonepe.cabs.data.repository.CabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phonepe/cab")
public class CabsController {

    private final CabRepository cabRepository;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCab(@RequestBody Cab cab){
        cabRepository.registerCab(cab);
    }

    @PutMapping("/update/city/{registrationNo}")
    @ResponseStatus(HttpStatus.OK)
    public void registerCab(@PathVariable String registrationNo, @RequestBody Cab cab){
        cabRepository.updateCabCity(registrationNo, cab.getCityId());
    }

    @PutMapping("/update/state")
    @ResponseStatus(HttpStatus.OK)
    public void updateCabState(@RequestBody CabState cabState){
        cabRepository.updateCabState(cabState.getRegistrationNo(), cabState.getStateId());
    }

    @PostMapping("/book/{cityId}")
    @ResponseStatus(HttpStatus.OK)
    public Booking getBooking(@PathVariable Integer cityId){
        return cabRepository.bookCab(cityId);
    }

    @GetMapping("/history/{cabId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TravelLog> getCabTravelHistory(@PathVariable String cabId){
        return cabRepository.getCabTravelHistory(cabId);
    }

}
