package com.phonepe.cabs.requests;

import lombok.Data;

@Data
public class InsightsIdleTimeRequest {
    private String registrationNo;
    private String startTime;
    private String endTime;
}
