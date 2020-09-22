package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.util.Comparator;
import java.util.Objects;

import com.jfoenix.controls.JFXDialog;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import javafx.scene.layout.StackPane;

public class TaskDialog extends JFXDialog {

    private TaskDialogView taskDialogView;

    private Module         module;
    private Task           task;

    public TaskDialog(final StackPane parent, final Module module, final Task task) {
        super(parent, null, DialogTransition.CENTER);
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        this.createWidgets();
        this.setupInteractions();
    }

    private void createWidgets() {
        this.taskDialogView = new TaskDialogView(this.task);
        this.setContent(this.taskDialogView.getPane());
    }

    private void setupInteractions() {
        this.taskDialogView.btnDelete.setOnAction(e -> {
            this.module.removeTask(this.task);
            this.close();
        });
        this.taskDialogView.btnSave.setOnAction(e -> {
            this.close();
            this.module.getTasks().sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        });
        this.visibleProperty().addListener((observable, oldValue, newValue) -> {
            this.taskDialogView.fldTitle.requestFocus();
            this.taskDialogView.fldTitle.selectAll();
        });
    }

}
