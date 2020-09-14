package de.hsharz.abgabeverwaltung.model.addresses;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class AddressBook {

    private static ObservableList<Person> contacts = FXCollections.observableArrayList();

    private AddressBook() {
    }

    public static void addContacts(final Person... persons) {
        Objects.requireNonNull(persons);
        for (Person p : persons) {
            addContact(p);
        }
    }

    private static void addContact(final Person p) {
        Objects.requireNonNull(p);
        if (!contacts.contains(p)) {
            contacts.add(p);
        }
        contacts.sort(Comparator.comparing(Person::getLastname));
    }

    public static void removeContact(final Person p) {
        contacts.remove(p);
    }

    public static ObservableList<Person> getContacts() {
        return contacts;
    }

    public static void readFromFile(File file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            try {
                List<Person> persons = (List<Person>) input.readObject();
                persons.forEach(AddressBook::addContact);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(File file) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(new ArrayList<>(AddressBook.getContacts()));
        }
    }

}
