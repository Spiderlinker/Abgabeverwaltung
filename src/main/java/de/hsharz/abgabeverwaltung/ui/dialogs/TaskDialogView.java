package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
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

    private Label lblTitle;
    protected JFXTextField fldTitle;
    protected JFXTextField fldCustomSubject;
    protected JFXDatePicker dueDate;

    protected JFXTextArea textDescription;

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
        return "/style/dialog/DefaultDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");

        LayoutUtils.setColumnWidths(root, 70, 30);

        lblTitle = new Label("Edit Task");
        lblTitle.getStyleClass().add("title");

        this.fldTitle = new JFXTextField();
        fldTitle.textProperty().bindBidirectional(task.nameProperty());
        this.fldTitle.setPromptText("Name of this Task");
        fldTitle.setLabelFloat(true);

        this.fldCustomSubject = new JFXTextField();
        fldCustomSubject.textProperty().bindBidirectional(task.customSubmissionTitleProperty());
        this.fldCustomSubject.setPromptText("Custom Submission Title / Subject");
        fldCustomSubject.setLabelFloat(true);

        this.dueDate = new JFXDatePicker();
        dueDate.valueProperty().bindBidirectional(task.dueDateProperty());
        this.dueDate.setPromptText("Submission Date");
        if (this.task.getDueDate() != null) {
            this.dueDate.setValue(this.task.getDueDate());
        }

        this.textDescription = new JFXTextArea();
        textDescription.textProperty().bindBidirectional(task.descriptionProperty());
        this.textDescription.setPromptText("Description of this task...");
        textDescription.setLabelFloat(true);
        textDescription.setPrefHeight(150);

        this.viewAttachments = new ListView<>(task.getAttachments());
        this.viewAttachments.setCellFactory(param -> new AttachmentView(task).newListCell());
        viewAttachments.setPrefHeight(120);
        viewAttachments.setPlaceholder(new Label("Drag&Drop files here to attach to this task"));

        this.btnDelete = new JFXButton("Delete Task", ImageLibrary.getImageView("trash.png"));

        this.btnSave = new JFXButton("Save & Close");
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
