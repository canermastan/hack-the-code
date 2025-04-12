package com.kodla.coz.utility;

import com.kodla.coz.model.Feedback;
import com.kodla.coz.model.User;
import com.kodla.coz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
public class Utilities { // FIXME rename this class to something more meaningful
    private UserService userService;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String emailAddress;

    private final JavaMailSender javaMailSender;

    public Utilities(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setUserService(UserService userService) { // For circular dependency issue
        this.userService = userService;
    }

    // usages of this class: AuthController
    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = emailAddress;
        String senderName = "Kodlacoz.com";
        String subject = "Lütfen hesabınızı doğrulayın";

        // Create a Thymeleaf context and set variables
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("user", user);
        String verificationURL = siteURL + "/verify?code=" + user.getVerificationCode();
        thymeleafContext.setVariable("verificationURL", verificationURL);

        // Process the Thymeleaf template
        String content = templateEngine.process("email/verification-email", thymeleafContext);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        helper.addInline("mail", new ClassPathResource("static/images/email/mail.png"));
        helper.addInline("check", new ClassPathResource("static/images/email/check.png"));
        helper.addInline("unlayer", new ClassPathResource("static/images/email/unlayer.png"));

        javaMailSender.send(message);
    }

    public void sendForgotPasswordMail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        String toAddress = recipientEmail;
        String fromAddress = emailAddress;
        String senderName = "Kodlacoz.com";
        String subject = "Parolanızı yenileyin";

        // Create a Thymeleaf context and set variables
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("link", link);

        // Process the Thymeleaf template
        String content = templateEngine.process("email/forgot-password", thymeleafContext);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        helper.addInline("mail", new ClassPathResource("static/images/email/mail.png"));
        helper.addInline("check", new ClassPathResource("static/images/email/check.png"));
        helper.addInline("unlayer", new ClassPathResource("static/images/email/unlayer.png"));

        javaMailSender.send(message);
    }

    public void sendFeedbackMail(Feedback feedback) throws MessagingException, UnsupportedEncodingException {
        String toAddress = emailAddress;
        String fromAddress = emailAddress;
        String senderName = "Kodlacoz.com";
        String subject = "Yeni Feedback Geldi!";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("Email: ").append(feedback.getEmail()).append("\n");
        sb.append("Feedback Type: ").append(feedback.getFeedbackType()).append("\n");
        sb.append("Feedback Message: ").append(feedback.getMessage()).append("\n");
        sb.append("Feedback Url: ").append(feedback.getUrl()).append("\n");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(sb.toString());

        javaMailSender.send(message);
    }

    public void sendAnyMail() throws MessagingException, UnsupportedEncodingException {
        String toAddress = "jcanermastan@gmail.com";
        String fromAddress = emailAddress;
        String senderName = "Kodlacoz.com";
        String subject = "Test Sürümü Çıktı!";

        // Create a Thymeleaf context and set variables
        Context thymeleafContext = new Context();

        // Process the Thymeleaf template
        String content = templateEngine.process("email/any-mail", thymeleafContext);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        helper.addInline("mail", new ClassPathResource("static/images/email/mail.png"));
        helper.addInline("check", new ClassPathResource("static/images/email/check.png"));
        helper.addInline("unlayer", new ClassPathResource("static/images/email/unlayer.png"));

        javaMailSender.send(message);
    }
    public void sendAnyMailToAllUsers() throws MessagingException, UnsupportedEncodingException {
        String fromAddress = emailAddress;
        String senderName = "Kodlacoz.com";
        String subject = "Test Sürümü Çıktı!";

        // Create a Thymeleaf context and set variables
        Context thymeleafContext = new Context();

        // Process the Thymeleaf template
        String content = templateEngine.process("email/any-mail", thymeleafContext);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set general properties for the email
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);
        helper.setText(content, true);

        // Inline resources
        helper.addInline("mail", new ClassPathResource("static/images/email/mail.png"));
        helper.addInline("check", new ClassPathResource("static/images/email/check.png"));
        helper.addInline("unlayer", new ClassPathResource("static/images/email/unlayer.png"));

        List<User> users = userService.findAllByVerifiedUsers();
        for (User user : users) {
            String toAddress = user.getEmail();
            try {
                helper.setTo(toAddress);
                javaMailSender.send(message);
            } catch (Exception ex) {
                throw new RuntimeException("Mail Gönderilemedi.");
            }
        }
    }

    // name of the file to be compiled
    public String generateRandomStringWithUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("_", "");
    }

    /**
     * Parametre olarak gelen komutu sunucuda çalıştırarak kodları çalıştırıp çıktısını döndürmek için kullanılan fonksiyondur
     * @param command
     * @return Çalıştırılan kodun çıktısı
     */
    public String compile(String command) {
        String output = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            System.out.println("command: " + command);
            Process process = Runtime.getRuntime().exec(command);

            process.waitFor();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            while ((output = stdInput.readLine()) != null) {
                stringBuilder.append('\n').append(output);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((output = stdError.readLine()) != null) {
                stringBuilder.append('\n').append(output);
            }

            if (stringBuilder.toString().startsWith("{\"")) {
                return "SUNUCU HATASI! Lütfen bizimle iletişime geçin. (1)";
            }

            return stringBuilder.toString();

        } catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }
        return "SUNUCU HATASI! Lütfen bizimle iletişime geçin. (2)";
    }

    // save profile picture file on the server
    // user-photos / USER_ID / FILE_NAME
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}

