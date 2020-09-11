package de.hsharz.abgabeverwaltung.ui;

import de.hsharz.abgabeverwaltung.ModuleDatabase;
import de.hsharz.abgabeverwaltung.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.addresses.Gender;
import de.hsharz.abgabeverwaltung.addresses.Person;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TestMain extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        //        Task task = new Task("Test");
        //        Task task2 = new Task("Anderer Test-Task");
        //        task2.addAttachment(new File("C:\\Users\\Oliver\\Downloads\\test.txt"));
        //
        //        TaskView ui = new TaskView(task);
        //        TaskView ui2 = new TaskView(task2);
        //
        //        HBox box = new HBox(20);
        //        box.setMaxHeight(250);
        //        box.setPadding(new Insets(20));
        //        HBox box2 = new HBox(20);
        //        box2.setPadding(new Insets(20));
        //
        //        Accordion acc = new Accordion();
        //
        //        TitledPane pane1 = new TitledPane("", box);
        //        TitledPane pane2 = new TitledPane("Mobile Applikationen", box2);
        //
        //        HBox boxPane1 = new HBox(20);
        //        boxPane1.setMaxWidth(Double.MAX_VALUE);
        //        Region region = new Region();
        //        boxPane1.setAlignment(Pos.CENTER_LEFT);
        //        HBox.setHgrow(region, Priority.ALWAYS);
        //        JFXButton btnAdd = new JFXButton("Aufgabe hinzufügen");
        //        boxPane1.getChildren().addAll(new Label("Sicherheit"), region, btnAdd);
        //        pane1.setGraphic(boxPane1);
        //        boxPane1.setStyle("-fx-background-color: red;");
        //        pane1.setMaxWidth(Double.MAX_VALUE);
        //
        //        pane1.setMaxHeight(50);
        //        acc.setMaxHeight(200);
        //
        //        acc.getPanes().add(pane1);
        //        acc.getPanes().add(pane2);
        //
        //        box.getChildren().add(ui.getPane());
        //        box.getChildren().add(ui2.getPane());
        //        box2.getChildren().add(new TaskDetailView(task2).getPane());


        AddressBook.addContacts(new Person("Lindemann", "u33873@hs-harz.de", Gender.MALE));

//        MainPane pane = new MainPane(new Semester());

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
        scene.getStylesheets().add(this.getClass().getResource("/style/List.css").toExternalForm());
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
