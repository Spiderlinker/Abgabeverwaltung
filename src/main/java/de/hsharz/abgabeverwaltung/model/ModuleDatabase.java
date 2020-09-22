package de.hsharz.abgabeverwaltung.model;

import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModuleDatabase {

    private static final ModuleDatabase INSTANCE = new ModuleDatabase();

    private ObservableList<Module>      modules  = FXCollections.observableArrayList();

    private ModuleDatabase() {
    }

    public static ModuleDatabase getInstance() {
        return INSTANCE;
    }

    public void addModules(final Module... modules) {
        Objects.requireNonNull(modules);
        for (Module module : modules) {
            this.addModule(module);
        }
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

}
