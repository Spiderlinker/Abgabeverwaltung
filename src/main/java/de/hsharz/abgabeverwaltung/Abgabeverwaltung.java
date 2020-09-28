package de.hsharz.abgabeverwaltung;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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

    private static final Gson gson         = FxGson.coreBuilder().setPrettyPrinting().create();

    private static boolean    firstStartup = !Config.APPLICATION_FOLDER.exists();

    public static void main(final String[] args) {
        checkApplicationDirectory();
        redirectOutputStream();
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        this.checkRequiredFiles();

        this.loadConfigurationFiles();
        this.loadApplicationState();

        this.createUiAndShow(stage);

        if (Abgabeverwaltung.firstStartup) {
            DialogCache.getDialog(DialogCache.DialogType.SETTINGS).show();
        }
    }

    private static void checkApplicationDirectory() {
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

    private static void redirectOutputStream() {
        try {
            System.setErr(new PrintStream(Config.ERROR_LOG_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.setOut(new PrintStream(Config.SYSTEM_LOG_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
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
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
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

        stage.setTitle(
                "Anwendung zur zeitlichen Erfassung semesterbegleitender Arbeiten, welche der Festigung der in den wenig lehrreichen Vorlesungen behandelten Themen dienen");
        stage.show();
    }

}
