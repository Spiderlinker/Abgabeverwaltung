package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    private static final Queue<String> possibleOperationErrors = new ArrayDeque<>();

    private BasicMail                  mail;

    private Session                    session;
    private MimeMessage                assembledMessage;

    public MailSender(final BasicMail mail) {
        this.mail = Objects.requireNonNull(mail);
    }

    public String getLastOperation() {
        return possibleOperationErrors.peek();
    }

    public void sendMail() throws UnsupportedEncodingException, MessagingException {
        possibleOperationErrors.addAll(Arrays.asList("Assemble mail: setFrom() with mail and full name", "Assemble mail: Add Recipients",
                "Assemble mail: Add BCC-Recipients", "Assemble mail: Set Subject", "Assemble mail: Set Body", "Assemble mail: Add Attachments"));
        this.assembleMail();
        possibleOperationErrors.addAll(Arrays.asList("Send mail: Transport mail to receiver"));
        this.transportMail();
    }

    private void assembleMail() throws UnsupportedEncodingException, MessagingException {
        this.session = Session.getInstance(this.mail.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return MailSender.this.mail.getAuthenticator();
            }
        });

        System.out.println("Assemble message");
        this.assembledMessage = new MimeMessage(this.session);

        // Set Sender and it's full name
        this.assembledMessage.setFrom(new InternetAddress(this.mail.getFrom(), this.mail.getFromName()));
        possibleOperationErrors.remove();

        // Add Recipients
        for (String recipient : this.mail.getRecipients()) {
            this.assembledMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        possibleOperationErrors.remove();

        // Add BCC-Recipients
        for (String bcc : this.mail.getBCCRecipients()) {
            this.assembledMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }
        possibleOperationErrors.remove();

        // Set Subject of this message
        this.assembledMessage.setSubject(this.mail.getSubject());
        possibleOperationErrors.remove();

        // Multipart (mailContent) contains body and attachments
        Multipart mailContent = new MimeMultipart();

        // Set Body of this mail
        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(this.mail.getBody());
        mailContent.addBodyPart(messageBody);
        possibleOperationErrors.remove();

        // Add attachments to mail
        for (File file : this.mail.getAttachedFiles()) {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
            attachment.setFileName(file.getName());
            mailContent.addBodyPart(attachment);
        }

        possibleOperationErrors.remove();

        // Set body and attachments as content of this mail
        this.assembledMessage.setContent(mailContent);
    }

    private void transportMail() throws MessagingException {
        System.out.println("Sending message...");
        // Submit Mail
        Transport.send(this.assembledMessage);
        possibleOperationErrors.remove();
        System.out.println("Sent message successfully....");
    }

    public void sendMailAndStoreInSentFolder() throws UnsupportedEncodingException, MessagingException {
        this.sendMail();
        possibleOperationErrors.addAll(Arrays.asList("Copy to Sent folder: Set Store (imap)", "Copy to Sent folder: Connect to server",
                "Copy to Sent folder: Get Folder 'Sent'", "Copy to Sent folder: Open Folder with READ_WRITE", "Copy to Sent folder: Mark as read",
                "Copy to Sent folder: Put message", "Copy to Sent folder: Close Store"));
        this.storeMailInSentFolder();
    }

    private void storeMailInSentFolder() throws MessagingException {
        System.out.println("Copying message to Send folder");
        // Copy message to "Sent Items" folder as read
        Store store = this.session.getStore("imap");
        possibleOperationErrors.remove();

        store.connect(this.mail.getProperties().getProperty("mail.imap.host"), this.mail.getAuthenticator().getUserName(),
                this.mail.getAuthenticator().getPassword());
        possibleOperationErrors.remove();

        Folder folder = store.getFolder("Sent");
        possibleOperationErrors.remove();

        folder.open(Folder.READ_WRITE);
        possibleOperationErrors.remove();

        this.assembledMessage.setFlag(Flags.Flag.SEEN, true);
        possibleOperationErrors.remove();

        folder.appendMessages(new Message[] { this.assembledMessage });
        possibleOperationErrors.remove();

        store.close();
        possibleOperationErrors.remove();
        System.out.println("Message copied");
    }

}
