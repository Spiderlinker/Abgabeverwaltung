package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.Settings;
import de.spiderlinker.utils.StringUtils;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsDialog extends AbstractDialog {

    private SettingsDialogView settingsDialogView;

    public SettingsDialog(StackPane parent) {
        super(parent, DialogTransition.TOP);

        createWidgets();
        setupInteractions();
        enableCloseOnEscape();
        loadConfiguration();
    }

    private void createWidgets() {
        settingsDialogView = new SettingsDialogView();
        setContent(settingsDialogView.getPane());
    }

    private void setupInteractions() {
        settingsDialogView.btnCancel.setOnAction(e -> close());

        settingsDialogView.btnSave.setOnAction(e -> {
            Settings.getEmailSettings().put("mail.from", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldName.getText(), ""));
            Settings.getEmailSettings().put("mail.username", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldEmail.getText(), ""));
            Settings.getEmailSettings().put("mail.password", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldPassword.getText(), ""));
            Settings.getEmailSettings().put("mail.bcc", String.valueOf(settingsDialogView.boxSendBccToMyself.isSelected()));
            Settings.saveEmailProperties();
            close();
        });
    }

    private void loadConfiguration() {
        settingsDialogView.updateConfigurationSettings(Settings.getEmailSettings());
    }

}
