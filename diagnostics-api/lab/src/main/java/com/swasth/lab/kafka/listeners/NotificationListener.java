package com.swasth.lab.kafka.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationListener {

    @KafkaListener(topics = "diagnostics.result.ready")
    public void handleResultReady(@Payload Map<String, Object> event) {
        System.out.println("[Notification] Result ready!");
        System.out.println("Order ID: " + event.get("orderId"));
        System.out.println("Summary : " + event.get("summary"));
    }
}
