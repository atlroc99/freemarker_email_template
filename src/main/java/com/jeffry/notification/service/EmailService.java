package com.jeffry.notification.service;

import com.jeffry.notification.Dto.MailRequest;
import com.jeffry.notification.Dto.MailResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private Configuration freeMarkerConfig;

    public EmailService(JavaMailSender mailSender, Configuration freeMarkerConfig) {
        this.mailSender = mailSender;
        this.freeMarkerConfig = freeMarkerConfig;
    }

    public MailResponse sendEmail(MailRequest mailRequest, Map<String, Object> model) {
        MailResponse mailResponse = new MailResponse();
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

            Template template = freeMarkerConfig.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            System.out.println("freemarker generated html: " + html);

            helper.setTo(mailRequest.getTo());
            helper.setFrom(mailRequest.getFrom());
            helper.setSubject(mailRequest.getSubject());
            helper.setText(html, true);
            mailSender.send(mimeMessage);

            mailResponse.setMessage("Mail sent to " + mailRequest.getTo());
            mailResponse.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return mailResponse;
    }
}
