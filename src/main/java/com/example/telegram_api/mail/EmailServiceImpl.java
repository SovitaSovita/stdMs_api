//package com.example.telegram_api.mail;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.util.Random;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    private final TemplateEngine emailTemplateEngine;
//    private final JavaMailSender emailSender;
//
//    @Value("${spring.mail.username}")
//    private String sender;
//
//    @Value("${baseUrl_img}")
//    private String baseURL;
//
//    @Value("${dashboard_url}")
//    private String dashboard;
//
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Autowired
//    private TemplateEngine templateEngine;
//
//    private static final String TEMPLATE = "message";
//
//    public EmailServiceImpl(TemplateEngine emailTemplateEngine, JavaMailSender emailSender) {
//        this.emailTemplateEngine = emailTemplateEngine;
//        this.emailSender = emailSender;
//    }
//
//    @Override
//    public int verifyCode(String login) throws MessagingException {
//
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//        int otp = generateOTP();
//        boolean html = true;
//
//        Context thymeleafContext = new Context(LocaleContextHolder.getLocale());
//        thymeleafContext.setVariable("name", login);
//        thymeleafContext.setVariable("otp", otp);
//
//        final String emailContent = this.emailTemplateEngine.process(TEMPLATE, thymeleafContext);
//
//        messageHelper.setFrom(sender);
//        messageHelper.setTo(login);
//        messageHelper.setSubject("Verification Code");
//        messageHelper.setText(emailContent, html);
//        emailSender.send(mimeMessage);
//        return otp;
//    }
//
//    @Override
//    public int resetPassword(String login) throws MessagingException {
//
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//        int otp = generateOTP();
//        boolean html = true;
//
//        Context thymeleafContext = new Context(LocaleContextHolder.getLocale());
//        thymeleafContext.setVariable("name", login);
//        thymeleafContext.setVariable("otp", otp);
//
//        final String emailContent = this.emailTemplateEngine.process(TEMPLATE, thymeleafContext);
//
//        messageHelper.setFrom(sender);
//        messageHelper.setTo(login);
//        messageHelper.setSubject("OTP Code");
//        messageHelper.setText(emailContent, html);
//        emailSender.send(mimeMessage);
//        return otp;
//    }
//
//    public int generateOTP() {
//        Random random = new Random();
//        return 100000 + random.nextInt(900000);
//    }
//
//    public void sendEmail(String to, String subject, String text, Boolean isAdmin, User user) {
//
////        System.out.println(">> email to :: " + to);
////        System.out.println(">> user to :: " + user);
//
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = null;
//
//        try {
//            helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
//
//            Context context = new Context();
//            context.setVariable("subject", subject);
//            context.setVariable("isAdmin", isAdmin);
//            context.setVariable("text", text);
//            context.setVariable("user", user);
//            context.setVariable("dashboard_url", dashboard);
//            context.setVariable("image_url", baseURL + user.getTransaction());
//
//            String htmlContent = templateEngine.process("to-admin-noti", context);
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true); // true indicates HTML content
//            javaMailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            // Handle exception appropriately
//            e.printStackTrace();
//        }
//    }
//}
