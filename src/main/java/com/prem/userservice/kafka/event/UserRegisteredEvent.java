package com.prem.userservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private String userId; // Changed from Long to String to match UUID
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredAt;

    // Event metadata
    private String correlationId;
    private String timestamp;
    private String source;
}
