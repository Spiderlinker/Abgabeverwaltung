package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.views.AttachmentView;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.File;
import java.util.Objects;

public class TaskDialogView extends AbstractStyledView<GridPane> {

    protected TextField fldTitle;
    protected TextField fldCustomSubject;
    protected DatePicker dueDate;

    protected TextArea textDescription;

    private ListView<File> viewAttachments;

    protected Button btnDelete;
    protected Button btnSave;

    private Task task;

    public TaskDialogView(final Task task) {
        super(new GridPane());
        this.task = Objects.requireNonNull(task);

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/ModuleDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");

        LayoutUtils.setColumnWidths(root, 70, 30);

        this.fldTitle = new JFXTextField();
        fldTitle.textProperty().bindBidirectional(task.nameProperty());
        this.fldTitle.setPromptText("Name der Aufgabe");

        this.fldCustomSubject = new JFXTextField();
        fldCustomSubject.textProperty().bindBidirectional(task.customSubmissionTitleProperty());
        this.fldCustomSubject.setPromptText("Einreichungstitel / Betreff");

        this.dueDate = new JFXDatePicker();
        dueDate.valueProperty().bindBidirectional(task.dueDateProperty());
        this.dueDate.setPromptText("Abgabedatum");
        if (this.task.getDueDate() != null) {
            this.dueDate.setValue(this.task.getDueDate());
        }

        this.textDescription = new JFXTextArea();
        textDescription.textProperty().bindBidirectional(task.descriptionProperty());
        this.textDescription.setPromptText("Beschreibung der Aufgabe...");

        this.viewAttachments = new ListView<>(task.getAttachments());
        this.viewAttachments.setCellFactory(param -> new AttachmentView(task).newListCell());
        viewAttachments.setPrefHeight(50);
        viewAttachments.setPlaceholder(new Label("Drag&Drop files here to attach to this task"));

        this.btnDelete = new JFXButton("Aufgabe löschen");

        this.btnSave = new JFXButton("Speichern");
        btnSave.disableProperty().bind(task.nameProperty().isEmpty());
        btnSave.setDefaultButton(true);
    }

    @Override
    protected void setupInteractions() {

        this.fldTitle.textProperty()
                .addListener((observable, oldValue, newValue) -> this.fldTitle.pseudoClassStateChanged(
                        PseudoClass.getPseudoClass("error"), this.fldTitle.getText().trim().isEmpty()));


        this.root.setOnDragOver(event -> {
            if (event.getGestureSource() != this.root && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        this.root.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                db.getFiles().stream().filter(File::isFile).forEach(task::addAttachments);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    @Override
    protected void addWidgets() {
        this.root.add(this.fldTitle, 0, 0);
        this.root.add(this.dueDate, 1, 0);
        this.root.add(this.fldCustomSubject, 0, 1);

        this.root.add(this.textDescription, 0, 2, 2, 1);
        this.root.add(this.viewAttachments, 0, 3, 2, 1);

        this.root.add(this.btnDelete, 0, 4);
        this.root.add(this.btnSave, 1, 4);

        GridPane.setHgrow(this.fldTitle, Priority.ALWAYS);
        GridPane.setHalignment(this.dueDate, HPos.RIGHT);
        GridPane.setHgrow(this.fldCustomSubject, Priority.ALWAYS);

        GridPane.setHgrow(this.textDescription, Priority.ALWAYS);

        GridPane.setHalignment(this.btnSave, HPos.RIGHT);
    }


}
