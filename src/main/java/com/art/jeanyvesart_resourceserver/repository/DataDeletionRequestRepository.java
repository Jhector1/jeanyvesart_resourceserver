// src/main/java/com/art/jeanyvesart_resourceserver/repository/DataDeletionRequestRepository.java
package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.DataDeletionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataDeletionRequestRepository extends JpaRepository<DataDeletionRequest, Long> {
    Optional<DataDeletionRequest> findByToken(String token);
}
