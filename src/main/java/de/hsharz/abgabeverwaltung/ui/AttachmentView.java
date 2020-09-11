package de.hsharz.abgabeverwaltung.ui;

import java.io.File;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.Task;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AttachmentView extends ListCell<File> {

    private HBox root;
    private Label lblPath;
    private Button btnDelete;

    private Task task;
    private File file;

    public AttachmentView(Task task) {
        this.task = Objects.requireNonNull(task);

        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    private void createWidgets() {
        this.root = new HBox(20);

        this.lblPath = new Label();
        this.lblPath.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);

        this.btnDelete = new JFXButton("Entfernen");
        btnDelete.setMinWidth(50);
    }

    private void setupInteractions() {
    btnDelete.setOnAction(e->task.removeAttachment(file));
    }

    private void addWidgets() {
        this.root.getChildren().addAll(this.lblPath, this.getHSpacer(), this.btnDelete);
        HBox.setHgrow(lblPath, Priority.NEVER);
        HBox.setHgrow(btnDelete, Priority.SOMETIMES);
    }

    private Region getHSpacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public Pane getPane() {
        return this.root;
    }

    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        this.file = item;

        if (item == null) {
            setGraphic(null);
            return;
        }

        prefWidthProperty().bind(getListView().widthProperty().subtract(2));
        setMaxWidth(Control.USE_PREF_SIZE);

        lblPath.setText(item.getAbsolutePath());
        setGraphic(root);

    }
}
