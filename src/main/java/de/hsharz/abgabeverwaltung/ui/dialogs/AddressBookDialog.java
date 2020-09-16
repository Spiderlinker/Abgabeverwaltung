package de.hsharz.abgabeverwaltung.ui.dialogs;

import javafx.scene.layout.StackPane;

public class AddressBookDialog extends AbstractDialog {

    private AddressBookDialogView addressBookDialogView;

    public AddressBookDialog(StackPane parent) {
        super(parent, DialogTransition.TOP);

        createWidgets();
        setupInteractions();
        enableCloseOnEscape();
    }

    private void createWidgets() {
        addressBookDialogView = new AddressBookDialogView();
        setContent(addressBookDialogView.getPane());
    }

    private void setupInteractions() {
        addressBookDialogView.btnClose.setOnAction(e -> close());
    }

}
