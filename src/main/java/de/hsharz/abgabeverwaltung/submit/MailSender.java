package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    public void sendMail(final BasicMail mail) {

        Session session = Session.getInstance(mail.getProperties(), new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return mail.getAuthenticator();
            }
        });

        try {
            System.out.println("Composing message");
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));
            for (String recipient : mail.getRecipients()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }

            for(String bcc : mail.getBCCRecipients()){
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }

            message.setSubject(mail.getSubject());

            Multipart mailContent = new MimeMultipart();

            BodyPart messageBody = new MimeBodyPart();
            messageBody.setText(mail.getBody());

            mailContent.addBodyPart(messageBody);

            for (File file : mail.getAttachedFiles()) {
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
                attachment.setFileName(file.getName());

                mailContent.addBodyPart(attachment);
            }

            // Send the complete message parts
            message.setContent(mailContent);

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
        }

    }
}
