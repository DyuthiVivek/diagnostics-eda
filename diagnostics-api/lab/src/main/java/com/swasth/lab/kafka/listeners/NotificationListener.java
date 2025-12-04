package com.swasth.lab.kafka.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NotificationListener {

    @KafkaListener(topics = "diagnostics.result.ready")
    public void handleResultReady(@Payload Map<String, Object> event) {
        log.info("[Notification] Result ready!");
        log.info("Order ID: {}", event.get("orderId"));
        log.info("Summary : {}", event.get("summary"));
    }
}
