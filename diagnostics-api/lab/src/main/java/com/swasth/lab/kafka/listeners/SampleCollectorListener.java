package com.swasth.lab.kafka.listeners;

import com.swasth.lab.model.LabOrder;
import com.swasth.lab.repository.LabOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.support.KafkaHeaders;

import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleCollectorListener {

    private final LabOrderRepository repo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "diagnostics.order.placed")
    public void handleOrderPlaced(
            @Payload Map<String, Object> event,
            @Header(KafkaHeaders.RECEIVED_KEY) String orderId
    ) {
        log.info("[Collector] Received order.placed for orderId={}", orderId);

        UUID oid = UUID.fromString(orderId);
        LabOrder order = repo.findById(oid).orElse(null);
        if (order == null) return;

        if (order.getStatus() == LabOrder.Status.COLLECTED) {
            return;
        }

        order.setStatus(LabOrder.Status.COLLECTED);
        repo.save(order);

        Map<String, Object> out = Map.of(
                "orderId", orderId,
                "eventType", "diagnostics.sample.collected"
        );

        kafkaTemplate.send("diagnostics.sample.collected", orderId, out);
        log.info("Published sample.collected for orderId={}", orderId);
    }
}
