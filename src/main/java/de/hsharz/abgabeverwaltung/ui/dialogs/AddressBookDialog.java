package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class AddressBookDialog extends JFXDialog {

    private AddressBookDialogView addressBookDialogView;

    public AddressBookDialog(StackPane parent) {
        super(parent, null, DialogTransition.TOP);

        createWidgets();
        setupInteractions();
    }

    private void createWidgets() {
        addressBookDialogView = new AddressBookDialogView();
        setContent(addressBookDialogView.getPane());
    }

    private void setupInteractions() {

        getDialogContainer().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
        addressBookDialogView.btnClose.setOnAction(e -> close());
    }

}
