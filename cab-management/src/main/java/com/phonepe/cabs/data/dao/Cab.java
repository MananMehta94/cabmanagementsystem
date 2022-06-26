package com.phonepe.cabs.data.dao;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Cab {
    private String registrationNo;
    private String registrationYear;
    private Integer cityId;
}
