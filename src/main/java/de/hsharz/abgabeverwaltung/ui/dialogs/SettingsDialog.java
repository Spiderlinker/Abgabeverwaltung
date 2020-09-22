package de.hsharz.abgabeverwaltung.ui.dialogs;

import de.hsharz.abgabeverwaltung.Settings;
import de.spiderlinker.utils.StringUtils;
import javafx.scene.layout.StackPane;

public class SettingsDialog extends AbstractDialog {

    private SettingsDialogView settingsDialogView;

    public SettingsDialog(final StackPane parent) {
        super(parent, DialogTransition.TOP);

        this.createWidgets();
        this.setupInteractions();
        this.enableCloseOnEscape();
        this.loadConfiguration();
    }

    private void createWidgets() {
        this.settingsDialogView = new SettingsDialogView();
        this.setContent(this.settingsDialogView.getPane());
    }

    private void setupInteractions() {
        this.settingsDialogView.btnCancel.setOnAction(e -> this.close());

        this.settingsDialogView.btnSave.setOnAction(e -> {
            Settings.getEmailSettings().put("mail.from", StringUtils.requireNonNullOrEmptyElse(this.settingsDialogView.fldName.getText(), ""));
            Settings.getEmailSettings().put("mail.username", StringUtils.requireNonNullOrEmptyElse(this.settingsDialogView.fldEmail.getText(), ""));
            Settings.getEmailSettings().put("mail.password",
                    StringUtils.requireNonNullOrEmptyElse(this.settingsDialogView.fldPassword.getText(), ""));
            Settings.getEmailSettings().put("mail.bcc", String.valueOf(this.settingsDialogView.boxSendBccToMyself.isSelected()));
            Settings.saveEmailProperties();
            this.close();
        });
    }

    private void loadConfiguration() {
        this.settingsDialogView.updateConfigurationSettings(Settings.getEmailSettings());
    }

}
