package com.phonepe.cabs.data.dao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
public class Booking {
    private String registrationNo;
    private Integer cityId;
    private Long bookingId;
    private String bookingStatus;
}
