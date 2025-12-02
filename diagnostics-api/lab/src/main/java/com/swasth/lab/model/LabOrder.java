package com.swasth.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class LabOrder {

    @Id
    private UUID orderId;

    private UUID patientId;

    private String testType;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PLACED,
        COLLECTED,
        PROCESSED,
        RESULT_READY
    }
}
