package com.nc.airport.backend.util.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Log4j2
public class TicketSender {
    public static MailAuthenticator authenticator;

    public static void authenticate(String username, String password) {
        authenticator = new TicketSender.MailAuthenticator(username, password);
    }

    public static boolean sendTicketById(byte[] ticket,
                                         String toEmail) {

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.fastmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.fastmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, authenticator);

        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Thank you for choosing our airline. Here is you'r ticket.");

            DataSource dataSource = new ByteArrayDataSource(ticket, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("Ticket.pdf");

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            InternetAddress iaSender = new InternetAddress(authenticator.getUsername());
            InternetAddress iaRecipient = new InternetAddress(toEmail);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject("NC Airport ticket pdf");
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);

            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("Error sending ticket to the address: " + toEmail, e);
            return false;
        }
    }

    @Getter
    @AllArgsConstructor
    private static class MailAuthenticator extends Authenticator {
        private String username;
        private String password;

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

}
