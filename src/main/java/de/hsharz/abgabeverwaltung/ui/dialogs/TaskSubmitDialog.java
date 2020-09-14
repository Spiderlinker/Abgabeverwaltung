package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.submit.BasicMail;
import de.hsharz.abgabeverwaltung.submit.MailSender;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class TaskSubmitDialog extends JFXDialog {

    private TaskSubmitDialogView taskSubmitDialogView;

    private Module module;
    private Task task;

    public TaskSubmitDialog(StackPane parent, Module module, Task task) {
        super(parent, null, JFXDialog.DialogTransition.TOP);
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        createWidgets();
        setupInteractions();
    }

    private void createWidgets() {
        taskSubmitDialogView = new TaskSubmitDialogView(module, task);
        setContent(taskSubmitDialogView.getPane());
    }

    private void setupInteractions() {
        taskSubmitDialogView.btnSubmit.setOnAction(e -> {

            BasicMail mail = new BasicMail();
            String[] recipients = taskSubmitDialogView.fldRecipient.getText().split(",");
            for (String recipient : recipients) {
                mail.addRecipient(recipient.trim());
            }
            mail.getAttachedFiles().addAll(task.getAttachments());
            mail.setSubject(taskSubmitDialogView.fldSubject.getText());
            mail.setBody(taskSubmitDialogView.textBody.getText());
            Properties userProperties = new Properties();

            try (FileInputStream input = new FileInputStream(Config.EMAIL_CONFIGURATION_FILE)) {
                userProperties.load(input);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            mail.setFrom(userProperties.getProperty("mail.username"), userProperties.getProperty("user.name"));

            mail.setUsernamePassword(userProperties.getProperty("mail.username"), userProperties.getProperty("mail.password"));
            if (Boolean.parseBoolean(userProperties.getProperty("mail.bcc"))) {
                mail.addBCCRecipient(userProperties.getProperty("mail.username"));
            }

            try (FileInputStream input = new FileInputStream(Config.EMAIL_SERVER_CONFIGURATION_FILE)) {
                mail.getProperties().load(input);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Thread thread = new Thread(() -> new MailSender().sendMail(mail));

            ProgressIndicator prog = new ProgressIndicator(-1);
            JFXDialog dia = new JFXDialog(getDialogContainer(), prog, JFXDialog.DialogTransition.CENTER);
            dia.show();

            thread.start();
            try {
                thread.join();
                task.setFinished(true);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                dia.close();
            }


        });

        taskSubmitDialogView.btnCancel.setOnAction(e -> close());
    }

}
