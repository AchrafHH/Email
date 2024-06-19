package email.perso.persoemail.controller;


import email.perso.persoemail.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, String> request) {
        String clientName = request.get("clientName");
        String email = request.get("email");
        String message = request.get("message");

        try {
            Map<String, Object> variables = Map.of("email", email, "message", message);
            emailService.sendEmail(clientName, variables);
            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Error sending email: " + e.getMessage();
        }
}
}

