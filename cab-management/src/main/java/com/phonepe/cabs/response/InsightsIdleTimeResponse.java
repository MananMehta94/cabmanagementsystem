package com.phonepe.cabs.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
public class InsightsIdleTimeResponse {
    @NonNull
    private String registrationNo;
    @NonNull
    private String startTime;
    @NonNull
    private String endTime;
    @NonNull
    private Long totalIdleTimeInSecs;
}
