package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    public void sendMail(final BasicMail mail) throws MessagingException, UnsupportedEncodingException {

        Session session = Session.getInstance(mail.getProperties(), new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return mail.getAuthenticator();
            }
        });

        System.out.println("Composing message");
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));
        for (String recipient : mail.getRecipients()) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        for (String bcc : mail.getBCCRecipients()) {
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

        System.out.println("Copying message to Send folder");
        // Copy message to "Sent Items" folder as read
        Store store = session.getStore("imap");
        store.connect(mail.getProperties().getProperty("mail.imap.host"), mail.getAuthenticator().getUserName(), mail.getAuthenticator().getPassword());
        Folder folder = store.getFolder("Sent");
        folder.open(Folder.READ_WRITE);
        message.setFlag(Flags.Flag.SEEN, true);
        folder.appendMessages(new Message[]{message});
        store.close();

        System.out.println("Message copied");

    }
}
