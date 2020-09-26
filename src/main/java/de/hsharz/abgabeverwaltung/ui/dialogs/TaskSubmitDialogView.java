package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.Language;
import de.hsharz.abgabeverwaltung.Settings;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TaskSubmitDialogView extends AbstractStyledView<GridPane> {

    private Map<String, String> stringReplacer  = new HashMap<>();

    private String              submissionTitle = "%s - Abgabe %s";

    private Label               lblTitle;
    protected JFXTextField      fldSubject;
    protected JFXTextField      fldRecipient;
    protected JFXTextArea       textBody;
    protected ListView<String>  viewAttachments;

    protected Button            btnSubmit;
    protected Button            btnCancel;

    private Module              module;
    private Task                task;

    public TaskSubmitDialogView(final Module module, final Task task) {
        super(new GridPane());
        this.task = Objects.requireNonNull(task);
        this.module = Objects.requireNonNull(module);
        this.initializeStringReplacer();

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/DefaultDialog.css";
    }

    private void initializeStringReplacer() {
        String formOfAddress;
        if (Gender.DIVERS.equals(this.module.getProfessor().getGender())) {
            formOfAddress = ",";
        } else {
            formOfAddress = " " + this.module.getProfessor().getGender().toString();
        }

        this.stringReplacer.put("[FORM_OF_ADDRESS]", formOfAddress);
        this.stringReplacer.put("[PROF_NAME]", this.module.getProfessor().getLastname());
        this.stringReplacer.put("[TASK_NAME]", this.task.getName());
        this.stringReplacer.put("[MODULE_NAME]", this.module.getName());
        this.stringReplacer.put("[USER_FULL_NAME]", Settings.getEmailSettings().getProperty("mail.from"));
    }

    @Override
    protected void createWidgets() {
        this.root.getStyleClass().add("root");
        this.root.setPrefSize(650, 780);

        LayoutUtils.setColumnWidths(this.root, 50, 50);

        this.lblTitle = new Label(Language.getString("SubmitTask"));
        this.lblTitle.getStyleClass().add("title");

        this.fldRecipient = new JFXTextField(this.module.getProfessor().getEmail());
        this.fldRecipient.setLabelFloat(true);
        this.fldRecipient.setPromptText(Language.getString("RecipientsSeparated"));

        this.fldSubject = new JFXTextField(String.format(this.submissionTitle, this.module.getName(), this.task.getName()));
        if (this.task.getCustomSubmissionTitle() != null && !this.task.getCustomSubmissionTitle().trim().isEmpty()) {
            this.fldSubject.setText(this.task.getCustomSubmissionTitle());
        }
        this.fldSubject.setLabelFloat(true);
        this.fldSubject.setPromptText(Language.getString("SubmissionTitle"));

        String body = "[BODY]";
        try {
            body = Files.readAllLines(Config.EMAIL_TEMPLATE_FILE.toPath()).stream().map(this::applyStringReplacer).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textBody = new JFXTextArea(body);
        this.textBody.setLabelFloat(true);
        this.textBody.setPromptText(Language.getString("Content"));

        this.btnSubmit = new JFXButton(Language.getString("SubmitTask"), ImageLibrary.getImageView("mail_send.png"));
        this.btnSubmit.setContentDisplay(ContentDisplay.RIGHT);
        this.btnSubmit.setGraphicTextGap(10);
        this.btnCancel = new JFXButton(Language.getString("Cancel"));

        ObservableList<String> attachments = FXCollections
                .observableArrayList(this.task.getAttachments().stream().map(File::getAbsolutePath).collect(Collectors.toList()));
        this.viewAttachments = new ListView<>(attachments);
        this.viewAttachments.setPrefHeight(150);
        this.viewAttachments.setPlaceholder(new Label(Language.getString("NoFilesAttached"), ImageLibrary.getImageView("warning.png")));
        this.viewAttachments.setStyle("-fx-background-color: -fx-orange");
    }

    private String applyStringReplacer(final String s) {
        String formattedString = s;
        for (Entry<String, String> e : this.stringReplacer.entrySet()) {
            formattedString = formattedString.replace(e.getKey(), e.getValue());
        }
        return formattedString;
    }

    @Override
    protected void setupInteractions() {
        this.btnSubmit.disableProperty().bind(this.module.nameProperty().isEmpty());
    }

    @Override
    protected void addWidgets() {
        this.root.add(this.lblTitle, 0, 0, 2, 1);
        this.root.add(this.fldRecipient, 0, 1, 2, 1);
        this.root.add(this.fldSubject, 0, 2, 2, 1);
        this.root.add(this.textBody, 0, 3, 2, 1);
        this.root.add(this.viewAttachments, 0, 4, 2, 1);
        this.root.add(this.btnCancel, 0, 5);
        this.root.add(this.btnSubmit, 1, 5);

        GridPane.setVgrow(this.viewAttachments, Priority.SOMETIMES);

        GridPane.setHalignment(this.lblTitle, HPos.CENTER);
        GridPane.setHalignment(this.btnSubmit, HPos.RIGHT);
    }

}
