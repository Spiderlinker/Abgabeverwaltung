package de.hsharz.abgabeverwaltung.ui.utils;

import com.jfoenix.controls.JFXDialog;

public class SimpleDialog {

    protected JFXDialog dialog;

    public SimpleDialog() {

    }

    public void show() {
        dialog.show();
    }

    public void close() {
        dialog.close();
    }

}
