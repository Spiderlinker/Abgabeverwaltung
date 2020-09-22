package de.hsharz.abgabeverwaltung.submit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.mail.PasswordAuthentication;

public class BasicMail {

    private String                 from;
    private String                 fromName;
    private List<String>           recipients    = new ArrayList<>();
    private List<String>           bccRecipients = new ArrayList<>();

    private String                 subject;
    private String                 body;

    private List<File>             attachedFiles = new ArrayList<>();
    private PasswordAuthentication authenticator;
    private Properties             properties    = new Properties();

    public BasicMail() {

    }

    public void setFrom(final String from, final String name) {
        this.from = from;
        this.fromName = name;
    }

    public String getFrom() {
        return this.from;
    }

    public String getFromName() {
        return this.fromName;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void addRecipient(final String... recipient) {
        Objects.requireNonNull(recipient);
        for (String r : recipient) {
            Objects.requireNonNull(r);
            this.recipients.add(r);
        }
    }

    public List<String> getRecipients() {
        return this.recipients;
    }

    public void addBCCRecipient(final String... bcc) {
        Objects.requireNonNull(bcc);
        for (String r : bcc) {
            Objects.requireNonNull(r);
            this.bccRecipients.add(r);
        }
    }

    public List<String> getBCCRecipients() {
        return this.bccRecipients;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void attachFile(final File file) {
        Objects.requireNonNull(file);
        this.attachedFiles.add(file);
    }

    public List<File> getAttachedFiles() {
        return this.attachedFiles;
    }

    public void setUsernamePassword(final String username, final String password) {
        this.setUsernamePassword(new PasswordAuthentication(username, password));
    }

    public void setUsernamePassword(final PasswordAuthentication authentication) {
        this.authenticator = authentication;
    }

    public PasswordAuthentication getAuthenticator() {
        return this.authenticator;
    }
}
