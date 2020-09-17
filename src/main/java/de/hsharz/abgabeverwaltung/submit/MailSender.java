package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    private BasicMail mail;

    private Session session;
    private MimeMessage assembledMessage;

    public MailSender(BasicMail mail) {
        this.mail = Objects.requireNonNull(mail);
    }

    public void sendMail() throws UnsupportedEncodingException, MessagingException {
        assembleMail();
        transportMail();
    }

    private void assembleMail() throws UnsupportedEncodingException, MessagingException {
        session = Session.getInstance(mail.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return mail.getAuthenticator();
            }
        });

        System.out.println("Assemble message");
        assembledMessage = new MimeMessage(session);

        // Set Sender and it's full name
        assembledMessage.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));

        // Add Recipients
        for (String recipient : mail.getRecipients()) {
            assembledMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        // Add BCC-Recipients
        for (String bcc : mail.getBCCRecipients()) {
            assembledMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }

        // Set Subject of this message
        assembledMessage.setSubject(mail.getSubject());

        // Multipart (mailContent) contains body and attachments
        Multipart mailContent = new MimeMultipart();

        // Set Body of this mail
        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(mail.getBody());
        mailContent.addBodyPart(messageBody);

        // Add attachments to mail
        for (File file : mail.getAttachedFiles()) {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
            attachment.setFileName(file.getName());
            mailContent.addBodyPart(attachment);
        }

        // Set body and attachments as content of this mail
        assembledMessage.setContent(mailContent);
    }

    private void transportMail() throws MessagingException {
        System.out.println("Sending message...");
        // Submit Mail
        Transport.send(assembledMessage);
        System.out.println("Sent message successfully....");
    }

    public void sendMailAndStoreInSentFolder() throws UnsupportedEncodingException, MessagingException {
        sendMail();
        storeMailInSentFolder();
    }

    private void storeMailInSentFolder() throws MessagingException {
        System.out.println("Copying message to Send folder");
        // Copy message to "Sent Items" folder as read
        Store store = session.getStore("imap");
        store.connect(mail.getProperties().getProperty("mail.imap.host"), mail.getAuthenticator().getUserName(), mail.getAuthenticator().getPassword());
        Folder folder = store.getFolder("Sent");
        folder.open(Folder.READ_WRITE);
        assembledMessage.setFlag(Flags.Flag.SEEN, true);
        folder.appendMessages(new Message[]{assembledMessage});
        store.close();
        System.out.println("Message copied");
    }

}
