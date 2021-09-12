package com.PSproject.TvShowsTracker.utils.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


public class EmailMaker {

    private final static String EMAIL_ADMIN = "tvshowstracker2021@gmail.com";
    private final static String PERSONAL = "Support TST";

    public static void sendEmail(JavaMailSender emailSender, String to, String content, String subject) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage);
        msgHelper.setFrom(EMAIL_ADMIN, PERSONAL);
        msgHelper.setTo(to);
        msgHelper.setSubject(subject);
        msgHelper.setText(content, true);
        emailSender.send(mimeMessage);
    }
}
