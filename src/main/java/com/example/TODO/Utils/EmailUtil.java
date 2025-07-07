package com.example.TODO.Utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {
    public static void send(String to, String password) {
        String from = "24mdts37@kristujayanti.com"; // ⚠️ Replace with your email
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "fmug wibn cnfa iqak"); // ⚠️ Use App Password
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Your To-Do App Password");
            msg.setText("Your password is: " + password);
            Transport.send(msg);
            System.out.println("Password emailed to " + to);
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
