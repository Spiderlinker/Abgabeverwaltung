package de.hsharz.abgabeverwaltung.ui.views;

import java.io.File;
import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskSubmitDialog;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
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
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

public class TaskView extends AbstractStyledView<StackPane> {

    public static final PseudoClass finished = PseudoClass.getPseudoClass("finished");
    public static final PseudoClass muchTimeLeft = PseudoClass.getPseudoClass("much-time-left");
    public static final PseudoClass fewTimeLeft = PseudoClass.getPseudoClass("few-time-left");
    public static final PseudoClass noTimeLeft = PseudoClass.getPseudoClass("no-time-left");

    private StackPane parent;

    private BorderPane dragPane;

    private VBox taskBox;
    private Label lblTitle;
    private Label lblDueDate;
    private Label lblAttachments;

    private HBox boxButton;

    private Button btnSubmit;
    private Button btnDelete;
    private Button btnRedo;

    private ObjectProperty<Module> module;

    private Task task;

    public TaskView(StackPane parent, ObjectProperty<Module> module) {
        super(new StackPane());
        this.parent = parent;
        this.module = module;

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/Task.css";
    }

    @Override
    protected void createWidgets() {
        this.root.setPrefSize(250, 200);

        this.taskBox = new VBox(10);
        taskBox.getStyleClass().add("taskBox");

        this.lblTitle = new Label();
        this.lblTitle.getStyleClass().add("title");

        Tooltip tooltipTitle = new Tooltip();
        tooltipTitle.textProperty().bind(lblTitle.textProperty());
        lblTitle.setTooltip(tooltipTitle);

        this.lblDueDate = new Label("", ImageLibrary.getImageView("calendar.png"));

        this.lblAttachments = new Label("", ImageLibrary.getImageView("attachments.png"));

        boxButton = new HBox(10);
        this.btnSubmit = new JFXButton("Review & Submit", ImageLibrary.getImageView("check_next.png"));
        btnRedo = new JFXButton("Re-Open", ImageLibrary.getImageView("repeat.png"));
        btnDelete = new JFXButton("", ImageLibrary.getImageView("trash.png"));

        this.dragPane = new BorderPane();
        this.dragPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        Label label = new Label("Drop here to add file to task");
        label.setStyle("-fx-font-size: 14pt;");
        this.dragPane.setCenter(label);

        this.dragPane.setVisible(false);
    }

    @Override
    protected void setupInteractions() {

        this.root.setOnDragEntered(event -> this.dragPane.setVisible(true));
        this.root.setOnDragExited(event -> this.dragPane.setVisible(false));

        this.root.setCursor(Cursor.HAND);

        this.root.setOnMouseClicked(e -> new TaskDialog(parent, module.get(), task).show());

        btnSubmit.setOnAction(e -> new TaskSubmitDialog(parent, module.get(), task).show());
        btnRedo.setOnAction(e -> task.setFinished(false));
        btnDelete.setOnAction(e -> module.get().removeTask(task));

        this.dragPane.setOnDragOver(event -> {
            if (event.getGestureSource() != this.dragPane && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        this.dragPane.setOnDragDropped(event -> {
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
        this.taskBox.getChildren().add(this.lblTitle);
        this.taskBox.getChildren().add(LayoutUtils.getVSpacer());
        this.taskBox.getChildren().add(this.lblDueDate);
        this.taskBox.getChildren().add(this.lblAttachments);
        this.taskBox.getChildren().add(LayoutUtils.getVSpacer());

        boxButton.getChildren().addAll(btnSubmit);
        boxButton.setAlignment(Pos.CENTER_RIGHT);
        this.taskBox.getChildren().add(boxButton);


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
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            updateItem(task, task == null);
            System.out.println("Update called " + task.getName());
        }

        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null) {
                setGraphic(null);
                return;
            }

            if (task != null) {
                task.isFinishedProperty().removeListener(this);
            }

            task = item;
            task.isFinishedProperty().addListener(this);

            resetTaskViewToDefault();
            setupButtonsAndColoring();
            setupTextComponents();

            setGraphic(root);
        }

        private void resetTaskViewToDefault() {
            resetPseudoClasses();
            lblTitle.setGraphic(null);
            boxButton.getChildren().clear();
        }

        private void resetPseudoClasses() {
            taskBox.pseudoClassStateChanged(finished, false);
            taskBox.pseudoClassStateChanged(noTimeLeft, false);
            taskBox.pseudoClassStateChanged(fewTimeLeft, false);
            taskBox.pseudoClassStateChanged(muchTimeLeft, false);
        }

        private void setupButtonsAndColoring() {
            if (task.isFinished()) {
                lblTitle.setGraphic(ImageLibrary.getImageView("finished.png"));
                boxButton.getChildren().addAll(btnDelete, btnRedo);
                taskBox.pseudoClassStateChanged(finished, true);
            } else {
                boxButton.getChildren().add(btnSubmit);

                if (task.getDueDate() == null) {
                    taskBox.pseudoClassStateChanged(muchTimeLeft, true);
                } else {
                    if (LocalDate.now().isAfter(task.getDueDate().minusDays(2))) {
                        taskBox.pseudoClassStateChanged(noTimeLeft, true);
                    } else if (LocalDate.now().isAfter(task.getDueDate().minusDays(4))) {
                        taskBox.pseudoClassStateChanged(fewTimeLeft, true);
                    } else {
                        taskBox.pseudoClassStateChanged(muchTimeLeft, true);
                    }
                }
            }
        }

        private void setupTextComponents() {
            lblTitle.textProperty().bind(task.nameProperty());
            lblDueDate.textProperty().bind(Bindings
                    .when(task.dueDateProperty().isNull())
                    .then("-")
                    .otherwise(task.dueDateProperty().asString()));
            lblAttachments.textProperty().bind(task.attachmentsProperty().sizeProperty().asString());
        }

    }


}
