package email.perso.persoemail.service;

import email.perso.persoemail.model.EmailTemplate;
import email.perso.persoemail.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public void sendEmail(String clientName, Map<String, Object> variables) throws MessagingException {
        EmailTemplate template = emailTemplateRepository.findByClientName(clientName);
        if (template == null) {
            throw new IllegalArgumentException("No email template found for client: " + clientName);
        }

        Context context = new Context();
        context.setVariables(variables);

        String body = templateEngine.process(template.getTemplateName(), context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(variables.get("email").toString());
        helper.setSubject(template.getSubject());
        helper.setText(body, true);

        mailSender.send(message);
    }
}
