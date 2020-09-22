package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ModuleDialogView extends AbstractStyledView<GridPane> {

    private Label               lblTitle;
    protected JFXTextField      fldModuleName;
    private JFXComboBox<Person> boxProf;

    private Button              btnManageProfs;
    protected Button            btnSave;
    protected Button            btnDelete;

    private Module              module;

    public ModuleDialogView(final Module module) {
        super(new GridPane());
        this.module = Objects.requireNonNull(module);

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/DefaultDialog.css";
    }

    @Override
    protected void createWidgets() {
        this.root.getStyleClass().add("root");
        this.root.setPrefSize(600, 350);

        LayoutUtils.setColumnWidths(this.root, 60, 40);

        this.lblTitle = new Label("Edit Module");
        this.lblTitle.getStyleClass().add("title");

        this.fldModuleName = new JFXTextField();
        this.fldModuleName.textProperty().bindBidirectional(this.module.nameProperty());
        this.fldModuleName.setPromptText("Module name");
        this.fldModuleName.setLabelFloat(true);

        this.boxProf = new JFXComboBox<>(AddressBook.getContacts());
        this.boxProf.getSelectionModel().select(this.module.getProfessor());
        this.module.professorProperty().bind(this.boxProf.getSelectionModel().selectedItemProperty());
        this.boxProf.setPromptText("Professor of this module");
        this.boxProf.setLabelFloat(true);

        this.btnSave = new JFXButton("Save Module");
        this.btnSave.getStyleClass().add("save-button");
        this.btnSave.setDefaultButton(true);
        this.btnDelete = new JFXButton("Delete Module", ImageLibrary.getImageView("trash.png"));
        this.btnManageProfs = new JFXButton("Manage Professors");
    }

    @Override
    protected void setupInteractions() {
        this.btnSave.disableProperty()
                .bind(Bindings.or(this.module.nameProperty().isEmpty(), this.boxProf.getSelectionModel().selectedItemProperty().isNull()));
        this.btnManageProfs.setOnAction(e -> DialogCache.getDialog(DialogCache.DialogType.ADDRESS_BOOK).show());
    }

    @Override
    protected void addWidgets() {
        this.root.add(this.lblTitle, 0, 0, 2, 1);
        this.root.add(this.fldModuleName, 0, 1, 2, 1);
        this.root.add(this.boxProf, 0, 2);
        this.root.add(this.btnManageProfs, 1, 2);
        this.root.add(this.btnDelete, 0, 3);
        this.root.add(this.btnSave, 1, 3);

        GridPane.setHalignment(this.lblTitle, HPos.CENTER);
        GridPane.setHalignment(this.btnSave, HPos.RIGHT);
        GridPane.setHalignment(this.btnManageProfs, HPos.RIGHT);
    }

}
