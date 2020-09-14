package de.hsharz.abgabeverwaltung.model;

import de.spiderlinker.utils.StringUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleDatabase {

    private static final ModuleDatabase INSTANCE = new ModuleDatabase();

    private StringProperty name = new SimpleStringProperty();
    private ObservableList<Module> modules = FXCollections.observableArrayList();

    private ModuleDatabase() {
    }

    public static ModuleDatabase getInstance() {
        return INSTANCE;
    }

    public void setName(final String name) {
        this.name.set(StringUtils.requireNonNullOrEmpty(name));
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public void addModule(final Module module) {
        Objects.requireNonNull(module);
//        if (!this.modules.contains(module)) {
        this.modules.add(module);
//        }
    }

    public void removeModule(final Module module) {
        this.modules.remove(module);
    }

    public ObservableList<Module> getModules() {
        return this.modules;
    }

    public void loadFrom(File file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            this.modules.addAll((List<Module>) input.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveTo(File file) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(new ArrayList<>(modules));
        }
    }

}
