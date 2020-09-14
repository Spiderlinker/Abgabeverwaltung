package de.hsharz.abgabeverwaltung;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.ModuleDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ModuleDatabaseTest {

    private ModuleDatabase objectUnderTest = ModuleDatabase.getInstance();

    @BeforeEach
    public void setup() {
        this.objectUnderTest.nameProperty().set(null);
        this.objectUnderTest.getModules().clear();
    }

    @Test
    void getNameNullFail() {
        Assertions.assertThrows(NullPointerException.class, () -> this.objectUnderTest.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void setNameEmptyFail(final String name) {
        Assertions.assertThrows(NullPointerException.class, () -> this.objectUnderTest.setName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Test", "Semester #1" })
    void getName(final String name) {
        Assertions.assertNull(this.objectUnderTest.getName());
        this.objectUnderTest.setName(name);
        Assertions.assertEquals(name, this.objectUnderTest.getName());
    }

    @Test
    void getModulesEmpty() {
        Assertions.assertNotNull(this.objectUnderTest.getModules());
        Assertions.assertTrue(this.objectUnderTest.getModules().isEmpty());
    }

    @Test
    void addModule() {
        Assertions.assertTrue(this.objectUnderTest.getModules().isEmpty());

        Module mod = new Module("Module1");
        Module mod2 = new Module("Module2");

        this.objectUnderTest.addModule(mod);
        Assertions.assertEquals(1, this.objectUnderTest.getModules().size());
        this.objectUnderTest.addModule(mod2);
        Assertions.assertEquals(2, this.objectUnderTest.getModules().size());
        this.objectUnderTest.addModule(mod2);
        Assertions.assertEquals(2, this.objectUnderTest.getModules().size());
    }

    @Test
    void addModuleNull() {
        Assertions.assertThrows(NullPointerException.class, () -> this.objectUnderTest.addModule(null));
    }

    @Test
    void removeModule() {
        Assertions.assertTrue(this.objectUnderTest.getModules().isEmpty());

        Module mod = new Module("Module1");
        Module mod2 = new Module("Module2");

        this.objectUnderTest.addModule(mod);
        Assertions.assertEquals(1, this.objectUnderTest.getModules().size());
        this.objectUnderTest.addModule(mod2);
        Assertions.assertEquals(2, this.objectUnderTest.getModules().size());

        this.objectUnderTest.removeModule(mod);
        Assertions.assertEquals(1, this.objectUnderTest.getModules().size());
        this.objectUnderTest.removeModule(mod);
        Assertions.assertEquals(1, this.objectUnderTest.getModules().size());
        this.objectUnderTest.removeModule(mod2);
        Assertions.assertEquals(0, this.objectUnderTest.getModules().size());

    }

}
