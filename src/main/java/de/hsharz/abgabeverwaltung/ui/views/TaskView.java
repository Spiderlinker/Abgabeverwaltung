package de.hsharz.abgabeverwaltung.ui.views;

import java.io.File;
import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskSubmitDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskSubmitDialogView;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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

    private Button btnSubmit;

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

        this.lblDueDate = new Label("", ImageLibrary.getImageViewScaled("calendar_bold.png", 24));

        this.lblAttachments = new Label("", ImageLibrary.getImageViewScaled("attachments_bold.png", 24));

        this.btnSubmit = new JFXButton("Review & Submit");

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

        btnSubmit.setOnAction(e -> {
            new TaskSubmitDialog(parent, module.get(), task).show();


        });

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

        HBox boxButton = new HBox();
        boxButton.getChildren().addAll(btnSubmit);
        boxButton.setAlignment(Pos.CENTER_RIGHT);
        this.taskBox.getChildren().add(boxButton);


        this.root.getChildren().addAll(this.taskBox, this.dragPane);
    }

    public Button getSaveButton() {
        return this.btnSubmit;
    }


    public ListCell<Task> newListCell() {
        return new TaskListCell();
    }

    class TaskListCell extends ListCell<Task> {

        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null) {
                setGraphic(null);
                return;
            }

            task = item;

            task.isFinishedProperty().addListener((observable, oldValue, newValue) -> {
                taskBox.pseudoClassStateChanged(finished, oldValue);
                taskBox.pseudoClassStateChanged(noTimeLeft, oldValue);
                taskBox.pseudoClassStateChanged(fewTimeLeft, oldValue);

                taskBox.pseudoClassStateChanged(finished, newValue);
            });

            taskBox.pseudoClassStateChanged(finished, false);
            taskBox.pseudoClassStateChanged(noTimeLeft, false);
            taskBox.pseudoClassStateChanged(fewTimeLeft, false);

            taskBox.pseudoClassStateChanged(muchTimeLeft, true);

            if (task.isFinished()) {
                taskBox.pseudoClassStateChanged(muchTimeLeft, false);
                taskBox.pseudoClassStateChanged(finished, true);
            } else if (task.getDueDate() != null) {
                if (LocalDate.now().isAfter(task.getDueDate().minusDays(2))) {
                    taskBox.pseudoClassStateChanged(finished, false);
                    taskBox.pseudoClassStateChanged(muchTimeLeft, false);
                    taskBox.pseudoClassStateChanged(fewTimeLeft, false);

                    taskBox.pseudoClassStateChanged(noTimeLeft, true);
                } else if (LocalDate.now().isAfter(task.getDueDate().minusDays(4))) {
                    taskBox.pseudoClassStateChanged(finished, false);
                    taskBox.pseudoClassStateChanged(muchTimeLeft, false);
                    taskBox.pseudoClassStateChanged(noTimeLeft, false);

                    taskBox.pseudoClassStateChanged(fewTimeLeft, true);
                }
            }

            lblTitle.textProperty().bind(task.nameProperty());
            lblDueDate.textProperty().bind(Bindings
                    .when(task.dueDateProperty().isNull())
                    .then("-")
                    .otherwise(task.dueDateProperty().asString()));
            lblAttachments.textProperty().bind(task.attachmentsProperty().sizeProperty().asString());

            setGraphic(root);
        }
    }


}
