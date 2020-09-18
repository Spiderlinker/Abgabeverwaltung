package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    private static final Queue<String> possibleOperationErrors = new ArrayDeque<>();

    private BasicMail mail;

    private Session session;
    private MimeMessage assembledMessage;

    public MailSender(BasicMail mail) {
        this.mail = Objects.requireNonNull(mail);
    }

    public String getLastOperation() {
        return possibleOperationErrors.peek();
    }

    public void sendMail() throws UnsupportedEncodingException, MessagingException {
        possibleOperationErrors.addAll(Arrays.asList(
                "Assemble mail: setFrom() with mail and full name",
                "Assemble mail: Add Recipients",
                "Assemble mail: Add BCC-Recipients",
                "Assemble mail: Set Subject",
                "Assemble mail: Set Body",
                "Assemble mail: Add Attachments"));
        assembleMail();
        possibleOperationErrors.addAll(Arrays.asList(
                "Send mail: Transport mail to receiver"));
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
        possibleOperationErrors.remove();

        // Add Recipients
        for (String recipient : mail.getRecipients()) {
            assembledMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        possibleOperationErrors.remove();

        // Add BCC-Recipients
        for (String bcc : mail.getBCCRecipients()) {
            assembledMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }
        possibleOperationErrors.remove();

        // Set Subject of this message
        assembledMessage.setSubject(mail.getSubject());
        possibleOperationErrors.remove();

        // Multipart (mailContent) contains body and attachments
        Multipart mailContent = new MimeMultipart();

        // Set Body of this mail
        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(mail.getBody());
        mailContent.addBodyPart(messageBody);
        possibleOperationErrors.remove();

        // Add attachments to mail
        for (File file : mail.getAttachedFiles()) {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
            attachment.setFileName(file.getName());
            mailContent.addBodyPart(attachment);
        }

        possibleOperationErrors.remove();

        // Set body and attachments as content of this mail
        assembledMessage.setContent(mailContent);
    }

    private void transportMail() throws MessagingException {
        System.out.println("Sending message...");
        // Submit Mail
        Transport.send(assembledMessage);
        possibleOperationErrors.remove();
        System.out.println("Sent message successfully....");
    }

    public void sendMailAndStoreInSentFolder() throws UnsupportedEncodingException, MessagingException {
        sendMail();
        possibleOperationErrors.addAll(Arrays.asList(
                "Copy to Sent folder: Set Store (imap)",
                "Copy to Sent folder: Connect to server",
                "Copy to Sent folder: Get Folder 'Sent'",
                "Copy to Sent folder: Open Folder with READ_WRITE",
                "Copy to Sent folder: Mark as read",
                "Copy to Sent folder: Put message",
                "Copy to Sent folder: Close Store"));
        storeMailInSentFolder();
    }

    private void storeMailInSentFolder() throws MessagingException {
        System.out.println("Copying message to Send folder");
        // Copy message to "Sent Items" folder as read
        Store store = session.getStore("imap");
        possibleOperationErrors.remove();

        store.connect(mail.getProperties().getProperty("mail.imap.host"), mail.getAuthenticator().getUserName(), mail.getAuthenticator().getPassword());
        possibleOperationErrors.remove();

        Folder folder = store.getFolder("Sent");
        possibleOperationErrors.remove();

        folder.open(Folder.READ_WRITE);
        possibleOperationErrors.remove();

        assembledMessage.setFlag(Flags.Flag.SEEN, true);
        possibleOperationErrors.remove();

        folder.appendMessages(new Message[]{assembledMessage});
        possibleOperationErrors.remove();

        store.close();
        possibleOperationErrors.remove();
        System.out.println("Message copied");
    }

}
