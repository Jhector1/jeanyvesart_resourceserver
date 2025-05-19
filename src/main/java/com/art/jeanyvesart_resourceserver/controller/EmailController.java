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

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(produces = "application/json")

public class EmailController {
    @Value("${base.url.client}")
    private String url;
    @Value("${base.url.auth_server}")
    private String auth_base_url;
    private static String baseUrl;
    private static String authBaseUrl;
    @PostConstruct
    public void init() {
        baseUrl = url;
        authBaseUrl = auth_base_url;
    }
    final
    EmailServiceImpl emailService;
    public final CustomerRepository customerRepository;

    public EmailController(EmailServiceImpl emailService, CustomerRepository customerRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailClient emailClient) {
        try {
            emailService.sendSimpleMessage(emailClient.getEmailFrom(), emailClient.getEmailTo(), emailClient.getSubject(), emailClient.getMessage());

            emailService.sendSimpleMessage(emailClient.getEmailFrom(), emailClient.getEmailTo(), "Thanks For Contacting Jean Yves Art", "Thank you for reaching out to Jean Yves Art. We will respond to your inquiry as soon as possible.");


           // emailService.sendMessageWithAttachment(emailClient.getEmailTo(), emailClient.getEmailFrom(), emailClient.getSubject(), emailClient.getMessage(), emailClient.getFileAttachment());
            return new ResponseEntity<>("Thank for your message", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/reset-password-email")
    public ResponseEntity<String> send_reset_email_password(@RequestBody EmailClient emailClient, HttpServletResponse response) {
        try {
            log.info("about to send email");
//System.out.println(emailClient.getEmailTo());
            Optional<MyCustomer> optionalMyCustomer = customerRepository.findByEmail(emailClient.getEmailTo());
           if (optionalMyCustomer.isPresent()) {
                String uuidString = UUID.randomUUID().toString();
                log.info("token received, {}", uuidString);
                MyCustomer customer = optionalMyCustomer.get();
               log.info("customer, {}", customer);
                //emailService.sendSimpleMessage(emailClient.getEmailFrom(), emailClient.getEmailTo(), "Thanks For Contacting Jean Yves Art", "Thank you for reaching out to Jean Yves Art. We will respond to your inquiry as soon as possible.");
                String url = auth_base_url+"/reset-password?token=" + uuidString +"&action=reset_password";
//                String body = "<!DOCTYPE html>\n" +
//                        "<html lang=\"en\">\n" +
//                        "<head>\n" +
//                        "    <meta charset=\"UTF-8\">\n" +
//                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                        "    <title>Password Reset Email</title>\n" +
//                        "    <style>\n" +
//                        "        body {\n" +
//                        "            font-family: Arial, sans-serif;\n" +
//                        "            background-color: #f4f4f4;\n" +
//                        "            margin: 0;\n" +
//                        "            padding: 0;\n" +
//                        "        }\n" +
//                        "        .container {\n" +
//                        "            width: 80%;\n" +
//                        "            max-width: 600px;\n" +
//                        "            margin: 20px auto;\n" +
//                        "            background-color: #ffffff;\n" +
//                        "            padding: 20px;\n" +
//                        "            border-radius: 8px;\n" +
//                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
//                        "        }\n" +
//                        "        h2 {\n" +
//                        "            color: #333333;\n" +
//                        "        }\n" +
//                        "        .button {\n" +
//                        "            display: inline-block;\n" +
//                        "            padding: 10px 20px;\n" +
//                        "            font-size: 16px;\n" +
//                        "            color: #ffffff;\n" +
//                        "            background-color: #007bff;\n" +
//                        "            text-decoration: none;\n" +
//                        "            border-radius: 4px;\n" +
//                        "            margin-top: 10px;\n" +
//                        "        }\n" +
//                        "        .footer {\n" +
//                        "            margin-top: 20px;\n" +
//                        "            font-size: 14px;\n" +
//                        "            color: #666666;\n" +
//                        "        }\n" +
//                        "    </style>\n" +
//                        "</head>\n" +
//                        "<body>\n" +
//                        "    <div class=\"container\">\n" +
//                        "        <h1>Jean Yves Art</h1>\n" +
//                        "        <p>Hi "+customer.getFullName()+",</p>\n" +
//                        "        <h2>Let's reset your password</h2>\n" +
//                        "        <p><a href=\""+url+"\" class=\"button\">Reset Password</a></p>\n" +
//                        "        <p class=\"footer\">If the above button does not work for you, copy and paste the following into your browser's address bar:</p>\n" +
//                        "        <p class=\"footer\">{{url}}</p>\n" +
//                        "    </div>\n" +
//                        "</body>\n" +
//                        "</html>\n";
               String body = "<!DOCTYPE html>\n" +
                       "<html>\n" +
                       "<head>\n" +
                       "    <style>\n" +
                       "        /* Add any additional CSS here, but use inline styles for best compatibility */\n" +
                       "    </style>\n" +
                       "</head>\n" +
                       "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif;\">\n" +
                       "    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f4f4f4; padding: 20px;\">\n" +
                       "        <tr>\n" +
                       "            <td align=\"center\">\n" +
                       "                <table width=\"600\" cellspacing=\"0\" cellpadding=\"20\" border=\"0\" style=\"background-color: #ffffff; border-radius: 5px;\">\n" +
                       "                    <tr>\n" +
                       "                        <td style=\"text-align: center; color: #333333;\">\n" +
                       "                            <h1 style=\"color: #4CAF50;\">Jean Yves Art</h1>\n" +
                       "                            <p style=\"font-size: 16px; color: #555555;\">Hi "+customer.getFullName()+",</p>\n" +
                       "                            <h2 style=\"color: #4CAF50;\">Let's reset your password</h2>\n" +
                       "                            <p>\n" +
                       "                                <a href=\""+url+"\" style=\"display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Reset Password</a>\n" +
                       "                            </p>\n" +
                       "                            <p style=\"font-size: 14px; color: #666666;\">\n" +
                       "                                If the above button does not work for you, copy and paste the following into your browser's address bar:\n" +
                       "                            </p>\n" +
                       "                            <p style=\"font-size: 14px; color: #666666;\">\n" +
                       "                                "+url+"\n" +
                       "                            </p>\n" +
                       "                        </td>\n" +
                       "                    </tr>\n" +
                       "                </table>\n" +
                       "            </td>\n" +
                       "        </tr>\n" +
                       "    </table>\n" +
                       "</body>\n" +
                       "</html>\n";
               emailService.sendHtmlEmail("myart@jeanyveshector.com", emailClient.getEmailTo(), "Reset Your Password", body);

               //emailService.sendSimpleMessage(emailClient.getEmailTo(), "myart@jeanyveshector.com", "Reset Your Password", body);
                customer.setResetToken(uuidString);
                customer.setResetTokenDate(new Date());
                customer.setResetTokenUsed(false);
                customerRepository.save(customer);
//                String html = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "<head>\n" +
//                        "    <style>\n" +
//                        "        /* Add any additional CSS here, but use inline styles for best compatibility */\n" +
//                        "    </style>\n" +
//                        "</head>\n" +
//                        "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif;\">\n" +
//                        "    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f4f4f4; padding: 20px;\">\n" +
//                        "        <tr>\n" +
//                        "            <td align=\"center\">\n" +
//                        "                <table width=\"600\" cellspacing=\"0\" cellpadding=\"20\" border=\"0\" style=\"background-color: #ffffff; border-radius: 5px;\">\n" +
//                        "                    <tr>\n" +
//                        "                        <td style=\"text-align: center; color: #333333;\">\n" +
//                        "                            <h1 style=\"color: #4CAF50;\">Hello, World!</h1>\n" +
//                        "                            <p style=\"font-size: 16px; color: #555555;\">This is a colorful email example.</p>\n" +
//                        "                            <a href=\"#\" style=\"display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #4CAF50; text-decoration: none; border-radius: 5px;\">Click Here</a>\n" +
//                        "                        </td>\n" +
//                        "                    </tr>\n" +
//                        "                </table>\n" +
//                        "            </td>\n" +
//                        "        </tr>\n" +
//                        "    </table>\n" +
//                        "</body>\n" +
//                        "</html>\n";
                // Create a new cookie
            Cookie cookie = new Cookie("token-validation", uuidString);

            // Set cookie properties (optional)
            //cookie.setMaxAge(24 * 60 * 60); // Set the cookie's maximum age in seconds (1 day in this example)
            cookie.setPath("/"); // Set the path for which the cookie is valid (root path in this example)

            // Add the cookie to the response
            response.addCookie(cookie);



           }
            String resetPasswordMessage = "If this email is associated with an account, please use the sign-in link sent to " + emailClient.getEmailTo() + ".\n" +
                    "\n" +"It will expired after 30minutes."+

                    "Ensure to double-check your email; it must be associated with an account to receive the\n" +
                    "message containing a link to reset your password.";
            return new ResponseEntity<>(resetPasswordMessage, HttpStatus.OK);

          //  return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}