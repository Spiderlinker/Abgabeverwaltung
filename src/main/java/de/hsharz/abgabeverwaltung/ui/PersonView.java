package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.Task;
import de.hsharz.abgabeverwaltung.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.addresses.Person;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.File;

public class PersonView extends ListCell<Person> {

    private HBox root;
    private Label lblName;
    private Label lblEmail;
    private Label lblGender;
    private Button btnDelete;

    private Person person;

    public PersonView() {
        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    private void createWidgets() {
        this.root = new HBox(20);

        this.lblName = new Label();
        this.lblEmail = new Label();
        this.lblGender = new Label();

        this.btnDelete = new JFXButton("Entfernen");
        btnDelete.setMinWidth(50);
    }

    private void setupInteractions() {
        btnDelete.setOnAction(e-> AddressBook.removeContact(person));
    }

    private void addWidgets() {
        this.root.getChildren().addAll(lblGender, this.lblName, lblEmail, this.getHSpacer(), this.btnDelete);
    }

    private Region getHSpacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public Pane getPane() {
        return this.root;
    }

    @Override
    protected void updateItem(Person item, boolean empty) {
        super.updateItem(item, empty);

        this.person = item;

        if (item == null) {
            setGraphic(null);
            return;
        }
        prefWidthProperty().bind(getListView().widthProperty().subtract(20));
        setMaxWidth(Control.USE_PREF_SIZE);

        lblGender.setText(item.getGender().toString());
        lblName.setText(item.getLastname());
        lblEmail.setText(item.getEmail());
        setGraphic(root);

    }

}
