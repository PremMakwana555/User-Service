package com.prem.userservice.kafka.producer;

import com.prem.userservice.kafka.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.user-events}")
    private String userEventsTopic;

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            // Set event metadata
            String correlationId = event.getCorrelationId() != null ? event.getCorrelationId()
                    : UUID.randomUUID().toString();
            event.setCorrelationId(correlationId);
            event.setTimestamp(Instant.now().toString());
            event.setSource("user-service");

            // Create producer record with headers
            ProducerRecord<String, Object> record = new ProducerRecord<>(
                    userEventsTopic,
                    event.getUserId(),
                    event);

            // Add headers as per PRD requirements
            record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("timestamp", event.getTimestamp().getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("source", "user-service".getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("eventType", "UserRegistered".getBytes(StandardCharsets.UTF_8)));

            // Send to Kafka
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("UserRegisteredEvent published successfully for userId={}, correlationId={}, offset={}",
                            event.getUserId(), correlationId, result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish UserRegisteredEvent for userId={}, correlationId={}",
                            event.getUserId(), correlationId, ex);
                }
            });
        } catch (Exception e) {
            log.error("Error publishing UserRegisteredEvent for userId={}", event.getUserId(), e);
        }
    }
}
