package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.spreadsheet.Grid;

import java.util.Comparator;
import java.util.Objects;

public class ModuleDialogView extends AbstractStyledView<GridPane> {

    private Label lblTitle;
    protected TextField fldModuleName;
    private JFXChipView<Person> profs;

    private Button btnCreateProf;
    protected Button btnSave;
    protected Button btnDelete;

    private Module module;

    public ModuleDialogView(Module module) {
        super(new GridPane());
        this.module = Objects.requireNonNull(module);

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/ModuleDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");
        root.setPrefSize(600, 350);

        LayoutUtils.setColumnWidths(root, 50, 50);

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
    }


    @Override
    protected void setupInteractions() {
        btnSave.disableProperty().bind(module.nameProperty().isEmpty());

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
    }

    @Override
    protected void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldModuleName, 0, 1, 2, 1);
        root.add(profs, 0, 2, 2, 1);
        root.add(btnCreateProf, 1, 3);
        root.add(btnDelete, 0, 4);
        root.add(btnSave, 1, 4);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
    }


}
