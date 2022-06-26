package com.phonepe.cabs.controllers;

import com.phonepe.cabs.data.repository.InsightsRepository;
import com.phonepe.cabs.requests.InsightsIdleTimeRequest;
import com.phonepe.cabs.response.InsightsIdleTimeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phonepe/insights")
public class InsightsController {

    @Autowired
    InsightsRepository insightsRepository;

    @GetMapping("/idletime/{cabId}")
    @ResponseStatus(HttpStatus.OK)
    public InsightsIdleTimeResponse getIdleTime(@PathVariable String cabId, @RequestBody InsightsIdleTimeRequest request){
        Long idleTimeInSecs = insightsRepository.getIdleTimeInSeconds(cabId, request.getStartTime(), request.getEndTime());
        return new InsightsIdleTimeResponse(cabId, request.getStartTime(), request.getEndTime(), idleTimeInSecs);

    }
}
