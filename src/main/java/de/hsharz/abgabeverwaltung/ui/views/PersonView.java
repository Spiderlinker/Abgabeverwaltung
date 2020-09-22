package de.hsharz.abgabeverwaltung.ui.views;

import com.jfoenix.controls.JFXButton;

import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractView;
import de.hsharz.abgabeverwaltung.ui.utils.ImageLibrary;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class PersonView extends AbstractView<HBox> {

    private Label  lblName;
    private Label  lblEmail;
    private Label  lblGender;
    private Button btnDelete;

    private Person person;

    public PersonView() {
        super(new HBox());

        this.initializeView();
    }

    @Override
    protected void createWidgets() {
        this.root.setSpacing(20);

        this.lblName = new Label();
        this.lblEmail = new Label();
        this.lblGender = new Label();

        this.btnDelete = new JFXButton("", ImageLibrary.getImageView("trash.png"));
        this.btnDelete.setMinWidth(50);
    }

    @Override
    protected void setupInteractions() {
        this.btnDelete.setOnAction(e -> AddressBook.removeContact(this.person));
    }

    @Override
    protected void addWidgets() {
        this.root.getChildren().addAll(this.lblGender, this.lblName, this.lblEmail, LayoutUtils.getHSpacer(), this.btnDelete);
    }

    public ListCell<Person> newListCell() {
        return new PersonListCell();
    }

    private class PersonListCell extends ListCell<Person> {

        @Override
        protected void updateItem(final Person item, final boolean empty) {
            super.updateItem(item, empty);

            PersonView.this.person = item;

            if (item == null) {
                this.setGraphic(null);
                return;
            }
            this.prefWidthProperty().bind(this.getListView().widthProperty().subtract(20));
            this.setMaxWidth(Region.USE_PREF_SIZE);

            PersonView.this.lblGender.setText(item.getGender().toString());
            PersonView.this.lblName.setText(item.getLastname());
            PersonView.this.lblEmail.setText(item.getEmail());
            this.setGraphic(PersonView.this.root);

        }
    }
}
