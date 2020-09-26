package de.hsharz.abgabeverwaltung.addresses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hsharz.abgabeverwaltung.model.addresses.AddressBook;
import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;

class AddressBookTest {

    @BeforeEach
    public void setup() {
        AddressBook.getContacts().clear();
    }

    @Test
    void getProfessorEmpty() {
        Assertions.assertNotNull(AddressBook.getContacts());
        Assertions.assertTrue(AddressBook.getContacts().isEmpty());
    }

    @Test
    void addProfessor() {
        Assertions.assertTrue(AddressBook.getContacts().isEmpty());

        Person p = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);
        Person p2 = new Person("Prof#2", "prof2@hs-harz.de", Gender.FEMALE);

        AddressBook.addContacts(p);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
        AddressBook.addContacts(p2);
        Assertions.assertEquals(2, AddressBook.getContacts().size());
        AddressBook.addContacts(p2);
        Assertions.assertEquals(2, AddressBook.getContacts().size());
    }

    @Test
    void addEqualProfessor() {
        Assertions.assertTrue(AddressBook.getContacts().isEmpty());

        Person p = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);
        Person pEqual = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);

        AddressBook.addContacts(p);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
        AddressBook.addContacts(pEqual);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
    }

    @Test
    void addProfessorNull() {
        Assertions.assertThrows(NullPointerException.class, () -> AddressBook.addContacts((Person[]) null));
    }

    @Test
    void removeProfessor() {
        Assertions.assertTrue(AddressBook.getContacts().isEmpty());

        Person p = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);
        Person p2 = new Person("Prof#2", "prof2@hs-harz.de", Gender.FEMALE);

        AddressBook.addContacts(p);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
        AddressBook.addContacts(p2);
        Assertions.assertEquals(2, AddressBook.getContacts().size());

        AddressBook.removeContact(p);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
        AddressBook.removeContact(p);
        Assertions.assertEquals(1, AddressBook.getContacts().size());
        AddressBook.removeContact(p2);
        Assertions.assertEquals(0, AddressBook.getContacts().size());
    }

    @Test
    public void getContactsEmpty() {
        Assertions.assertTrue(AddressBook.getContacts().isEmpty());
    }

}
