package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.dto.EmailClient;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import com.art.jeanyvesart_resourceserver.service.EmailServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(produces = "application/json")
public class EmailController {

    @Value("${base.url.client}")
    private String url;
    private static String baseUrl;

    private static final DateTimeFormatter EMAIL_TS =
            DateTimeFormatter.ofPattern("EEE, MMM d, yyyy 'at' h:mm a z")
                    .withZone(ZoneId.systemDefault());

    @PostConstruct
    public void init() { baseUrl = url; }

    final EmailServiceImpl emailService;
    public final CustomerRepository customerRepository;

    public EmailController(EmailServiceImpl emailService, CustomerRepository customerRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    // ---------- HTML templates with named tokens (no %s) ----------
    private static final String ADMIN_HTML_TPL = """
        <!doctype html>
        <html>
        <head>
          <meta charset="utf-8">
          <meta name="color-scheme" content="light dark">
          <style>
            body{margin:0;background:#f6f7f9;color:#111;font-family:system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif}
            .container{max-width:640px;margin:0 auto;padding:24px}
            .card{background:#fff;border-radius:12px;border:1px solid #e5e7eb;box-shadow:0 8px 24px rgba(0,0,0,.06);overflow:hidden}
            .header{padding:16px 20px;border-bottom:1px solid #e5e7eb;background:linear-gradient(135deg,#0a7b3e,#16a34a);color:#fff}
            h1{font-size:18px;line-height:1.2;margin:0}
            .content{padding:20px}
            .meta{width:100%;border-collapse:collapse;margin:0 0 16px}
            .meta th,.meta td{font-size:14px;text-align:left;padding:8px 10px;border-bottom:1px solid #f1f5f9}
            .label{color:#6b7280;white-space:nowrap}
            .message{white-space:pre-wrap;background:#f9fafb;border:1px solid #e5e7eb;border-radius:8px;padding:12px}
            .footer{padding:16px 20px;border-top:1px solid #e5e7eb;color:#6b7280;font-size:12px}
            .pre{display:none!important;visibility:hidden;opacity:0;color:transparent;height:0;width:0;overflow:hidden;mso-hide:all}
            @media (prefers-color-scheme:dark){
              body{background:#0b0f14;color:#e5e7eb}
              .card{background:#0f172a;border-color:#1f2937}
              .header{border-bottom-color:#1f2937}
              .meta th,.meta td{border-bottom-color:#1f2937}
              .message{background:#0b1220;border-color:#1f2937}
              .footer{border-top-color:#1f2937}
            }
          </style>
        </head>
        <body>
          <span class="pre">New contact from {{PREHEADER}}</span>
          <div class="container">
            <div class="card">
              <div class="header"><h1>New Contact Form Submission</h1></div>
              <div class="content">
                <table class="meta" role="presentation">
                  <tr><th class="label">From</th><td>{{FROM}}</td></tr>
                  <tr><th class="label">Reply-To</th><td>{{REPLY_TO}}</td></tr>
                  <tr><th class="label">Subject</th><td>{{SUBJECT}}</td></tr>
                  <tr><th class="label">Received</th><td>{{RECEIVED}}</td></tr>
                </table>
                <div class="message">{{MESSAGE}}</div>
              </div>
              <div class="footer">Sent automatically by JeanYvesArt.com</div>
            </div>
          </div>
        </body>
        </html>
        """;

    private static final String USER_ACK_HTML_TPL = """
        <!doctype html>
        <html>
        <head>
          <meta charset="utf-8">
          <meta name="color-scheme" content="light dark">
          <style>
            body{margin:0;background:#f6f7f9;color:#111;font-family:system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif}
            .container{max-width:560px;margin:0 auto;padding:24px}
            .card{background:#fff;border-radius:12px;border:1px solid #e5e7eb;box-shadow:0 8px 24px rgba(0,0,0,.06);overflow:hidden}
            .header{padding:16px 20px;border-bottom:1px solid #e5e7eb;background:linear-gradient(135deg,#0a7b3e,#16a34a);color:#fff}
            h1{font-size:18px;line-height:1.2;margin:0}
            .content{padding:20px;font-size:15px;line-height:1.65}
            .muted{color:#6b7280;font-size:13px;margin-top:10px}
            .pre{display:none!important;visibility:hidden;opacity:0;color:transparent;height:0;width:0;overflow:hidden;mso-hide:all}
            @media (prefers-color-scheme:dark){
              body{background:#0b0f14;color:#e5e7eb}
              .card{background:#0f172a;border-color:#1f2937}
              .header{border-bottom-color:#1f2937}
              .muted{color:#9ca3af}
            }
          </style>
        </head>
        <body>
          <span class="pre">We received your message — {{SENT_ON}}</span>
          <div class="container">
            <div class="card">
              <div class="header"><h1>Thanks for contacting Jean&nbsp;Yves&nbsp;Art</h1></div>
              <div class="content">
                <p>We received your message and will reply as soon as possible.</p>
                <p class="muted">Sent on {{SENT_ON}}</p>
              </div>
            </div>
          </div>
        </body>
        </html>
        """;

    private static final String RESET_HTML_TPL = """
        <!doctype html>
        <html>
        <head>
          <meta charset="utf-8">
          <meta name="color-scheme" content="light dark">
          <style>
            body{margin:0;background:#f6f7f9;color:#111;font-family:system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif}
            .container{max-width:560px;margin:0 auto;padding:24px}
            .card{background:#fff;border-radius:12px;border:1px solid #e5e7eb;box-shadow:0 8px 24px rgba(0,0,0,.06);overflow:hidden}
            .header{padding:16px 20px;border-bottom:1px solid #e5e7eb;background:linear-gradient(135deg,#0a7b3e,#16a34a);color:#fff}
            h1{font-size:18px;line-height:1.2;margin:0}
            .content{padding:20px;font-size:15px;line-height:1.65}
            .btn{display:inline-block;margin-top:12px;padding:12px 18px;border-radius:999px;background:#0a7b3e;color:#fff;text-decoration:none}
            .muted{color:#6b7280;font-size:13px;margin-top:14px}
            code{background:#f3f4f6;padding:2px 6px;border-radius:6px}
            .pre{display:none!important;visibility:hidden;opacity:0;color:transparent;height:0;width:0;overflow:hidden;mso-hide:all}
            @media (prefers-color-scheme:dark){
              body{background:#0b0f14;color:#e5e7eb}
              .card{background:#0f172a;border-color:#1f2937}
              .header{border-bottom-color:#1f2937}
              .muted{color:#9ca3af}
              code{background:#111827}
            }
          </style>
        </head>
        <body>
          <span class="pre">Reset your password</span>
          <div class="container">
            <div class="card">
              <div class="header"><h1>Reset your password</h1></div>
              <div class="content">
                <p>Hi {{NAME}}, let’s reset your password. Click the button below:</p>
                <p><a class="btn" href="{{URL}}" target="_blank" rel="noopener">Reset Password</a></p>
                <p class="muted">If the button doesn’t work, copy and paste this link into your browser:</p>
                <p><code>{{URL}}</code></p>
                <p class="muted">For security, this link expires in 30 minutes.</p>
              </div>
            </div>
          </div>
        </body>
        </html>
        """;

    // ---------- endpoints ----------

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailClient emailClient) {
        try {
            final String now = EMAIL_TS.format(Instant.now());
            final String escapedMsg = esc(String.valueOf(emailClient.getMessage()));

            final String adminHtml = ADMIN_HTML_TPL
                    .replace("{{PREHEADER}}", esc(emailClient.getEmailFrom()) + " — " + now)
                    .replace("{{FROM}}", "Contact Form")
                    .replace("{{REPLY_TO}}", esc(emailClient.getEmailFrom()))
                    .replace("{{SUBJECT}}", esc(emailClient.getSubject()))
                    .replace("{{RECEIVED}}", now)
                    .replace("{{MESSAGE}}", escapedMsg);

            final String userAckHtml = USER_ACK_HTML_TPL
                    .replace("{{SENT_ON}}", now);

            // Admin copy
            emailService.sendSimpleMessage(
                    "myart@jeanyveshector.com",
                    "jeanyveshector@gmail.com",
                    emailClient.getSubject(),
                    adminHtml
            );

            // Acknowledgement
            emailService.sendSimpleMessage(
                    "myart@jeanyveshector.com",

                    emailClient.getEmailTo(),
                    "Thanks For Contacting Jean Yves Art",
                    userAckHtml
            );

            return new ResponseEntity<>("Thank for your message", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reset-password-email")
    public ResponseEntity<String> send_reset_email_password(@RequestBody EmailClient emailClient, HttpServletResponse response) {
        try {
            Optional<MyCustomer> optionalMyCustomer = customerRepository.findByEmail(emailClient.getEmailTo());
            if (optionalMyCustomer.isPresent()) {
                String uuidString = UUID.randomUUID().toString();
                log.info("token received, {}", uuidString);
                MyCustomer customer = optionalMyCustomer.get();

                String url = baseUrl + "/reset-password?token=" + uuidString + "&action=reset_password";

                final String resetHtml = RESET_HTML_TPL
                        .replace("{{NAME}}", esc(Optional.ofNullable(customer.getFullName()).orElse("there")))
                        .replace("{{URL}}", esc(url));

                emailService.sendSimpleMessage(
                        "no-reply@jeanyveshector.com",

                        emailClient.getEmailTo(),
                        "Reset Your Password",
                        resetHtml
                );

                customer.setResetToken(uuidString);
                customer.setResetTokenDate(new Date());
                customer.setResetTokenUsed(false);
                customerRepository.save(customer);

                Cookie cookie = new Cookie("token-validation", uuidString);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            String resetPasswordMessage =
                    "If this email is associated with an account, please use the sign-in link sent to "
                            + emailClient.getEmailTo()
                            + ".\n\nIt will expired after 30minutes."
                            + "Ensure to double-check your email; it must be associated with an account to receive the\n"
                            + "message containing a link to reset your password.";

            return new ResponseEntity<>(resetPasswordMessage, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
