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

    private StackPane              parent;

    private Label                  lblModule;
    private Button                 btnAddTask;
    private Button                 btnRemoveModule;
    private Button                 btnEditModule;
    private ListView<Task>         viewTasks;

    private ObjectProperty<Module> module = new SimpleObjectProperty<>();

    public ModuleView(final StackPane parent) {
        super(new BorderPane());
        this.parent = parent;

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/ModuleView.css";
    }

    @Override
    protected void createWidgets() {
        this.root.setPrefSize(0, 300);

        this.lblModule = new Label();
        this.lblModule.getStyleClass().add("header-label");

        this.btnAddTask = new JFXButton("Add Task");
        this.btnRemoveModule = new JFXButton("", ImageLibrary.getImageView("trash.png"));
        this.btnEditModule = new JFXButton("", ImageLibrary.getImageView("edit.png"));

        this.viewTasks = new ListView<>();
        this.viewTasks.setOrientation(Orientation.HORIZONTAL);
        this.viewTasks.setCellFactory(param -> new TaskView(this.parent, this.module).newListCell());
        this.viewTasks.setPlaceholder(new Label("Click 'Add Task' to create your first Task for this Module"));

    }

    @Override
    protected void setupInteractions() {
        this.btnAddTask.setOnAction(e -> {
            Task task = new Task("New Task...");
            this.module.get().addTask(task);
            new TaskDialog(this.parent, this.module.get(), task).show();
        });

        this.btnEditModule.setOnAction(e -> new ModuleDialog(this.parent, this.module.get()).show());

        this.btnRemoveModule.setOnAction(e -> ModuleDatabase.getInstance().removeModule(this.module.get()));
    }

    @Override
    protected void addWidgets() {

        HBox boxTitle = new HBox(5);
        boxTitle.getChildren().addAll(this.btnAddTask, LayoutUtils.getHSpacer(), this.lblModule, LayoutUtils.getHSpacer(), this.btnEditModule,
                this.btnRemoveModule);
        boxTitle.getStyleClass().add("headerBox");

        this.root.setTop(boxTitle);
        this.root.setCenter(this.viewTasks);
    }

    public ListCell<Module> newListCell() {
        return new ModuleListCell();
    }

    private class ModuleListCell extends ListCell<Module> {

        @Override
        protected void updateItem(final Module item, final boolean empty) {
            super.updateItem(item, empty);

            ModuleView.this.module.set(item);

            if (item == null) {
                this.setGraphic(null);
                return;
            }

            ModuleView.this.lblModule.textProperty().bind(item.nameProperty());
            ModuleView.this.viewTasks.setItems(item.getTasks());

            this.setGraphic(ModuleView.this.root);
        }
    }
}
