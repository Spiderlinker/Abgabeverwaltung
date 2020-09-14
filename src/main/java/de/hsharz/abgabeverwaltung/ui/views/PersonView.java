package de.hsharz.abgabeverwaltung.ui.views;

import com.jfoenix.controls.JFXButton;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class PersonView extends AbstractView<HBox> {

    private Label lblName;
    private Label lblEmail;
    private Label lblGender;
    private Button btnDelete;

    private Person person;

    public PersonView() {
        super(new HBox());

        initializeView();
    }

    @Override
    protected void createWidgets() {
        this.root.setSpacing(20);

        this.lblName = new Label();
        this.lblEmail = new Label();
        this.lblGender = new Label();

        this.btnDelete = new JFXButton("Entfernen");
        btnDelete.setMinWidth(50);
    }

    @Override
    protected void setupInteractions() {
        btnDelete.setOnAction(e -> AddressBook.removeContact(person));
    }

    @Override
    protected void addWidgets() {
        this.root.getChildren().addAll(lblGender, this.lblName, lblEmail, LayoutUtils.getHSpacer(), this.btnDelete);
    }

    public ListCell<Person> newListCell(){
        return new PersonListCell();
    }

    class PersonListCell extends ListCell<Person> {

        @Override
        protected void updateItem(Person item, boolean empty) {
            super.updateItem(item, empty);

            person = item;

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
}
