package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.io.File;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import de.hsharz.abgabeverwaltung.ui.utils.UiUtils;
import de.hsharz.abgabeverwaltung.ui.views.AttachmentView;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TaskDialogView extends AbstractStyledView<GridPane> {

    private Label           lblTitle;
    protected JFXTextField  fldTitle;
    protected JFXTextField  fldCustomSubject;
    protected JFXDatePicker dueDate;

    protected JFXTextArea   textDescription;

    private ListView<File>  viewAttachments;

    protected Button        btnDelete;
    protected Button        btnSave;

    private Task            task;

    public TaskDialogView(final Task task) {
        super(new GridPane());
        this.task = Objects.requireNonNull(task);

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/DefaultDialog.css";
    }

    @Override
    protected void createWidgets() {
        this.root.getStyleClass().add("root");
        LayoutUtils.setColumnWidths(this.root, 70, 30);
        UiUtils.addFilesDropFeature(this.root, this.task::addAttachments);

        this.lblTitle = new Label("Edit Task");
        this.lblTitle.getStyleClass().add("title");

        this.fldTitle = new JFXTextField();
        this.fldTitle.textProperty().bindBidirectional(this.task.nameProperty());
        this.fldTitle.setPromptText("Name of this Task");
        this.fldTitle.setLabelFloat(true);

        this.fldCustomSubject = new JFXTextField();
        this.fldCustomSubject.textProperty().bindBidirectional(this.task.customSubmissionTitleProperty());
        this.fldCustomSubject.setPromptText("Custom Submission Title / Subject");
        this.fldCustomSubject.setLabelFloat(true);

        this.dueDate = new JFXDatePicker();
        this.dueDate.valueProperty().bindBidirectional(this.task.dueDateProperty());
        this.dueDate.setPromptText("Submission Date");
        if (this.task.getDueDate() != null) {
            this.dueDate.setValue(this.task.getDueDate());
        }

        this.textDescription = new JFXTextArea();
        this.textDescription.textProperty().bindBidirectional(this.task.descriptionProperty());
        this.textDescription.setPromptText("Description of this task...");
        this.textDescription.setLabelFloat(true);
        this.textDescription.setPrefHeight(150);

        this.viewAttachments = new ListView<>(this.task.getAttachments());
        this.viewAttachments.setCellFactory(param -> new AttachmentView(this.task).newListCell());
        this.viewAttachments.setPrefHeight(120);
        this.viewAttachments.setPlaceholder(new Label("Drag&Drop files here to attach to this task"));

        this.btnDelete = new JFXButton("Delete Task", ImageLibrary.getImageView("trash.png"));

        this.btnSave = new JFXButton("Save & Close");
        this.btnSave.disableProperty().bind(this.task.nameProperty().isEmpty());
        this.btnSave.getStyleClass().add("save-button");
        this.btnSave.setDefaultButton(true);
    }

    @Override
    protected void setupInteractions() {
        this.fldTitle.textProperty().addListener((observable, oldValue, newValue) -> this.fldTitle
                .pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), this.fldTitle.getText().trim().isEmpty()));
    }

    @Override
    protected void addWidgets() {
        this.root.add(this.lblTitle, 0, 0, 2, 1);

        this.root.add(this.fldTitle, 0, 1);
        this.root.add(this.dueDate, 1, 2);
        this.root.add(this.fldCustomSubject, 0, 2);

        this.root.add(this.textDescription, 0, 3, 2, 1);
        this.root.add(this.viewAttachments, 0, 4, 2, 1);

        this.root.add(this.btnDelete, 0, 5);
        this.root.add(this.btnSave, 1, 5);

        GridPane.setHalignment(this.lblTitle, HPos.CENTER);

        GridPane.setHgrow(this.fldTitle, Priority.ALWAYS);
        GridPane.setHalignment(this.dueDate, HPos.RIGHT);
        GridPane.setHgrow(this.fldCustomSubject, Priority.ALWAYS);

        GridPane.setHgrow(this.textDescription, Priority.ALWAYS);

        GridPane.setHalignment(this.btnSave, HPos.RIGHT);
    }

}
