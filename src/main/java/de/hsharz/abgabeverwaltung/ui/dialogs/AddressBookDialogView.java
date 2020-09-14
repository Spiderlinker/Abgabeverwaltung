package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.views.PersonView;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class AddressBookDialogView extends AbstractStyledView<GridPane> {

    private Label lblTitle;

    private JFXComboBox<Gender> boxGender;
    private JFXTextField fldLastname;
    private JFXTextField fldEmail;

    private Button btnAdd;
    protected Button btnClose;

    private ListView<Person> viewPerson;

    public AddressBookDialogView() {
        super(new GridPane());
        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/AddressBookDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");
        root.setPrefSize(700, 500);
        LayoutUtils.setColumnWidths(root, 20, 60, 20);

        lblTitle = new Label("Address Book");

        boxGender = new JFXComboBox<>(FXCollections.observableArrayList(Gender.values()));
        boxGender.setLabelFloat(true);
        boxGender.getSelectionModel().selectFirst();

        fldLastname = new JFXTextField();
        fldLastname.setPromptText("Last name");
        fldLastname.setLabelFloat(true);

        fldEmail = new JFXTextField();
        fldEmail.setPromptText("E-Mail-Address");
        fldEmail.setLabelFloat(true);

        btnAdd = new JFXButton("Add");
        btnAdd.setDefaultButton(true);
        btnClose = new JFXButton("Close");

        viewPerson = new ListView<>(AddressBook.getContacts());
        viewPerson.setCellFactory(param -> new PersonView().newListCell());
        viewPerson.setPlaceholder(new Label("Add a new contact"));
    }

    @Override
    protected void setupInteractions() {

        btnAdd.disableProperty().bind(Bindings.or(fldLastname.textProperty().isEmpty(), fldEmail.textProperty().isEmpty()));

        btnAdd.setOnAction(e -> {
            Person newPerson = new Person(fldLastname.getText(), fldEmail.getText(), boxGender.getValue());
            AddressBook.addContacts(newPerson);

            fldEmail.clear();
            fldLastname.clear();
            boxGender.getSelectionModel().selectFirst();
        });


    }

    @Override
    protected void addWidgets() {
        root.add(lblTitle, 0, 0, 3, 1);

        root.add(boxGender, 0, 1);
        root.add(fldLastname, 1, 1);
        root.add(fldEmail, 0, 2, 2, 1);
        root.add(btnAdd, 2, 1, 1, 2);

        root.add(viewPerson, 0, 3, 3, 1);
        root.add(btnClose, 2, 4);
    }



}
