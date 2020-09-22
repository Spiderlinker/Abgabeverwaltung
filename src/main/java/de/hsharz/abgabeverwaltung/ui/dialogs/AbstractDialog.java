package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public abstract class AbstractDialog extends JFXDialog {

    public AbstractDialog(final StackPane parent, final DialogTransition transition) {
        super(parent, null, transition);
    }

    protected void enableCloseOnEscape() {
        this.getDialogContainer().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                this.close();
            }
        });
    }

}
