package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import de.hsharz.abgabeverwaltung.ui.views.PersonView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class AddressBookDialogView extends AbstractStyledView<GridPane> {

    private Label               lblTitle;

    private JFXComboBox<Gender> boxGender;
    private JFXTextField        fldLastname;
    private JFXTextField        fldEmail;

    private Button              btnAdd;
    protected Button            btnClose;

    private ListView<Person>    viewPerson;

    public AddressBookDialogView() {
        super(new GridPane());
        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/AddressBookDialog.css";
    }

    @Override
    protected void createWidgets() {
        this.root.getStyleClass().add("root");
        this.root.setPrefSize(700, 600);
        LayoutUtils.setColumnWidths(this.root, 20, 60, 20);

        this.lblTitle = new Label("Address Book");
        this.lblTitle.getStyleClass().add("title");

        this.boxGender = new JFXComboBox<>(FXCollections.observableArrayList(Gender.values()));
        this.boxGender.setLabelFloat(true);
        this.boxGender.getSelectionModel().selectFirst();

        this.fldLastname = new JFXTextField();
        this.fldLastname.setPromptText("Last name");
        this.fldLastname.setLabelFloat(true);

        this.fldEmail = new JFXTextField();
        this.fldEmail.setPromptText("E-Mail-Address");
        this.fldEmail.setLabelFloat(true);

        this.btnAdd = new JFXButton("Add", ImageLibrary.getImageView("add_contact.png"));
        this.btnAdd.setDefaultButton(true);
        this.btnClose = new JFXButton("Close");
        this.btnClose.getStyleClass().add("save-button");

        this.viewPerson = new ListView<>(AddressBook.getContacts());
        this.viewPerson.setCellFactory(param -> new PersonView().newListCell());
        this.viewPerson.setPlaceholder(new Label("Add a new contact"));
    }

    @Override
    protected void setupInteractions() {

        this.btnAdd.disableProperty().bind(Bindings.or(this.fldLastname.textProperty().isEmpty(), this.fldEmail.textProperty().isEmpty()));

        this.btnAdd.setOnAction(e -> {
            Person newPerson = new Person(this.fldLastname.getText(), this.fldEmail.getText(), this.boxGender.getValue());
            AddressBook.addContacts(newPerson);

            this.fldEmail.clear();
            this.fldLastname.clear();
            this.boxGender.getSelectionModel().selectFirst();
        });

    }

    @Override
    protected void addWidgets() {
        this.root.add(this.lblTitle, 0, 0, 3, 1);

        this.root.add(this.boxGender, 0, 1);
        this.root.add(this.fldLastname, 1, 1);
        this.root.add(this.fldEmail, 0, 2, 2, 1);
        this.root.add(this.btnAdd, 2, 1, 1, 2);

        this.root.add(this.viewPerson, 0, 3, 3, 1);
        this.root.add(this.btnClose, 2, 4);

        GridPane.setHalignment(this.lblTitle, HPos.CENTER);
    }

}
