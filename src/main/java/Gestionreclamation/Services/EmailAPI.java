package Gestionreclamation.Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailAPI {

    private static final String USERNAME = "iyed.ouederni@esprit.tn";
    private static final String PASSWORD = "jzagybphgctjripq";

    public void sendEmail(String recipientEmail, String subject, String messageText) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message message = prepareMessage(session, USERNAME, recipientEmail, subject, messageText);

        Transport.send(message);
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recipientEmail, String subject, String messageText) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setContent(messageText, "text/html"); // Set the email content type to HTML
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}