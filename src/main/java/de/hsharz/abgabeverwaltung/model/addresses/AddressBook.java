package de.hsharz.abgabeverwaltung.model.addresses;

import java.util.Comparator;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

}
