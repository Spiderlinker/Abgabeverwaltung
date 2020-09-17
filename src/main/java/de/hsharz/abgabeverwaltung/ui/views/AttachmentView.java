package de.hsharz.abgabeverwaltung.ui.views;

import java.io.File;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AttachmentView extends AbstractView<HBox> {

    private Label lblPath;
    private Button btnDelete;

    private Task task;
    private File file;

    public AttachmentView(Task task) {
        super(new HBox());
        this.task = Objects.requireNonNull(task);

        initializeView();
    }

    @Override
    protected void createWidgets() {
        root.setSpacing(20);

        this.lblPath = new Label();
        this.lblPath.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);

        this.btnDelete = new JFXButton("", ImageLibrary.getImageView("trash.png"));
        btnDelete.setMinWidth(50);
    }

    @Override
    protected void setupInteractions() {
        btnDelete.setOnAction(e -> task.removeAttachment(file));
    }

    @Override
    protected void addWidgets() {
        this.root.getChildren().addAll(this.lblPath, LayoutUtils.getHSpacer(), this.btnDelete);
        HBox.setHgrow(lblPath, Priority.NEVER);
        HBox.setHgrow(btnDelete, Priority.SOMETIMES);
    }

    public ListCell<File> newListCell() {
        return new AttachmentListCell();
    }

    private class AttachmentListCell extends ListCell<File> {

        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);

            AttachmentView.this.file = item;

            if (item == null) {
                setGraphic(null);
                return;
            }

            prefWidthProperty().bind(getListView().widthProperty().subtract(20));
            setMaxWidth(Control.USE_PREF_SIZE);

            lblPath.setText(item.getAbsolutePath());
            setGraphic(root);

        }
    }
}
