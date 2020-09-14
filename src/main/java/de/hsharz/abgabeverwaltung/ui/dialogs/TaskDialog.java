package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import javafx.scene.layout.StackPane;

import java.util.Comparator;
import java.util.Objects;

public class TaskDialog extends JFXDialog {

    private TaskDialogView taskDialogView;

    private Module module;
    private Task task;

    public TaskDialog(StackPane parent, Module module, final Task task) {
        super(parent, null, DialogTransition.CENTER);
        this.module = Objects.requireNonNull(module);
        this.task = Objects.requireNonNull(task);

        createWidgets();
        setupInteractions();
    }

    private void createWidgets() {
        taskDialogView = new TaskDialogView(task);
        setContent(taskDialogView.getPane());
    }

    private void setupInteractions() {
        taskDialogView.btnDelete.setOnAction(e -> {
            module.removeTask(task);
            close();
        });
        taskDialogView.btnSave.setOnAction(e -> {
            close();
            module.getTasks().sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        });
        visibleProperty().addListener((observable, oldValue, newValue) -> {
            taskDialogView.fldTitle.requestFocus();
            taskDialogView.fldTitle.selectAll();
        });
    }

}
