package com.art.jeanyvesart_resourceserver.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Properties;

@Service
public class EmailServiceImpl  {
    private final JavaMailSenderImpl emailSender;

    public EmailServiceImpl(JavaMailSenderImpl emailSender) {
        this.emailSender = emailSender;
        // set once (don’t rebuild per send)
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        emailSender.setJavaMailProperties(props);
        emailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    public void sendSimpleMessage(String from, String to, String subject, String text) {
        if (looksLikeHtml(text)) {
            sendHtml(from, to, subject, text);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // keep your domain sender
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);                        // plain text path
        emailSender.send(message);
        System.out.println("...send (text)");
    }

    // Keep your existing attachment API; just allow HTML bodies here too
    public void sendMessageWithAttachment(String to, String from, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, looksLikeHtml(text)); // <-- render as HTML when applicable

            byte[] fileBytes = pathToAttachment.getBytes(StandardCharsets.UTF_8);
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            helper.addAttachment("invoice" + pathToAttachment, new ByteArrayResource(Base64.getDecoder().decode(base64File)));

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- helpers ----------
    private boolean looksLikeHtml(String s) {
        if (s == null) return false;
        String t = s.trim().toLowerCase(Locale.ROOT);
        return t.startsWith("<!doctype") || t.startsWith("<html") || t.contains("<body");
    }

    private void sendHtml(String from, String to, String subject, String html) {
        try {
            MimeMessage mime = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from); // Gmail may rewrite display, but this is fine
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);                 // <-- HTML mode
            // Optional (so Reply goes to the visitor):
            if (from != null && !from.isBlank()) helper.setReplyTo(from);
            emailSender.send(mime);
            System.out.println("...send (html)");
        } catch (MessagingException e) {
            // graceful fallback to text
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(stripTags(html));
            emailSender.send(msg);
        }
    }

    private String stripTags(String html) {
        return html.replaceAll("<[^>]+>", "").replace("&nbsp;", " ");
    }
}
