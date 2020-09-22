package de.hsharz.abgabeverwaltung.ui.dialogs;

import javafx.scene.layout.StackPane;

public class AddressBookDialog extends AbstractDialog {

    private AddressBookDialogView addressBookDialogView;

    public AddressBookDialog(final StackPane parent) {
        super(parent, DialogTransition.TOP);

        this.createWidgets();
        this.setupInteractions();
        this.enableCloseOnEscape();
    }

    private void createWidgets() {
        this.addressBookDialogView = new AddressBookDialogView();
        this.setContent(this.addressBookDialogView.getPane());
    }

    private void setupInteractions() {
        this.addressBookDialogView.btnClose.setOnAction(e -> this.close());
    }

}
