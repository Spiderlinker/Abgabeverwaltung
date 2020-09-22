package de.hsharz.abgabeverwaltung.ui.views;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.ui.dialogs.DialogCache;
import de.hsharz.abgabeverwaltung.ui.dialogs.ModuleDialog;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SemesterView extends AbstractStyledView<StackPane> {

    private VBox             mainPane;
    private HBox             boxTop;

    private Label            lblTitle;
    private Button           btnAddModule;
    private Button           btnManageProfs;
    private Button           btnSettings;

    private ListView<Module> viewModules;

    public SemesterView() {
        super(new StackPane());

        this.initializeView();
        DialogCache.init(this.root);
    }

    @Override
    protected String getStylesheet() {
        return "/style/ModulesView.css";
    }

    @Override
    protected void createWidgets() {
        this.mainPane = new VBox();
        this.mainPane.getStyleClass().add("mainPane");

        this.lblTitle = new Label("Task Manage & Submission System");
        this.lblTitle.getStyleClass().add("labelTitle");

        this.boxTop = new HBox(20);
        this.boxTop.getStyleClass().add("boxTop");

        this.btnManageProfs = new JFXButton("Manage Professors", ImageLibrary.getImageView("address_book.png"));
        this.btnManageProfs.setAlignment(Pos.CENTER_LEFT);
        this.btnSettings = new JFXButton("Settings", ImageLibrary.getImageView("settings.png"));
        this.btnSettings.setAlignment(Pos.CENTER_LEFT);
        this.btnAddModule = new JFXButton("Add new module");

        this.viewModules = new ListView<>(ModuleDatabase.getInstance().getModules());
        this.viewModules.setCellFactory(param -> new ModuleView(this.root).newListCell());
        this.viewModules.setPlaceholder(new Label("Click 'Create Module' to create your first Module"));

    }

    @Override
    protected void setupInteractions() {
        this.btnAddModule.setOnAction(e -> {

            Module module = new Module("New Module...");
            ModuleDatabase.getInstance().addModule(module);
            new ModuleDialog(this.root, module).show();
        });

        this.btnManageProfs.setOnAction(e -> DialogCache.getDialog(DialogCache.DialogType.ADDRESS_BOOK).show());

        this.btnSettings.setOnAction(e -> DialogCache.getDialog(DialogCache.DialogType.SETTINGS).show());
    }

    @Override
    protected void addWidgets() {

        this.root.getChildren().add(this.mainPane);

        this.boxTop.getChildren().addAll(this.btnAddModule, LayoutUtils.getHSpacer(), this.btnManageProfs, this.btnSettings);
        HBox.setHgrow(this.btnAddModule, Priority.SOMETIMES);

        this.mainPane.getChildren().add(this.lblTitle);
        this.mainPane.getChildren().add(this.boxTop);
        this.mainPane.getChildren().add(this.viewModules);

        VBox.setVgrow(this.viewModules, Priority.ALWAYS);
    }

}
