package de.hsharz.abgabeverwaltung.ui.views;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.ui.dialogs.AddressBookDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.ModuleDialog;
import de.hsharz.abgabeverwaltung.ui.dialogs.SettingsDialog;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

public class SemesterView extends AbstractStyledView<StackPane> {

    private VBox mainPane;
    private HBox boxTop;

    private Label lblTitle;
    private Button btnAddModule;
    private Button btnManageProfs;
    private Button btnSettings;

    private ListView<Module> viewModules;

    public SemesterView() {
        super(new StackPane());

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/ModulesView.css";
    }

    @Override
    protected void createWidgets() {
        this.mainPane = new VBox();
        mainPane.getStyleClass().add("mainPane");

        lblTitle = new Label("Task Manage & Submission System");
        lblTitle.getStyleClass().add("labelTitle");

        boxTop = new HBox(20);
        boxTop.getStyleClass().add("boxTop");

        btnManageProfs = new JFXButton("Manage Professors", ImageLibrary.getImageViewScaled("address_book_bold.png", 24));
        btnManageProfs.setAlignment(Pos.CENTER_LEFT);
        btnSettings = new JFXButton("Settings", ImageLibrary.getImageViewScaled("settings_bold.png", 24));
        btnSettings.setAlignment(Pos.CENTER_LEFT);
        this.btnAddModule = new JFXButton("Modul hinzufügen");

        viewModules = new ListView<>(ModuleDatabase.getInstance().getModules());
        viewModules.setCellFactory(param -> new ModuleView(root).newListCell());
        viewModules.setPlaceholder(new Label("Click an 'Create Module' to create your first Module"));

    }

    @Override
    protected void setupInteractions() {
        this.btnAddModule.setOnAction(e -> {

            Module module = new Module("New Module...");
            ModuleDatabase.getInstance().addModule(module);
            new ModuleDialog(root, module).show();
        });

        btnManageProfs.setOnAction(e -> new AddressBookDialog(root).show());

        btnSettings.setOnAction(e -> {
            new SettingsDialog(root).show();
        });
    }

    @Override
    protected void addWidgets() {

        this.root.getChildren().add(this.mainPane);

        boxTop.getChildren().addAll(btnAddModule, LayoutUtils.getHSpacer(), btnManageProfs, btnSettings);
        HBox.setHgrow(btnAddModule, Priority.SOMETIMES);

        this.mainPane.getChildren().add(lblTitle);
        this.mainPane.getChildren().add(boxTop);
        this.mainPane.getChildren().add(viewModules);

        VBox.setVgrow(viewModules, Priority.ALWAYS);
    }

}
