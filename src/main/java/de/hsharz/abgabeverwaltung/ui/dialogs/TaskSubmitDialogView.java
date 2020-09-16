package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.*;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.submit.BasicMail;
import de.hsharz.abgabeverwaltung.submit.MailSender;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TaskSubmitDialogView extends AbstractStyledView<GridPane> {

    private String submissionTitle = "%s - Abgabe %s";
    private String body = "Hallo %s %s, \n\nim Anhang finden Sie meine Abgabe für die Aufgabe '%s' in dem Modul '%s'. \n\n\nMit freundlichen Grüßen\n%s";

    private Label lblTitle;
    protected JFXTextField fldSubject;
    protected JFXTextField fldRecipient;
    protected JFXChipView<Person> recipients;
    protected JFXTextArea textBody;
    protected ListView<String> viewAttachments;

    protected Button btnSubmit;
    protected Button btnCancel;

    private Module module;
    private Task task;

    public TaskSubmitDialogView(Module module, Task task) {
        super(new GridPane());
        this.task = Objects.requireNonNull(task);
        this.module = Objects.requireNonNull(module);

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/TaskSubmitDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");
        root.setPrefSize(650, 720);

        LayoutUtils.setColumnWidths(root, 50, 50);

        lblTitle = new Label("Submit Task");
        lblTitle.getStyleClass().add("title");

        fldRecipient = new JFXTextField(module.getProfessor().getEmail());
        fldRecipient.setLabelFloat(true);
        fldRecipient.setPromptText("Recipient (Comma separated)");

        fldSubject = new JFXTextField(String.format(submissionTitle, module.getName(), task.getName()));
        if (task.getCustomSubmissionTitle() != null && !task.getCustomSubmissionTitle().trim().isEmpty()) {
            fldSubject.setText(task.getCustomSubmissionTitle());
        }
        fldSubject.setLabelFloat(true);
        fldSubject.setPromptText("Submission Title / Subject");

        String anrede = module.getProfessor().getGender().toString();
        if(Gender.DIVERS.equals(module.getProfessor().getGender())){
            anrede = ",";
        }
        textBody = new JFXTextArea(String.format(body, anrede, module.getProfessor().getLastname(), task.getName(), module.getName(), ""));
        textBody.setLabelFloat(true);
        textBody.setPromptText("Content");

        btnSubmit = new JFXButton("Submit Task", ImageLibrary.getImageView("mail_send_bold.png"));
        btnSubmit.setContentDisplay(ContentDisplay.RIGHT);
        btnSubmit.setGraphicTextGap(10);
        btnCancel = new JFXButton("Cancel");

        ObservableList<String> attachments = FXCollections.observableArrayList(task.getAttachments().stream().map(File::getAbsolutePath).collect(Collectors.toList()));
        this.viewAttachments = new ListView<>(attachments);
        viewAttachments.setPrefHeight(50);
        viewAttachments.setPlaceholder(new Label("No Files attached"));
    }

    @Override
    protected void setupInteractions() {
        btnSubmit.disableProperty().bind(module.nameProperty().isEmpty());
    }

    @Override
    protected void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldRecipient, 0, 1, 2, 1);
        root.add(fldSubject, 0, 2, 2, 1);
        root.add(textBody, 0, 3, 2, 1);
        root.add(viewAttachments, 0, 4, 2, 1);
        root.add(btnCancel, 0, 5);
        root.add(btnSubmit, 1, 5);

        GridPane.setVgrow(viewAttachments, Priority.SOMETIMES);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
        GridPane.setHalignment(btnSubmit, HPos.RIGHT);
    }

}
