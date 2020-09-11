package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.Module;
import de.hsharz.abgabeverwaltung.ModuleDatabase;
import de.hsharz.abgabeverwaltung.ui.dialogs.ModuleDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

public class SemesterView {

    private StackPane root;
    private VBox mainPane;
    private HBox boxTop;

    private Label lblTitle;
    private Button btnAddModule;
    private Button btnManageProfs;
    private Button btnSettings;

    private ListView<Module> viewModules;

    public SemesterView() {
        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    private void createWidgets() {
        this.root = new StackPane();
        root.getStylesheets().add(this.getClass().getResource("/style/ModulesView.css").toExternalForm());

        this.mainPane = new VBox();
        mainPane.getStyleClass().add("mainPane");

        lblTitle = new Label("Task Manage & Submission System");
        lblTitle.getStyleClass().add("labelTitle");

        boxTop = new HBox(20);
        boxTop.getStyleClass().add("boxTop");

        btnManageProfs = new JFXButton("Manage Professors");
        btnSettings = new JFXButton("Settings");
        this.btnAddModule = new JFXButton("Modul hinzufügen");

        viewModules = new ListView<>(ModuleDatabase.getInstance().getModules());
        viewModules.setCellFactory(param -> new ModuleView(root));
        viewModules.setPlaceholder(new Label("Click an 'Create Module' to create your first Module"));

    }

    private void setupInteractions() {
        this.btnAddModule.setOnAction(e -> {

            Module module = new Module("New Module...");
            ModuleDatabase.getInstance().addModule(module);
            new ModuleDialog(root, module).show();
        });

        btnManageProfs.setOnAction(e -> new AddressBookView(root).show());

        btnSettings.setOnAction(e ->{
            new SettingsView(root).show();
        });
    }

    private void addWidgets() {

        this.root.getChildren().add(this.mainPane);

        boxTop.getChildren().addAll(btnAddModule, getHSpacer(), btnManageProfs, btnSettings);
        HBox.setHgrow(btnAddModule, Priority.SOMETIMES);

        this.mainPane.getChildren().add(lblTitle);
        this.mainPane.getChildren().add(boxTop);
        this.mainPane.getChildren().add(viewModules);

        VBox.setVgrow(viewModules, Priority.ALWAYS);
    }

    private Region getHSpacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public Pane getPane() {
        return root;
    }


}
