package de.hsharz.abgabeverwaltung.ui.views;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.model.Task;
import de.hsharz.abgabeverwaltung.ui.dialogs.ModuleDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.TaskDialog;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ModuleView extends AbstractStyledView<BorderPane> {

    private StackPane parent;

    private Label lblModule;
    private Button btnAddTask;
    private Button btnRemoveModule;
    private Button btnEditModule;
    private ListView<Task> viewTasks;

    private ObjectProperty<Module> module = new SimpleObjectProperty<>();

    public ModuleView(StackPane parent) {
        super(new BorderPane());
        this.parent = parent;

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/ModuleView.css";
    }

    @Override
    protected void createWidgets() {
        root.setPrefSize(0, 300);

        lblModule = new Label();
        lblModule.getStyleClass().add("header-label");

        btnAddTask = new JFXButton("Add Task");
        btnRemoveModule = new JFXButton("", ImageLibrary.getImageView("trash_bold.png"));
        btnEditModule = new JFXButton("", ImageLibrary.getImageView("edit_bold.png"));

        viewTasks = new ListView<>();
        viewTasks.setOrientation(Orientation.HORIZONTAL);
        viewTasks.setCellFactory(param -> new TaskView(parent, module).newListCell());
        viewTasks.setPlaceholder(new Label("Click 'Add Task' to create your first Task for this Module"));

    }

    @Override
    protected void setupInteractions() {
        btnAddTask.setOnAction(e -> {
            Task task = new Task("New Task...");
            module.get().addTask(task);
            new TaskDialog(parent, module.get(), task).show();
        });

        btnEditModule.setOnAction(e -> new ModuleDialog(parent, module.get()).show());

        btnRemoveModule.setOnAction(e -> ModuleDatabase.getInstance().removeModule(module.get()));
    }

    @Override
    protected void addWidgets() {

        HBox boxTitle = new HBox(5);
        boxTitle.getChildren().addAll(btnAddTask, LayoutUtils.getHSpacer(), lblModule, LayoutUtils.getHSpacer(), btnEditModule, btnRemoveModule);
        boxTitle.getStyleClass().add("headerBox");

        root.setTop(boxTitle);
        root.setCenter(viewTasks);
    }


    public ListCell<Module> newListCell() {
        return new ModuleListCell();
    }

    private class ModuleListCell extends ListCell<Module> {

        @Override
        protected void updateItem(Module item, boolean empty) {
            super.updateItem(item, empty);

            module.set(item);

            if (item == null) {
                setGraphic(null);
                return;
            }

            lblModule.textProperty().bind(item.nameProperty());
            viewTasks.setItems(item.getTasks());

            setGraphic(root);
        }
    }
}
