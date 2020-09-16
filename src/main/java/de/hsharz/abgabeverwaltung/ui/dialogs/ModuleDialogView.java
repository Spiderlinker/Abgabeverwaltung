package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.*;
import com.jfoenix.skins.JFXChipViewSkin;
import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.spreadsheet.Grid;

import java.util.Comparator;
import java.util.Objects;

public class ModuleDialogView extends AbstractStyledView<GridPane> {

    private Label lblTitle;
    protected JFXTextField fldModuleName;
    private JFXComboBox<Person> boxProf;

    private Button btnManageProfs;
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
        root.setPrefSize(600, 500);

        LayoutUtils.setColumnWidths(root, 60, 40);

        lblTitle = new Label("Edit Module");
        lblTitle.getStyleClass().add("title");

        fldModuleName = new JFXTextField();
        fldModuleName.textProperty().bindBidirectional(module.nameProperty());
        fldModuleName.setPromptText("Module name");
        fldModuleName.setLabelFloat(true);

        boxProf = new JFXComboBox<>(AddressBook.getContacts());
        boxProf.getSelectionModel().select(module.getProfessor());
        module.professorProperty().bind(boxProf.getSelectionModel().selectedItemProperty());
        boxProf.setPromptText("Professor of this module");
        boxProf.setLabelFloat(true);

        btnSave = new JFXButton("Save Module");
        btnSave.setDefaultButton(true);
        btnDelete = new JFXButton("Delete Module");
        btnManageProfs = new JFXButton("Manage Professors");
    }


    @Override
    protected void setupInteractions() {
        btnSave.disableProperty().bind(Bindings.or(module.nameProperty().isEmpty(), boxProf.getSelectionModel().selectedItemProperty().isNull()));
        btnManageProfs.setOnAction(e -> DialogCache.getDialog(DialogCache.DialogType.ADDRESS_BOOK).show());
    }

    @Override
    protected void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldModuleName, 0, 1, 2, 1);
        root.add(boxProf, 0, 2);
        root.add(btnManageProfs, 1, 2);
        root.add(btnDelete, 0, 3);
        root.add(btnSave, 1, 3);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
    }

}
