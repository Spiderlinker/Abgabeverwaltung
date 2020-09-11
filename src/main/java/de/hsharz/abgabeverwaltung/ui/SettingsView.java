package de.hsharz.abgabeverwaltung.ui;

import com.jfoenix.controls.*;
import de.hsharz.abgabeverwaltung.Config;
import de.spiderlinker.utils.StringUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class SettingsView {

    private File configurationFile;

    private StackPane parent;
    private JFXDialog dialog;
    private GridPane root;

    private Label lblTitle;

    private JFXTextField fldEmail;
    private JFXPasswordField fldPassword;
    private JFXCheckBox boxSendBccToMyself;

    private Button btnOpenConfigurationFile;
    private Button btnCancel;
    private Button btnSave;

    Properties properties = new Properties();

    public SettingsView(StackPane parent) {
        this.parent = parent;

        createWidgets();
        setupInteractions();
        addWidgets();

        loadConfiguration();
    }

    private void createWidgets() {
        root = new GridPane();
        root.getStylesheets().add(this.getClass().getResource("/style/dialog/SettingsDialog.css").toExternalForm());
        root.getStyleClass().add("root");
        root.setPrefSize(500, 400);
        setColumnWidths(root, 50, 50);

        lblTitle = new Label("Settings");

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

        dialog = new JFXDialog(parent, root, JFXDialog.DialogTransition.TOP);
    }


    private void setColumnWidths(GridPane root, int... columnWidths) {

        for (int columnWidth : columnWidths) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(columnWidth);
            root.getColumnConstraints().add(c);
        }

    }

    private void setupInteractions() {

        btnCancel.setOnAction(e -> dialog.close());
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

        btnSave.setOnAction(e -> {
            try {
                properties.put("mail.username", StringUtils.requireNonNullOrEmptyElse(fldEmail.getText(), ""));
                properties.put("mail.password", StringUtils.requireNonNullOrEmptyElse(fldPassword.getText(), ""));
                properties.put("mail.bcc", String.valueOf(boxSendBccToMyself.isSelected()));
                try (FileOutputStream output = new FileOutputStream(Config.EMAIL_CONFIGURATION_FILE)) {
                    properties.store(output, "Your Mail-Configuration");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    private void addWidgets() {
        root.add(lblTitle, 0, 0, 2, 1);
        root.add(fldEmail, 0, 1);
        root.add(fldPassword, 1, 1);
        root.add(boxSendBccToMyself, 0, 2, 2, 1);
        root.add(btnOpenConfigurationFile, 0, 3, 2, 1);
        root.add(btnCancel, 0, 4);
        root.add(btnSave, 1, 4);
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

        fldEmail.setText(properties.getProperty("mail.username"));
        fldPassword.setText(properties.getProperty("mail.password"));
        boxSendBccToMyself.setSelected(Boolean.parseBoolean(properties.getProperty("mail.bcc")));
    }

    public void show() {
        dialog.show();
    }

}
