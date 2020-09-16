package de.hsharz.abgabeverwaltung.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hsharz.abgabeverwaltung.model.addresses.Person;
import de.spiderlinker.utils.StringUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Module implements Externalizable {

    private StringProperty         name       = new SimpleStringProperty("DefaultName");
    private ObjectProperty<Person> professor = new SimpleObjectProperty<>();
    private ObservableList<Task>   tasks      = FXCollections.observableArrayList();

    public Module() {
        // Used for Externalizable
    }

    public Module(final String name) {
        this.setName(name);
    }

    public Module(final String name, final Person prof) {
        this.setName(name);
        this.setProfessor(prof);
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

//    public void addProfessors(final Person... profs) {
//        Objects.requireNonNull(profs);
//        for (Person prof : profs) {
//            this.addProfessor(prof);
//        }
//    }
//
//    private void addProfessor(final Person prof) {
//        Objects.requireNonNull(prof);
//        if (!this.professors.contains(prof)) {
//            this.professors.add(prof);
//        }
//    }
//
//    public void removeProfessor(final Person p) {
//        this.professors.remove(p);
//    }

    public void setProfessor(Person person) {
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

    @Override
    public String toString() {
        return "Module{" +
                "name=" + name +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name.get());
        out.writeObject(professor.get());
        out.writeObject(new ArrayList<>(tasks));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name.set(in.readUTF());
        professor.set((Person)in.readObject());
        tasks.addAll((List<Task>)in.readObject());
    }
}
