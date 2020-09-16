package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXDialog;
import de.hsharz.abgabeverwaltung.Config;
import de.spiderlinker.utils.StringUtils;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsDialog extends AbstractDialog {

    private SettingsDialogView settingsDialogView;
    private Properties properties = new Properties();


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
            try {
                properties.put("mail.name", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldName.getText(), ""));
                properties.put("mail.username", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldEmail.getText(), ""));
                properties.put("mail.password", StringUtils.requireNonNullOrEmptyElse(settingsDialogView.fldPassword.getText(), ""));
                properties.put("mail.bcc", String.valueOf(settingsDialogView.boxSendBccToMyself.isSelected()));
                try (FileOutputStream output = new FileOutputStream(Config.EMAIL_CONFIGURATION_FILE)) {
                    properties.store(output, "Your Mail-Configuration");
                }
                close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }


    private void loadConfiguration() {
        try {
            if (!Config.EMAIL_CONFIGURATION_FILE.exists()) {
                Config.EMAIL_CONFIGURATION_FILE.createNewFile();
            }
            properties.load(new FileInputStream(Config.EMAIL_CONFIGURATION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingsDialogView.updateConfigurationSettings(properties);
    }

}
