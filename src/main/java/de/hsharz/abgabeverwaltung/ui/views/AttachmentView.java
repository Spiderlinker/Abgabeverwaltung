package de.hsharz.abgabeverwaltung.ui.views;

import java.io.File;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AttachmentView extends AbstractView<HBox> {

    private Label  lblPath;
    private Button btnDelete;

    private Task   task;
    private File   file;

    public AttachmentView(final Task task) {
        super(new HBox());
        this.task = Objects.requireNonNull(task);

        this.initializeView();
    }

    @Override
    protected void createWidgets() {
        this.root.setSpacing(20);

        this.lblPath = new Label();
        this.lblPath.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);

        this.btnDelete = new JFXButton("", ImageLibrary.getImageView("trash.png"));
        this.btnDelete.setMinWidth(50);
    }

    @Override
    protected void setupInteractions() {
        this.btnDelete.setOnAction(e -> this.task.removeAttachment(this.file));
    }

    @Override
    protected void addWidgets() {
        this.root.getChildren().addAll(this.lblPath, LayoutUtils.getHSpacer(), this.btnDelete);
        HBox.setHgrow(this.lblPath, Priority.NEVER);
        HBox.setHgrow(this.btnDelete, Priority.SOMETIMES);
    }

    public ListCell<File> newListCell() {
        return new AttachmentListCell();
    }

    private class AttachmentListCell extends ListCell<File> {

        @Override
        protected void updateItem(final File item, final boolean empty) {
            super.updateItem(item, empty);

            AttachmentView.this.file = item;

            if (item == null) {
                this.setGraphic(null);
                return;
            }

            this.prefWidthProperty().bind(this.getListView().widthProperty().subtract(20));
            this.setMaxWidth(Region.USE_PREF_SIZE);

            AttachmentView.this.lblPath.setText(item.getAbsolutePath());
            this.setGraphic(AttachmentView.this.root);

        }
    }
}
