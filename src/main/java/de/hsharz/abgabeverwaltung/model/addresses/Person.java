package de.hsharz.abgabeverwaltung.model.addresses;

import de.spiderlinker.utils.StringUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Person {

    private StringProperty lastname = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private ObjectProperty<Gender> gender = new SimpleObjectProperty<>();

    public Person(final String lastname, final String email, final Gender gender) {
        this.setLastname(lastname);
        this.setEmail(email);
        this.setGender(gender);
    }

    public void setLastname(final String lastname) {
        this.lastname.set(StringUtils.requireNonNullOrEmpty(lastname));
    }

    public String getLastname() {
        return this.lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public void setEmail(final String email) {
        this.email.set(StringUtils.requireNonNullOrEmpty(email));
    }

    public String getEmail() {
        return this.email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setGender(final Gender gender) {
        this.gender.set(Objects.requireNonNull(gender));
    }

    public Gender getGender() {
        return this.gender.get();
    }

    public ObjectProperty<Gender> genderProperty() {
        return gender;
    }

    @Override
    public String toString() {
        return this.gender.get() + " " + this.lastname.get() + " - " + this.email.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return lastname.get().equals(person.lastname.get()) &&
                email.get().equals(person.email.get()) &&
                gender.get().equals(person.gender.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastname, email, gender);
    }

}
