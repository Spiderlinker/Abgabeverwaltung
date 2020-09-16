package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class DialogCache {

    private static Map<DialogType, JFXDialog> dialogs = new HashMap<>();

    public static void init(StackPane parent) {
        dialogs.put(DialogType.SETTINGS, new SettingsDialog(parent));
        dialogs.put(DialogType.ADDRESS_BOOK, new AddressBookDialog(parent));
    }

    private DialogCache() {
        // Utility class
    }

    public static JFXDialog getDialog(DialogType type) {
        return dialogs.get(type);
    }

    public enum DialogType {
        TASK_VIEW, TASK_SUBMIT, MODULE_VIEW, SETTINGS, ADDRESS_BOOK;
    }

}

