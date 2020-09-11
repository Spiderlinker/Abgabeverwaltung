package de.hsharz.abgabeverwaltung.ui;

import java.io.File;
import java.time.LocalDate;
import java.util.Random;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.Module;
import de.hsharz.abgabeverwaltung.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

public class TaskView extends ListCell<Task> {

    public static final PseudoClass finished = PseudoClass.getPseudoClass("finished");
    public static final PseudoClass muchTimeLeft = PseudoClass.getPseudoClass("much-time-left");
    public static final PseudoClass fewTimeLeft = PseudoClass.getPseudoClass("few-time-left");
    public static final PseudoClass noTimeLeft = PseudoClass.getPseudoClass("no-time-left");

    private static final int[] finishedColor = {204, 255, 153};

    private StackPane parent;

    private StackPane root;
    private BorderPane dragPane;

    private VBox taskBox;
    private Label lblTitle;
    private Label lblDueDateDescription;
    private Label lblDueDate;
    private Label lblAttachments;
    private Button btnSave;

    private ObjectProperty<Module> module;
    private Task task;

    public TaskView(StackPane parent, ObjectProperty<Module> module) {
        this.parent = parent;
        this.module = module;

        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();

    }

    private void createWidgets() {
        this.root = new StackPane();
        this.root.setPrefSize(250, 200);
        this.root.getStylesheets().add(this.getClass().getResource("/style/Task.css").toExternalForm());

        this.taskBox = new VBox(10);
        taskBox.getStyleClass().add("taskBox");

        this.lblTitle = new Label();
        this.lblTitle.getStyleClass().add("title");

        this.lblDueDate = new Label();
        this.lblAttachments = new Label();

        this.btnSave = new JFXButton("Check and Submit");

        this.dragPane = new BorderPane();
        this.dragPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        Label label = new Label("Drop here to add file to task");
        label.setStyle("-fx-font-size: 14pt;");
        this.dragPane.setCenter(label);

        this.dragPane.setVisible(false);
    }

    private void setupInteractions() {

        this.root.setOnDragEntered(event -> this.dragPane.setVisible(true));
        this.root.setOnDragExited(event -> this.dragPane.setVisible(false));

        this.root.setCursor(Cursor.HAND);

        this.root.setOnMouseClicked(e -> new TaskDialog(parent, module.get(), task).show());

        btnSave.setOnAction(e -> {

            new TaskSubmitView(parent, module.get(), task).show();

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

    private void addWidgets() {
        this.taskBox.getChildren().add(this.lblTitle);
        this.taskBox.getChildren().add(this.getVSpacer());
        this.taskBox.getChildren().add(this.lblDueDate);
        this.taskBox.getChildren().add(this.lblAttachments);
        this.taskBox.getChildren().add(this.getVSpacer());
        this.taskBox.getChildren().add(this.btnSave);

        this.root.getChildren().addAll(this.taskBox, this.dragPane);
    }

    public Pane getPane() {
        return this.root;
    }

    public Button getSaveButton() {
        return this.btnSave;
    }

    private Region getVSpacer() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            setGraphic(null);
            return;
        }

        this.task = item;

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
                .concat("Abgabedatum: ")
                .concat(Bindings.when(task.dueDateProperty().isNull())
                        .then("-")
                        .otherwise(task.dueDateProperty().asString())));
        lblAttachments.textProperty().bind(Bindings
                .concat(task.attachmentsProperty().sizeProperty())
                .concat(" Dateianhänge"));

        setGraphic(root);
    }
}
