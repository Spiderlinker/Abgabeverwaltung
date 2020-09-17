package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.Settings;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.submit.BasicMail;
import de.hsharz.abgabeverwaltung.submit.MailSender;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;

public class TaskSubmitDialog extends AbstractDialog {

    private StackPane root;
    private JFXProgressBar progressBar;
    private TaskSubmitDialogView taskSubmitDialogView;

    private Module module;
    private Task task;

    public TaskSubmitDialog(StackPane parent, Module module, Task task) {
        super(parent, JFXDialog.DialogTransition.TOP);
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        createWidgets();
        setupInteractions();
    }

    private void createWidgets() {
        root = new StackPane();
        progressBar = new JFXProgressBar(-1);
        progressBar.setVisible(false);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        taskSubmitDialogView = new TaskSubmitDialogView(module, task);
        root.getChildren().addAll(taskSubmitDialogView.getPane(), progressBar);

        StackPane.setAlignment(progressBar, Pos.BOTTOM_CENTER);

        setContent(root);
    }

    private void setupInteractions() {
        taskSubmitDialogView.btnSubmit.setOnAction(e -> {

            javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
                @Override
                public Void call() throws Exception {
                    Settings.reloadEmailServerProperties();
                    Settings.reloadEmailProperties();
                    BasicMail mail = composeMail();
                    new MailSender(mail).sendMailAndStoreInSentFolder();
                    storeMailInSentFolder();
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                progressBar.setVisible(false);
                setDisabled(false);
                close();
                this.task.setFinished(true);
            });

            task.setOnFailed(event -> {
                progressBar.setVisible(false);
                setDisabled(false);

                String message = "Failed to send your message!";

                if (task.getException() instanceof MessagingException) {
                    message = "Could not send message. Please check your username and password!";
                } else if (task.getException() instanceof UnsupportedEncodingException) {
                    message = "Invalid character on your Name. Please remove them!";
                }
                new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).showAndWait();
            });

            task.setOnRunning(event -> {
                setDisabled(true);
                progressBar.setVisible(true);
            });

            new Thread(task).start();
        });

        taskSubmitDialogView.btnCancel.setOnAction(e -> close());
    }

    private BasicMail composeMail() {
        BasicMail mail = new BasicMail();
        String[] recipients = taskSubmitDialogView.fldRecipient.getText().split(",");
        for (String recipient : recipients) {
            mail.addRecipient(recipient.trim());
        }
        mail.getAttachedFiles().addAll(task.getAttachments());
        mail.setSubject(taskSubmitDialogView.fldSubject.getText());
        mail.setBody(taskSubmitDialogView.textBody.getText());

        mail.setFrom(Settings.getEmailSettings().getProperty("mail.username"), Settings.getEmailSettings().getProperty("user.name"));

        mail.setUsernamePassword(Settings.getEmailSettings().getProperty("mail.username"), Settings.getEmailSettings().getProperty("mail.password"));
        if (Boolean.parseBoolean(Settings.getEmailSettings().getProperty("mail.bcc"))) {
            mail.addBCCRecipient(Settings.getEmailSettings().getProperty("mail.username"));
        }

        mail.getProperties().putAll(Settings.getEmailServerSettings());

        return mail;
    }

    private void storeMailInSentFolder() {

    }

}
