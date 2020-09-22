package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.util.HashMap;
import java.util.Map;

import com.jfoenix.controls.JFXDialog;

import javafx.scene.layout.StackPane;

public class DialogCache {

    private static Map<DialogType, JFXDialog> dialogs = new HashMap<>();

    public static void init(final StackPane parent) {
        dialogs.put(DialogType.SETTINGS, new SettingsDialog(parent));
        dialogs.put(DialogType.ADDRESS_BOOK, new AddressBookDialog(parent));
    }

    private DialogCache() {
        // Utility class
    }

    public static JFXDialog getDialog(final DialogType type) {
        return dialogs.get(type);
    }

    public enum DialogType {
        TASK_VIEW, TASK_SUBMIT, MODULE_VIEW, SETTINGS, ADDRESS_BOOK
    }

}
