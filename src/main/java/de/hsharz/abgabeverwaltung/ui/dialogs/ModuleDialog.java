package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.Module;
import de.hsharz.abgabeverwaltung.ModuleDatabase;
import de.hsharz.abgabeverwaltung.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.addresses.Person;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Comparator;
import java.util.Objects;

public class ModuleDialog {

    private JFXDialog dialog;
    private StackPane parent;

    private GridPane root;

    private Label lblTitle;
    private TextField fldModuleName;
    private JFXChipView<Person> profs;

    private Button btnCreateProf;
    private Button btnSave;
    private Button btnDelete;

    private Module module;

    public ModuleDialog(StackPane parent, Module module) {
        this.parent = parent;
        this.module = Objects.requireNonNull(module);

        createWidgets();
        setupInteractions();
        addWidgets();
    }

    private void createWidgets() {
        root = new GridPane();
        root.getStylesheets().add(this.getClass().getResource("/style/dialog/ModuleDialog.css").toExternalForm());
        root.getStyleClass().add("root");
        root.setPrefSize(600, 350);

        setColumnWidths(root, 50, 50);

        lblTitle = new Label("Edit Module");
        lblTitle.getStyleClass().add("title");

        fldModuleName = new JFXTextField();
        fldModuleName.textProperty().bindBidirectional(module.nameProperty());

        profs = new JFXChipView<>();
        profs.setPromptText("Choose Professor(s) for this module");
        profs.getSuggestions().addAll(AddressBook.getContacts());
        profs.getChips().addAll(module.getProfessors());

        btnSave = new JFXButton("Save Module");
        btnSave.setDefaultButton(true);
        btnDelete = new JFXButton("Delete Module");
        btnCreateProf = new JFXButton("New Professor...");

        dialog = new JFXDialog(parent, root, JFXDialog.DialogTransition.TOP);

    }

    private void setColumnWidths(GridPane root, int... columnWidths) {

        for (int columnWidth : columnWidths) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(columnWidth);
            root.getColumnConstraints().add(c);
        }

    }

    private void setupInteractions() {
        btnSave.disableProperty().bind(module.nameProperty().isEmpty());

        btnDelete.setOnAction(e -> {
            ModuleDatabase.getInstance().removeModule(module);
            dialog.close();
        });
        btnSave.setOnAction(e -> {
            dialog.close();
            ModuleDatabase.getInstance().getModules().sort(Comparator.comparing(Module::getName));
        });

        profs.getChips().addListener((ListChangeListener<Person>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    module.getProfessors().addAll(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    module.getProfessors().removeAll(c.getRemoved());
                }
            }
        });

        dialog.visibleProperty().addListener((observable, oldValue, newValue) ->{
            fldModuleName.requestFocus();
            fldModuleName.selectAll();
        });
    }

    private void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldModuleName, 0, 1, 2, 1);
        root.add(profs, 0, 2, 2, 1);
        root.add(btnCreateProf, 1, 3);
        root.add(btnDelete, 0, 4);
        root.add(btnSave, 1, 4);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
    }


    public void show() {
        dialog.show();
    }
}
