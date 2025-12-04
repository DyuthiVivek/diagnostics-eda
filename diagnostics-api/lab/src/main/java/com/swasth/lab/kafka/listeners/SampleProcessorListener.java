package com.swasth.lab.kafka.listeners;

import com.swasth.lab.model.LabOrder;
import com.swasth.lab.repository.LabOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;

import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleProcessorListener {

    private final LabOrderRepository repo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "diagnostics.sample.collected")
    public void handleSampleCollected(
            @Payload Map<String, Object> event,
            @Header(KafkaHeaders.RECEIVED_KEY) String orderId
    ) {
        log.info("[Processor] Received sample.collected for orderId={}", orderId);

        UUID oid = UUID.fromString(orderId);
        LabOrder order = repo.findById(oid).orElse(null);
        if (order == null) return;

        if (order.getStatus() == LabOrder.Status.PROCESSED) {
            return; // idempotent
        }

        // Simulate lab processing
        try { Thread.sleep(1000); } catch (Exception ignored) {}

        order.setStatus(LabOrder.Status.PROCESSED);
        repo.save(order);

        Map<String, Object> out = Map.of(
                "orderId", orderId,
                "eventType", "diagnostics.sample.processed"
        );

        kafkaTemplate.send("diagnostics.sample.processed", orderId, out);
        log.info("Published sample.processed for orderId={}", orderId);
    }
}
