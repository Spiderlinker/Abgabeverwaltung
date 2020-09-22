package de.hsharz.abgabeverwaltung;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.hsharz.abgabeverwaltung.ui.dialogs.DialogCache;
import de.hsharz.abgabeverwaltung.ui.views.SemesterView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Abgabeverwaltung extends Application {

    private static final Gson gson = FxGson.coreBuilder().setPrettyPrinting().create();

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        boolean firstStartup = !Config.APPLICATION_FOLDER.exists();

        this.checkApplicationDirectory();
        this.checkRequiredFiles();

        this.loadConfigurationFiles();
        this.loadApplicationState();

        this.createUiAndShow(stage);

        if (firstStartup) {
            DialogCache.getDialog(DialogCache.DialogType.SETTINGS).show();
        }
    }

    private void checkApplicationDirectory() {
        Config.APPLICATION_FOLDER.mkdirs();
    }

    private void checkRequiredFiles() {
        if (!Config.ADDRESS_BOOK_FILE.exists()) {
            try {
                Files.copy(Settings.class.getResourceAsStream("/files/addressbook.db"), Config.ADDRESS_BOOK_FILE.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Config.EMAIL_TEMPLATE_FILE.exists()) {
            try {
                Files.copy(Settings.class.getResourceAsStream("/files/email_template.txt"), Config.EMAIL_TEMPLATE_FILE.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfigurationFiles() {
        Settings.reloadEmailProperties();
        Settings.reloadEmailServerProperties();
    }

    private void loadApplicationState() {
        Person[] persons = readFromFile(Config.ADDRESS_BOOK_FILE, Person[].class);
        if (persons != null) {
            AddressBook.addContacts(persons);
        }

        Module[] modules = readFromFile(Config.MODULES_FILE, Module[].class);
        if (modules != null) {
            ModuleDatabase.getInstance().addModules(modules);
        }
    }

    private static <T> T readFromFile(final File file, final Class<T> type) {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                return gson.fromJson(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void saveApplicationState() {
        writeToFile(Config.ADDRESS_BOOK_FILE, AddressBook.getContacts());
        writeToFile(Config.MODULES_FILE, ModuleDatabase.getInstance().getModules());
    }

    private static void writeToFile(final File file, final Object content) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(content, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUiAndShow(final Stage stage) {
        Scene scene = new Scene(new SemesterView().getPane(), 1400, 850);
        scene.getStylesheets().add(this.getClass().getResource("/style/Application.css").toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> saveApplicationState());

        stage.setTitle("Abgabeverwaltung");
        stage.show();
    }

}
