package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.util.Objects;
import java.util.Optional;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;

import de.hsharz.abgabeverwaltung.Settings;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.submit.BasicMail;
import de.hsharz.abgabeverwaltung.submit.MailSender;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

public class TaskSubmitDialog extends AbstractDialog {

    private StackPane            root;
    private JFXProgressBar       progressBar;
    private TaskSubmitDialogView taskSubmitDialogView;

    private Module               module;
    private Task                 task;

    public TaskSubmitDialog(final StackPane parent, final Module module, final Task task) {
        super(parent, JFXDialog.DialogTransition.TOP);
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        this.createWidgets();
        this.setupInteractions();
    }

    private void createWidgets() {
        this.root = new StackPane();
        this.progressBar = new JFXProgressBar(-1);
        this.progressBar.setVisible(false);
        this.progressBar.setMaxWidth(Double.MAX_VALUE);

        this.taskSubmitDialogView = new TaskSubmitDialogView(this.module, this.task);
        this.root.getChildren().addAll(this.taskSubmitDialogView.getPane(), this.progressBar);

        StackPane.setAlignment(this.progressBar, Pos.BOTTOM_CENTER);

        this.setContent(this.root);
    }

    private void setupInteractions() {
        this.taskSubmitDialogView.btnSubmit.setOnAction(e -> {

            boolean sendMail = this.getConfirmationOfUserToSendMail();
            if (!sendMail) {
                return;
            }

            System.out.println("Send mail");

            final MailSender[] mailSender = new MailSender[1];

            javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
                @Override
                public Void call() throws Exception {
                    Settings.reloadEmailServerProperties();
                    Settings.reloadEmailProperties();
                    BasicMail mail = TaskSubmitDialog.this.composeMail();
                    mailSender[0] = new MailSender(mail);
                    mailSender[0].sendMailAndStoreInSentFolder();
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                this.progressBar.setVisible(false);
                this.setDisabled(false);
                this.close();
                this.task.setFinished(true);
            });

            task.setOnFailed(event -> {
                this.progressBar.setVisible(false);
                this.setDisabled(false);

                String message = task.getException().getMessage();

                //                if (task.getException() instanceof MessagingException) {
                //                    message = "Could not send message. Please check your username and password!";
                //                } else if (task.getException() instanceof UnsupportedEncodingException) {
                //                    message = "Invalid character on your Name. Please remove them!";
                //                }
                new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).showAndWait();
            });

            task.setOnRunning(event -> {
                this.setDisabled(true);
                this.progressBar.setVisible(true);
            });

            new Thread(task).start();
        });

        this.taskSubmitDialogView.btnCancel.setOnAction(e -> this.close());
    }

    private boolean getConfirmationOfUserToSendMail() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Submit Task?");
        alert.setContentText("Do you really want to submit this task to " + this.module.getProfessor().getEmail());
        alert.getButtonTypes().clear();
        ButtonType submitButton = new ButtonType("Yes, Submit");
        ButtonType cancelButton = new ButtonType("No");
        alert.getButtonTypes().addAll(cancelButton, submitButton);
        Optional<ButtonType> pressedButton = alert.showAndWait();
        // If there is no pressed Button or the Button was not the submit button, return false
        // Only return true if there was a button pressed and this button was the custom submitButton
        return pressedButton.isPresent() && pressedButton.get().equals(submitButton);
    }

    private BasicMail composeMail() {
        BasicMail mail = new BasicMail();
        String[] recipients = this.taskSubmitDialogView.fldRecipient.getText().split(",");
        for (String recipient : recipients) {
            mail.addRecipient(recipient.trim());
        }
        mail.getAttachedFiles().addAll(this.task.getAttachments());
        mail.setSubject(this.taskSubmitDialogView.fldSubject.getText());
        mail.setBody(this.taskSubmitDialogView.textBody.getText());

        mail.setFrom(Settings.getEmailSettings().getProperty("mail.username"), Settings.getEmailSettings().getProperty("user.name"));

        mail.setUsernamePassword(Settings.getEmailSettings().getProperty("mail.username"), Settings.getEmailSettings().getProperty("mail.password"));
        if (Boolean.parseBoolean(Settings.getEmailSettings().getProperty("mail.bcc"))) {
            mail.addBCCRecipient(Settings.getEmailSettings().getProperty("mail.username"));
        }

        mail.getProperties().putAll(Settings.getEmailServerSettings());

        return mail;
    }

}
