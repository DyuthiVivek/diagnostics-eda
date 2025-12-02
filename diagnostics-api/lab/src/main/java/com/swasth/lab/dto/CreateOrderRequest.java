package com.swasth.lab.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {
    private UUID patientId;
    private String testType;
}
