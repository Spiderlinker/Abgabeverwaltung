package de.hsharz.abgabeverwaltung.ui.dialogs;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.Language;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SettingsDialogView extends AbstractStyledView<GridPane> {

    private Label              lblTitle;

    protected JFXTextField     fldName;
    protected JFXTextField     fldEmail;
    protected JFXPasswordField fldPassword;
    protected JFXCheckBox      boxSendBccToMyself;

    private Button             btnOpenConfigurationFile;
    private Button             btnOpenEmailTemplate;
    protected Button           btnCancel;
    protected Button           btnSave;

    public SettingsDialogView() {
        super(new GridPane());

        this.initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/DefaultDialog.css";
    }

    @Override
    protected void createWidgets() {
        this.root.getStyleClass().add("root");
        this.root.setPrefSize(500, 400);
        LayoutUtils.setColumnWidths(this.root, 50, 50);

        this.lblTitle = new Label(Language.getString("Settings"));
        this.lblTitle.getStyleClass().add("title");

        this.fldName = new JFXTextField();
        this.fldName.setPromptText(Language.getString("YourName"));
        this.fldName.setLabelFloat(true);

        this.fldEmail = new JFXTextField();
        this.fldEmail.setPromptText(Language.getString("YourEmailAddress"));
        this.fldEmail.setLabelFloat(true);

        this.fldPassword = new JFXPasswordField();
        this.fldPassword.setPromptText(Language.getString("PasswordOfYourEmailAddress"));
        this.fldPassword.setLabelFloat(true);

        this.boxSendBccToMyself = new JFXCheckBox(Language.getString("SendCopyToBcc"));

        this.btnOpenConfigurationFile = new JFXButton(Language.getString("OpenEmailConfiguration"));
        this.btnOpenEmailTemplate = new JFXButton(Language.getString("OpenEmailTemplate"));

        this.btnCancel = new JFXButton(Language.getString("Cancel"));
        this.btnCancel.getStyleClass().add("cancel-button");

        this.btnSave = new JFXButton(Language.getString("SaveAndClose"));
        this.btnSave.getStyleClass().add("save-button");
    }

    @Override
    protected void setupInteractions() {
        this.btnOpenConfigurationFile.setOnAction(e -> this.openFile(Config.EMAIL_SERVER_CONFIGURATION_FILE));
        this.btnOpenEmailTemplate.setOnAction(e -> this.openFile(Config.EMAIL_TEMPLATE_FILE));
    }

    private void openFile(final File f) {
        try {
            Desktop.getDesktop().open(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void addWidgets() {
        this.root.add(this.lblTitle, 0, 0, 2, 1);
        this.root.add(this.fldName, 0, 1, 2, 1);
        this.root.add(this.fldEmail, 0, 2);
        this.root.add(this.fldPassword, 1, 2);
        this.root.add(this.boxSendBccToMyself, 0, 3, 2, 1);
        this.root.add(this.btnOpenEmailTemplate, 0, 4);
        this.root.add(this.btnOpenConfigurationFile, 1, 4);
        this.root.add(this.btnCancel, 0, 5);
        this.root.add(this.btnSave, 1, 5);

        GridPane.setHalignment(this.lblTitle, HPos.CENTER);
        GridPane.setHalignment(this.btnSave, HPos.RIGHT);
    }

    protected void updateConfigurationSettings(final Properties properties) {
        this.fldName.setText(properties.getProperty("mail.from"));
        this.fldEmail.setText(properties.getProperty("mail.username"));
        this.fldPassword.setText(properties.getProperty("mail.password"));
        this.boxSendBccToMyself.setSelected(Boolean.parseBoolean(properties.getProperty("mail.bcc")));
    }

}
