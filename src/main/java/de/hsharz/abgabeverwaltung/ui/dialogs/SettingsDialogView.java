package de.hsharz.abgabeverwaltung.ui.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.hsharz.abgabeverwaltung.Config;
import de.hsharz.abgabeverwaltung.ui.utils.AbstractStyledView;
import de.hsharz.abgabeverwaltung.ui.utils.LayoutUtils;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class SettingsDialogView extends AbstractStyledView<GridPane> {

    private Label lblTitle;

    protected JFXTextField fldName;
    protected JFXTextField fldEmail;
    protected JFXPasswordField fldPassword;
    protected JFXCheckBox boxSendBccToMyself;

    private Button btnOpenConfigurationFile;
    protected Button btnCancel;
    protected Button btnSave;

    public SettingsDialogView() {
        super(new GridPane());

        initializeView();
    }

    @Override
    protected String getStylesheet() {
        return "/style/dialog/SettingsDialog.css";
    }

    @Override
    protected void createWidgets() {
        root.getStyleClass().add("root");
        root.setPrefSize(500, 400);
        LayoutUtils.setColumnWidths(root, 50, 50);

        lblTitle = new Label("Settings");
        lblTitle.getStyleClass().add("title");

        fldName = new JFXTextField();
        fldName.setPromptText("Your Name");
        fldName.setLabelFloat(true);

        fldEmail = new JFXTextField();
        fldEmail.setPromptText("Your E-Mail-Address");
        fldEmail.setLabelFloat(true);

        fldPassword = new JFXPasswordField();
        fldPassword.setPromptText("Password of your E-Mail-Address");
        fldPassword.setLabelFloat(true);

        boxSendBccToMyself = new JFXCheckBox("Send a Copy (BCC) of every submission to my E-Mail-Address");

        btnOpenConfigurationFile = new JFXButton("Open E-Mail Configuration File");
        btnCancel = new JFXButton("Cancel");
        btnSave = new JFXButton("Save & Close");
    }

    @Override
    protected void setupInteractions() {
        btnOpenConfigurationFile.setOnAction(e -> {
            try {
                if (!Config.EMAIL_SERVER_CONFIGURATION_FILE.exists()) {
                    Files.copy(getClass().getResourceAsStream("/mail/email_server.configuration"), Config.EMAIL_SERVER_CONFIGURATION_FILE.toPath());
                }
                Desktop.getDesktop().open(Config.EMAIL_SERVER_CONFIGURATION_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    protected void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldName, 0, 1, 2, 1);
        root.add(fldEmail, 0, 2);
        root.add(fldPassword, 1, 2);
        root.add(boxSendBccToMyself, 0, 3, 2, 1);
        root.add(btnOpenConfigurationFile, 0, 4, 2, 1);
        root.add(btnCancel, 0, 5);
        root.add(btnSave, 1, 5);

        GridPane.setHalignment(lblTitle, HPos.CENTER);
        GridPane.setHalignment(btnSave, HPos.RIGHT);
    }

    protected void updateConfigurationSettings(Properties properties) {
        fldName.setText(properties.getProperty("mail.from"));
        fldEmail.setText(properties.getProperty("mail.username"));
        fldPassword.setText(properties.getProperty("mail.password"));
        boxSendBccToMyself.setSelected(Boolean.parseBoolean(properties.getProperty("mail.bcc")));
    }

}
