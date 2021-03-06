package de.hsharz.abgabeverwaltung.model;

import java.util.Objects;
import java.util.UUID;

import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.spiderlinker.utils.StringUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Module {

    private final UUID             uuid;
    private StringProperty         name      = new SimpleStringProperty("DefaultName");
    private ObjectProperty<Person> professor = new SimpleObjectProperty<>();
    private ObservableList<Task>   tasks     = FXCollections.observableArrayList();

    public Module(final String name) {
        this(name, null);
    }

    public Module(final String name, final Person prof) {
        this.setName(name);
        this.setProfessor(prof);

        this.uuid = UUID.randomUUID();
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

    public void setProfessor(final Person person) {
        this.professor.set(person);
    }

    public Person getProfessor() {
        return this.professor.get();
    }

    public ObjectProperty<Person> professorProperty() {
        return this.professor;
    }

    public void addTask(final Task task) {
        Objects.requireNonNull(task);
        //        if (!this.tasks.contains(task)) {
        this.tasks.add(task);
        //        }
    }

    public void removeTask(final Task task) {
        this.tasks.remove(task);
    }

    public ObservableList<Task> getTasks() {
        return this.tasks;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Module{" + "name=" + this.name + '}';
    }

}
