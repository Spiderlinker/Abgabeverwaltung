package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.*;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.addresses.Gender;
import de.hsharz.abgabeverwaltung.addresses.Person;
import de.spiderlinker.utils.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;

public class AddressBookView {

    private File configurationFile;

    private StackPane parent;
    private JFXDialog dialog;
    private GridPane root;

    private Label lblTitle;

    private JFXComboBox<Gender> boxGender;
    private JFXTextField fldLastname;
    private JFXTextField fldEmail;

    private Button btnAdd;
    private Button btnClose;

    private ListView<Person> viewPerson;

    Properties properties = new Properties();

    public AddressBookView(StackPane parent) {
        this.parent = parent;

        createWidgets();
        setupInteractions();
        addWidgets();
    }

    private void createWidgets() {
        root = new GridPane();
        root.getStylesheets().add(this.getClass().getResource("/style/dialog/SettingsDialog.css").toExternalForm());
        root.getStyleClass().add("root");
        root.setPrefSize(700, 500);
        setColumnWidths(root, 20, 60, 20);

        lblTitle = new Label("Address Book");

        boxGender = new JFXComboBox<>(FXCollections.observableArrayList(Gender.values()));

        fldLastname = new JFXTextField();
        fldLastname.setPromptText("Last name");
        fldLastname.setLabelFloat(true);

        fldEmail = new JFXTextField();
        fldEmail.setPromptText("E-Mail-Address");
        fldEmail.setLabelFloat(true);

        btnAdd = new JFXButton("Add");
        btnClose = new JFXButton("Close");

        viewPerson = new ListView<>(AddressBook.getContacts());
        viewPerson.setCellFactory(param -> new PersonView());
        viewPerson.setPlaceholder(new Label("Add a new contact"));

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

        btnClose.setOnAction(e -> dialog.close());

        btnAdd.disableProperty().bind(Bindings.or(fldLastname.textProperty().isEmpty(), fldEmail.textProperty().isEmpty()));

        btnAdd.setOnAction(e -> {
            Person newPerson = new Person(fldLastname.getText(), fldEmail.getText(), boxGender.getValue());
            AddressBook.addContacts(newPerson);

            fldEmail.clear();
            fldLastname.clear();
            boxGender.getSelectionModel().select(0);

        });


    }

    private void addWidgets() {
        root.add(lblTitle, 0, 0, 3, 1);

        root.add(boxGender, 0, 1);
        root.add(fldLastname, 1, 1);
        root.add(fldEmail, 0, 2, 2, 1);
        root.add(btnAdd, 2, 1, 1, 2);

        root.add(viewPerson, 0, 3, 3, 1);
        root.add(btnClose, 2, 4);
    }

    public void show() {
        dialog.show();
    }


}
