package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.Module;
import de.hsharz.abgabeverwaltung.ModuleDatabase;
import de.hsharz.abgabeverwaltung.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.ModuleDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ModuleView extends ListCell<Module> {

    private StackPane parent;

    private BorderPane root;
    private Label lblModule;
    private Button btnAddTask;
    private Button btnRemoveModule;
    private Button btnEditModule;
    private ListView<Task> viewTasks;

    private ObjectProperty<Module> module = new SimpleObjectProperty<>();

    public ModuleView(StackPane parent) {
        this.parent = parent;

        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    private void createWidgets() {
        root = new BorderPane();
        root.setPrefSize(0, 300);
        root.getStylesheets().add(this.getClass().getResource("/style/ModuleView.css").toExternalForm());

        lblModule = new Label();
        lblModule.getStyleClass().add("header-label");

        btnAddTask = new JFXButton("Add Task");
        btnRemoveModule = new JFXButton("Remove Module");
        btnEditModule = new JFXButton("Edit Module");

        viewTasks = new ListView<>();
        viewTasks.setOrientation(Orientation.HORIZONTAL);
        viewTasks.setCellFactory(param -> new TaskView(parent, module));
        viewTasks.setPlaceholder(new Label("Click an 'Add Task' to create your first Task for this Module"));

    }

    private void setupInteractions() {
        btnAddTask.setOnAction(e -> {
            Task task = new Task("New Task...");
            module.get().addTask(task);
            new TaskDialog(parent, module.get(), task).show();
        });

        btnEditModule.setOnAction(e-> new ModuleDialog(parent, module.get()).show());

        btnRemoveModule.setOnAction(e -> ModuleDatabase.getInstance().removeModule(module.get()));
    }

    private void addWidgets() {

        HBox boxTitle = new HBox(20);
        boxTitle.getChildren().addAll(lblModule, getHSpacer(), btnAddTask, getHSpacer(), btnEditModule, btnRemoveModule);
        boxTitle.getStyleClass().add("headerBox");

        root.setTop(boxTitle);
        root.setCenter(viewTasks);
    }

    private Region getHSpacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    @Override
    protected void updateItem(Module item, boolean empty) {
        super.updateItem(item, empty);

        this.module.set(item);

        if (item == null) {
            setGraphic(null);
            return;
        }

        lblModule.textProperty().bind(item.nameProperty());
        viewTasks.setItems(item.getTasks());

        setGraphic(root);
    }
}
