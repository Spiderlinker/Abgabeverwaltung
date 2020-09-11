package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.*;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.Module;
import de.hsharz.abgabeverwaltung.ModuleDatabase;
import de.hsharz.abgabeverwaltung.Task;
import de.hsharz.abgabeverwaltung.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.addresses.Person;
import de.hsharz.abgabeverwaltung.submit.BasicMail;
import de.hsharz.abgabeverwaltung.submit.MailSender;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TaskSubmitView {

    private String submissionTitle = "%s - Abgabe %s";
    private String body = "Hallo Herr %s, \n\nim Anhang finden Sie meine Abgabe für die Aufgabe '%s' in dem Modul '%s'. \n\n\nMit freundlichen Grüßen\n%s";

    private JFXDialog dialog;
    private StackPane parent;

    private GridPane root;

    private Label lblTitle;
    private JFXTextField fldSubject;
    private JFXTextField fldRecipient;
    private JFXChipView<Person> recipients;
    private JFXTextArea textBody;
    private ListView<String> viewAttachments;

    private Button btnSubmit;
    private Button btnCancel;

    private Module module;
    private Task task;

    public TaskSubmitView(StackPane parent, Module module, Task task) {
        this.parent = parent;
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        createWidgets();
        setupInteractions();
        addWidgets();
    }

    private void createWidgets() {
        root = new GridPane();
        root.getStylesheets().add(this.getClass().getResource("/style/dialog/TaskSubmitDialog.css").toExternalForm());
        root.getStyleClass().add("root");
        root.setPrefSize(600, 600);

        setColumnWidths(root, 50, 50);

        lblTitle = new Label("Submit Task");
        lblTitle.getStyleClass().add("title");

        fldRecipient = new JFXTextField(module.getProfessors().get(0).getEmail());
        fldRecipient.setLabelFloat(true);
        fldRecipient.setPromptText("Recipient");

        fldSubject = new JFXTextField(String.format(submissionTitle, module.getName(), task.getName()));
        if (task.getCustomSubmissionTitle() != null && !task.getCustomSubmissionTitle().trim().isEmpty()) {
            fldSubject.setText(task.getCustomSubmissionTitle());
        }
        fldSubject.setLabelFloat(true);
        fldSubject.setPromptText("Submission Title / Subject");

        textBody = new JFXTextArea(String.format(body, module.getProfessors().get(0).getLastname(), task.getName(), module.getName(), "Oliver Lindemann"));
        textBody.setLabelFloat(true);
        textBody.setPromptText("Content");

        btnSubmit = new JFXButton("Submit Task");
        btnCancel = new JFXButton("Cancel");

        ObservableList<String> attachments = FXCollections.observableArrayList(task.getAttachments().stream().map(File::getAbsolutePath).collect(Collectors.toList()));
        this.viewAttachments = new ListView<>(attachments);
        viewAttachments.setPrefHeight(50);
        viewAttachments.setPlaceholder(new Label("No Files attached"));

        dialog = new JFXDialog(parent, root, JFXDialog.DialogTransition.TOP);

    }

    private void setColumnWidths(GridPane root, int... columnWidths) {

        for (int columnWidth : columnWidths) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(columnWidth);
            root.getColumnConstraints().add(c);
        }

    }

    private void setupInteractions() {
        btnSubmit.disableProperty().bind(module.nameProperty().isEmpty());

        btnSubmit.setOnAction(e -> {

            BasicMail mail = new BasicMail();
            String[] recipients = fldRecipient.getText().split(",");
            for (String recipient : recipients) {
                mail.addRecipient(recipient.trim());
            }
            mail.getAttachedFiles().addAll(task.getAttachments());
            mail.setSubject(fldSubject.getText());
            mail.setBody(textBody.getText());
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
            if(Boolean.parseBoolean(userProperties.getProperty("mail.bcc"))){
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
            JFXDialog dia = new JFXDialog(parent, prog, JFXDialog.DialogTransition.CENTER);
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

        btnCancel.setOnAction(e -> dialog.close());
    }

    private void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldRecipient, 0, 1, 2, 1);
        root.add(fldSubject, 0, 2, 2, 1);
        root.add(textBody, 0, 3, 2, 1);
        root.add(viewAttachments, 0, 4, 2, 1);
        root.add(btnCancel, 0, 5);
        root.add(btnSubmit, 1, 5);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
        GridPane.setHalignment(btnSubmit, HPos.RIGHT);
    }


    public void show() {
        dialog.show();
    }
}
