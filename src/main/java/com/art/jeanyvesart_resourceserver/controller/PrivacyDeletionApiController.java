// src/main/java/com/art/jeanyvesart_resourceserver/controller/PrivacyDeletionApiController.java
package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.DataDeletionRequest;
import com.art.jeanyvesart_resourceserver.repository.DataDeletionRequestRepository;
import com.art.jeanyvesart_resourceserver.service.DataDeletionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/privacy/delete")
public class PrivacyDeletionApiController {

    private final DataDeletionRequestRepository deletionRepo;
    private final DataDeletionService deletionService;

    @Value("${base.url.client}")
    private String frontendBase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submit(@RequestBody @Valid DataDeletionForm body,
                                    @RequestHeader(value = "X-HP", required = false) String hp) {
        // Honeypot header (optional). If present, pretend success.
        if (StringUtils.hasText(hp)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("ok", true, "message", "Request received."));
        }

        var req = DataDeletionRequest.builder()
                .email(body.getEmail())
                .fullName(body.getFullName())
                .provider(body.getProvider())
                .reason(body.getReason())
                .status(DataDeletionRequest.Status.RECEIVED)
                .build();

        req = deletionRepo.save(req);
        deletionService.sendConfirmationEmail(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("ok", true, "email", req.getEmail(), "message", "Confirmation email sent."));
    }

    @GetMapping("/confirm")
    public RedirectView confirm(@RequestParam("token") String token) {
        boolean ok = deletionService.verifyAndDelete(token);
        String dest = ok
                ? frontendBase + "/privacy/delete/done"
                : frontendBase + "/privacy/delete/error";
        return new RedirectView(dest);
    }

    public static class DataDeletionForm {
        @jakarta.validation.constraints.Email
        @jakarta.validation.constraints.NotBlank
        private String email;
        private String fullName;
        private String provider;
        @jakarta.validation.constraints.Size(max = 2000)
        private String reason;
        @jakarta.validation.constraints.AssertTrue(message = "Please confirm you want to delete your data.")
        private boolean confirm;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public boolean isConfirm() { return confirm; }
        public void setConfirm(boolean confirm) { this.confirm = confirm; }
    }
}
