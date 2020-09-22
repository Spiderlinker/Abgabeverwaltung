package de.hsharz.abgabeverwaltung.ui.views;

import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskSubmitDialog;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import de.hsharz.abgabeverwaltung.ui.utils.UiUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskView extends AbstractStyledView<StackPane> {

    public static final PseudoClass finished     = PseudoClass.getPseudoClass("finished");
    public static final PseudoClass muchTimeLeft = PseudoClass.getPseudoClass("much-time-left");
    public static final PseudoClass fewTimeLeft  = PseudoClass.getPseudoClass("few-time-left");
    public static final PseudoClass noTimeLeft   = PseudoClass.getPseudoClass("no-time-left");

    private StackPane               parent;

    private BorderPane              dragPane;

    private VBox                    taskBox;
    private Label                   lblTitle;
    private Label                   lblDueDate;
    private Label                   lblAttachments;

    private HBox                    boxButton;

    private Button                  btnSubmit;
    private Button                  btnDelete;
    private Button                  btnRedo;

    private ObjectProperty<Module>  module;

    private Task                    task;

    public TaskView(final StackPane parent, final ObjectProperty<Module> module) {
        super(new StackPane());
        this.parent = parent;
        this.module = module;

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/Task.css";
    }

    @Override
    protected void createWidgets() {
        this.root.setPrefSize(250, 200);

        this.taskBox = new VBox(10);
        this.taskBox.getStyleClass().add("taskBox");

        this.lblTitle = new Label();
        this.lblTitle.getStyleClass().add("title");

        Tooltip tooltipTitle = new Tooltip();
        tooltipTitle.textProperty().bind(this.lblTitle.textProperty());
        this.lblTitle.setTooltip(tooltipTitle);

        this.lblDueDate = new Label("", ImageLibrary.getImageView("calendar.png"));

        this.lblAttachments = new Label("", ImageLibrary.getImageView("attachments.png"));

        this.boxButton = new HBox(10);
        this.btnSubmit = new JFXButton("Review & Submit", ImageLibrary.getImageView("check_next.png"));
        this.btnRedo = new JFXButton("Re-Open", ImageLibrary.getImageView("repeat.png"));
        this.btnDelete = new JFXButton("", ImageLibrary.getImageView("trash.png"));

        this.dragPane = new BorderPane();
        this.dragPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        Label label = new Label("Drop here to add file to task");
        label.setStyle("-fx-font-size: 14pt;");
        this.dragPane.setCenter(label);
        UiUtils.addFilesDropFeature(this.dragPane, this.task::addAttachments);

        this.dragPane.setVisible(false);
    }

    @Override
    protected void setupInteractions() {

        this.root.setOnDragEntered(event -> this.dragPane.setVisible(true));
        this.root.setOnDragExited(event -> this.dragPane.setVisible(false));

        this.root.setCursor(Cursor.HAND);

        this.root.setOnMouseClicked(e -> new TaskDialog(this.parent, this.module.get(), this.task).show());

        this.btnSubmit.setOnAction(e -> new TaskSubmitDialog(this.parent, this.module.get(), this.task).show());
        this.btnRedo.setOnAction(e -> this.task.setFinished(false));
        this.btnDelete.setOnAction(e -> this.module.get().removeTask(this.task));

    }

    @Override
    protected void addWidgets() {
        this.taskBox.getChildren().add(this.lblTitle);
        this.taskBox.getChildren().add(LayoutUtils.getVSpacer());
        this.taskBox.getChildren().add(this.lblDueDate);
        this.taskBox.getChildren().add(this.lblAttachments);
        this.taskBox.getChildren().add(LayoutUtils.getVSpacer());

        this.boxButton.getChildren().addAll(this.btnSubmit);
        this.boxButton.setAlignment(Pos.CENTER_RIGHT);
        this.taskBox.getChildren().add(this.boxButton);

        this.root.getChildren().addAll(this.taskBox, this.dragPane);
    }

    public Button getSubmitButton() {
        return this.btnSubmit;
    }

    public ListCell<Task> newListCell() {
        return new TaskListCell();
    }

    private class TaskListCell extends ListCell<Task> implements ChangeListener<Boolean> {

        @Override
        public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
            this.updateItem(TaskView.this.task, TaskView.this.task == null);
            System.out.println("Update called " + TaskView.this.task.getName());
        }

        @Override
        protected void updateItem(final Task item, final boolean empty) {
            super.updateItem(item, empty);

            if (item == null) {
                this.setGraphic(null);
                return;
            }

            if (TaskView.this.task != null) {
                TaskView.this.task.isFinishedProperty().removeListener(this);
            }

            TaskView.this.task = item;
            TaskView.this.task.isFinishedProperty().addListener(this);

            this.resetTaskViewToDefault();
            this.setupButtonsAndColoring();
            this.setupTextComponents();

            this.setGraphic(TaskView.this.root);
        }

        private void resetTaskViewToDefault() {
            this.resetPseudoClasses();
            TaskView.this.lblTitle.setGraphic(null);
            TaskView.this.boxButton.getChildren().clear();
        }

        private void resetPseudoClasses() {
            TaskView.this.taskBox.pseudoClassStateChanged(finished, false);
            TaskView.this.taskBox.pseudoClassStateChanged(noTimeLeft, false);
            TaskView.this.taskBox.pseudoClassStateChanged(fewTimeLeft, false);
            TaskView.this.taskBox.pseudoClassStateChanged(muchTimeLeft, false);
        }

        private void setupButtonsAndColoring() {
            if (TaskView.this.task.isFinished()) {
                TaskView.this.lblTitle.setGraphic(ImageLibrary.getImageView("finished.png"));
                TaskView.this.boxButton.getChildren().addAll(TaskView.this.btnDelete, TaskView.this.btnRedo);
                TaskView.this.taskBox.pseudoClassStateChanged(finished, true);
            } else {
                TaskView.this.boxButton.getChildren().add(TaskView.this.btnSubmit);

                if (TaskView.this.task.getDueDate() == null) {
                    TaskView.this.taskBox.pseudoClassStateChanged(muchTimeLeft, true);
                } else {
                    if (LocalDate.now().isAfter(TaskView.this.task.getDueDate().minusDays(2))) {
                        TaskView.this.taskBox.pseudoClassStateChanged(noTimeLeft, true);
                    } else if (LocalDate.now().isAfter(TaskView.this.task.getDueDate().minusDays(4))) {
                        TaskView.this.taskBox.pseudoClassStateChanged(fewTimeLeft, true);
                    } else {
                        TaskView.this.taskBox.pseudoClassStateChanged(muchTimeLeft, true);
                    }
                }
            }
        }

        private void setupTextComponents() {
            TaskView.this.lblTitle.textProperty().bind(TaskView.this.task.nameProperty());
            TaskView.this.lblDueDate.textProperty().bind(Bindings.when(TaskView.this.task.dueDateProperty().isNull()).then("-")
                    .otherwise(TaskView.this.task.dueDateProperty().asString()));
            TaskView.this.lblAttachments.textProperty().bind(TaskView.this.task.attachmentsProperty().sizeProperty().asString());
        }

    }

}
