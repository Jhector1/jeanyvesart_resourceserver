// src/main/java/com/art/jeanyvesart_resourceserver/model/DataDeletionRequest.java
package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DataDeletionRequest {
    public enum Status { RECEIVED, EMAIL_SENT, VERIFIED, COMPLETED, REJECTED }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email @NotBlank
    private String email;

    private String fullName;

    /** Optionally store where the account is from, ex: GOOGLE, FACEBOOK, APPLE, PASSWORD */
    private String provider;

    /** Optional free-form reason from the user */
    @Column(length = 2000)
    private String reason;

    @Enumerated(EnumType.STRING)
    private Status status;

    /** One-time confirmation token sent by email */
    @Column(unique = true, nullable = false, length = 64)
    private String token;

    private Instant createdAt;
    private Instant verifiedAt;
    private Instant processedAt;

    /** Keep a minimal audit trail without storing the actual account after deletion */
    private String auditNote;

    @PrePersist
    public void prePersist() {
        if (token == null) token = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = Status.RECEIVED;
    }
}
