// src/main/java/com/art/jeanyvesart_resourceserver/service/DataDeletionService.java
package com.art.jeanyvesart_resourceserver.service;

import com.art.jeanyvesart_resourceserver.model.DataDeletionRequest;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.DataDeletionRequestRepository;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DataDeletionService {

    private final DataDeletionRequestRepository deletionRepo;
    private final CustomerRepository customerRepo;
    private final JavaMailSender mailSender;
    final EmailServiceImpl emailService;


    @Value("${base.url.server}")
    private String baseUrl;

    @Value("${app.privacy.deletion.from:no-reply@jeanyveshector.com}")
    private String fromAddress;
    public void sendConfirmationEmail(DataDeletionRequest req) {
        String link = baseUrl + "/api/privacy/delete/confirm?token=" + req.getToken();
        String from = fromAddress;
        String to = req.getEmail();
        String subject = "Confirm your data deletion request";

        String name = (req.getFullName() != null && !req.getFullName().isBlank())
                ? " " + req.getFullName().trim()
                : "";

        // Minimal, broadly-compatible HTML (inline styles)
        String html = """
      <!doctype html><html><body style="margin:0;background:#f6f7f9">
        <div style="max-width:560px;margin:0 auto;padding:24px;
                    font:14px/1.5 -apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif;
                    color:#111">
          <table role="presentation" width="100%%" cellspacing="0" cellpadding="0"
                 style="background:#fff;border:1px solid #e5e7eb;border-radius:12px">
            <tr>
              <td style="padding:16px 20px;border-bottom:1px solid #e5e7eb;background:#0a7b3e">
                <h1 style="margin:0;font-size:18px;line-height:1.2;color:#fff">Confirm your data deletion request</h1>
              </td>
            </tr>
            <tr>
              <td style="padding:20px">
                <p style="margin:0 0 12px">Hi%s,</p>
                <p style="margin:0 0 12px">We received a request to delete your account and associated personal data.</p>
                <p style="margin:0 0 12px">To confirm, click the button below:</p>
                <p style="margin:0 0 16px">
                  <a href="%s" target="_blank"
                     style="display:inline-block;background:#16a34a;color:#fff;text-decoration:none;
                            padding:12px 18px;border-radius:10px;font-weight:700">Confirm Deletion</a>
                </p>
                <p style="margin:0 0 6px;color:#6b7280">If the button doesn’t work, copy and paste this link:</p>
                <p style="margin:0;padding:10px;border:1px solid #e5e7eb;border-radius:8px;background:#f3f4f6;
                          word-break:break-all;font-size:12px">%s</p>
                <p style="margin:12px 0 0;color:#6b7280">If you didn’t request this, please ignore this email.</p>
              </td>
            </tr>
          </table>
          <p style="margin:12px 8px 0;color:#6b7280;font-size:12px">This message was intended for %s</p>
        </div>
      </body></html>
      """.formatted(name, link, link, req.getEmail());

        // If your service has a dedicated HTML method:
        emailService.sendSimpleMessage(from, to, subject, html);

        // If your service uses a boolean flag instead, use this form:
        // emailService.sendMessage(from, to, subject, html, true);

        req.setStatus(DataDeletionRequest.Status.EMAIL_SENT);
        deletionRepo.save(req);
    }


    @Transactional
    public boolean verifyAndDelete(String token) {
        var reqOpt = deletionRepo.findByToken(token);
        if (reqOpt.isEmpty()) return false;

        var req = reqOpt.get();
        if (req.getStatus() == DataDeletionRequest.Status.COMPLETED) return true; // idempotent

        req.setVerifiedAt(Instant.now());
        req.setStatus(DataDeletionRequest.Status.VERIFIED);
        deletionRepo.save(req);

        // Find the customer by email and delete their account + cascade data.
        var customerOpt = customerRepo.findByEmail(req.getEmail());
        if (customerOpt.isPresent()) {
            MyCustomer customer = customerOpt.get();
            // NOTE: Your mappings on MyCustomer include orphanRemoval/cascade REMOVE
            // for favorites, cart, orders, reviews, and addresses.
            customerRepo.delete(customer);
            req.setAuditNote("Deleted customer data for email=" + req.getEmail());
        } else {
            req.setAuditNote("No account found for email=" + req.getEmail() + " — marking completed.");
        }

        req.setProcessedAt(Instant.now());
        req.setStatus(DataDeletionRequest.Status.COMPLETED);
        deletionRepo.save(req);
        return true;
    }
}
