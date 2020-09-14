package de.hsharz.abgabeverwaltung.model.addresses;

public enum Gender {

    MALE("Herr"), FEMALE("Frau"), DIVERS("Divers");

    private String asString;

     Gender(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
