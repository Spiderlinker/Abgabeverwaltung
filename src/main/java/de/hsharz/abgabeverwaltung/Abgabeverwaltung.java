package de.hsharz.abgabeverwaltung;

import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.views.SemesterView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.nio.file.Files;

public class Abgabeverwaltung extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Config.APPLICATION_FOLDER.mkdirs();

        if(!Config.EMAIL_SERVER_CONFIGURATION_FILE.exists()) {
            Files.copy(getClass().getResourceAsStream("/mail/email_server.configuration"), Config.EMAIL_SERVER_CONFIGURATION_FILE.toPath());
        }
        if(!Config.EMAIL_CONFIGURATION_FILE.exists()) {
            Files.copy(getClass().getResourceAsStream("/mail/email.configuration"), Config.EMAIL_CONFIGURATION_FILE.toPath());
        }

        if (Config.ADDRESS_BOOK_FILE.exists()) {
            try {
                AddressBook.readFromFile(Config.ADDRESS_BOOK_FILE);
            } catch (InvalidClassException e) {
                Config.ADDRESS_BOOK_FILE.renameTo(new File(Config.APPLICATION_FOLDER, "*INVALID* AddressBook.db"));
            }
        }
        if (Config.MODULES_FILE.exists()) {
            try {
                ModuleDatabase.getInstance().loadFrom(Config.MODULES_FILE);
            } catch (InvalidClassException e) {
                Config.MODULES_FILE.renameTo(new File(Config.APPLICATION_FOLDER, "*INVALID* Modules.db"));
            }
        }

        Scene scene = new Scene(new SemesterView().getPane(), 1400, 850);
        scene.getStylesheets().add(this.getClass().getResource("/style/Application.css").toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try {
                ModuleDatabase.getInstance().saveTo(Config.MODULES_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                AddressBook.writeToFile(Config.ADDRESS_BOOK_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        stage.setTitle("Abgabeverwaltung");
        stage.show();
    }
}
