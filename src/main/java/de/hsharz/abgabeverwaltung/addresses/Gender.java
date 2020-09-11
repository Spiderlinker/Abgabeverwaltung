package de.hsharz.abgabeverwaltung.addresses;

public enum Gender {

    MALE("Herr"), FEMALE("Frau"), DIVERS("");

    private String asString;

     Gender(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
