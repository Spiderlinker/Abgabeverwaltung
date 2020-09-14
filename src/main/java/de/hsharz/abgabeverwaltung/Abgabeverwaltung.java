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

public class Abgabeverwaltung extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        String userHomeDirectory = System.getProperty("user.home");
        File appHome = new File(userHomeDirectory, "Abgabenverwaltung");
        File moduledb = new File(appHome, "modules.db");
        File persondb = new File(appHome, "addressBook.db");

        appHome.mkdirs();

        if (persondb.exists()) {
            AddressBook.readFromFile(persondb);
        }
        if (moduledb.exists()) {
            ModuleDatabase.getInstance().loadFrom(moduledb);
        }

        Scene scene = new Scene(new SemesterView().getPane(), 1400, 850);
        scene.getStylesheets().add(this.getClass().getResource("/style/Application.css").toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try {
                ModuleDatabase.getInstance().saveTo(moduledb);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                AddressBook.writeToFile(persondb);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        stage.setTitle("Abgabeverwaltung");
        stage.show();
    }
}
