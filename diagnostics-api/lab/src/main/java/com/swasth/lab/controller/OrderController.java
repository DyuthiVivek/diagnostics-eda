package com.swasth.lab.controller;

import com.swasth.lab.dto.CreateOrderRequest;
import com.swasth.lab.model.LabOrder;
import com.swasth.lab.repository.LabOrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final LabOrderRepository orderRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {

        UUID orderId = UUID.randomUUID();

        LabOrder order = new LabOrder();
        order.setOrderId(orderId);
        order.setPatientId(request.getPatientId());
        order.setTestType(request.getTestType());
        order.setStatus(LabOrder.Status.PLACED);

        orderRepo.save(order);

        Map<String, Object> event = new LinkedHashMap<>();
        event.put("orderId", orderId.toString());
        event.put("patientId", request.getPatientId().toString());
        event.put("testType", request.getTestType());
        event.put("eventType", "diagnostics.order.placed");
        event.put("timestamp", new Date().toString());

        kafkaTemplate.send("diagnostics.order.placed", orderId.toString(), event);

        return ResponseEntity.ok(order);
    }
}
