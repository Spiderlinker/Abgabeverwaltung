package de.hsharz.abgabeverwaltung;

import de.hsharz.abgabeverwaltung.model.Module;
import de.hsharz.abgabeverwaltung.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.hsharz.abgabeverwaltung.model.addresses.Gender;
import de.hsharz.abgabeverwaltung.model.addresses.Person;

import java.io.*;
import java.time.LocalDate;

class ModuleTest {

    private Module testObject;

    @BeforeEach
    public void setup() {
        this.testObject = new Module("TestModule");
    }

    @Test
    void getNameNullFail() {
        Assertions.assertThrows(NullPointerException.class, () -> this.testObject.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void setNameEmptyFail(final String name) {
        Assertions.assertThrows(NullPointerException.class, () -> this.testObject.setName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Test", "Semester #1" })
    void getName(final String name) {
        this.testObject.setName(name);
        Assertions.assertEquals(name, this.testObject.getName());
    }

    @Test
    void getProfessorEmpty() {
        Assertions.assertNotNull(this.testObject.getProfessors());
        Assertions.assertTrue(this.testObject.getProfessors().isEmpty());
    }

    @Test
    void addProfessor() {
        Assertions.assertTrue(this.testObject.getProfessors().isEmpty());

        Person p = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);
        Person p2 = new Person("Prof#2", "prof2@hs-harz.de", Gender.FEMALE);

        this.testObject.addProfessors(p);
        Assertions.assertEquals(1, this.testObject.getProfessors().size());
        this.testObject.addProfessors(p2);
        Assertions.assertEquals(2, this.testObject.getProfessors().size());
        this.testObject.addProfessors(p2);
        Assertions.assertEquals(2, this.testObject.getProfessors().size());
    }

    @Test
    void addProfessorNull() {
        Assertions.assertThrows(NullPointerException.class, () -> this.testObject.addProfessors((Person[]) null));
    }

    @Test
    void removeProfessor() {
        Assertions.assertTrue(this.testObject.getProfessors().isEmpty());

        Person p = new Person("Prof#1", "prof1@hs-harz.de", Gender.MALE);
        Person p2 = new Person("Prof#2", "prof2@hs-harz.de", Gender.FEMALE);

        this.testObject.addProfessors(p);
        Assertions.assertEquals(1, this.testObject.getProfessors().size());
        this.testObject.addProfessors(p2);
        Assertions.assertEquals(2, this.testObject.getProfessors().size());

        this.testObject.removeProfessor(p);
        Assertions.assertEquals(1, this.testObject.getProfessors().size());
        this.testObject.removeProfessor(p);
        Assertions.assertEquals(1, this.testObject.getProfessors().size());
        this.testObject.removeProfessor(p2);
        Assertions.assertEquals(0, this.testObject.getProfessors().size());
    }

    @Test
    void getTasksEmpty() {
        Assertions.assertNotNull(this.testObject.getTasks());
        Assertions.assertTrue(this.testObject.getTasks().isEmpty());
    }

    @Test
    void addTask() {
        Assertions.assertTrue(this.testObject.getTasks().isEmpty());

        Task mod = new Task("Task1");
        Task mod2 = new Task("Task2");

        this.testObject.addTask(mod);
        Assertions.assertEquals(1, this.testObject.getTasks().size());
        this.testObject.addTask(mod2);
        Assertions.assertEquals(2, this.testObject.getTasks().size());
        this.testObject.addTask(mod2);
        Assertions.assertEquals(2, this.testObject.getTasks().size());
    }

    @Test
    void addTaskNull() {
        Assertions.assertThrows(NullPointerException.class, () -> this.testObject.addTask(null));
    }

    @Test
    void removeTask() {
        Assertions.assertTrue(this.testObject.getTasks().isEmpty());

        Task mod = new Task("Task1");
        Task mod2 = new Task("Task2");

        this.testObject.addTask(mod);
        Assertions.assertEquals(1, this.testObject.getTasks().size());
        this.testObject.addTask(mod2);
        Assertions.assertEquals(2, this.testObject.getTasks().size());

        this.testObject.removeTask(mod);
        Assertions.assertEquals(1, this.testObject.getTasks().size());
        this.testObject.removeTask(mod);
        Assertions.assertEquals(1, this.testObject.getTasks().size());
        this.testObject.removeTask(mod2);
        Assertions.assertEquals(0, this.testObject.getTasks().size());
    }

    @Test
    void writeReadFile(@TempDir File tempDir) throws IOException, ClassNotFoundException {
        File tempFile = new File(tempDir, "module.test");
        System.out.println("Saving Module to test file: " + tempFile);

        Task task = new Task("Task1");
        Task task2 = new Task("Task2");

        task.setDescription("Sample Description");
        task.setCustomSubmissionTitle("Submission Title");
        task.setDueDate(LocalDate.now());
        task.addAttachments(new File("C:\\Users\\test.txt"), new File("/some/other/file.extension"));

        Person person = new Person("Prof#2", "prof2@hs-harz.de", Gender.FEMALE);
        Person person2 = new Person("Prof#3", "profasdf@hs-harz.de", Gender.DIVERS);

        testObject.addProfessors(person, person2);
        testObject.addTask(task);
        testObject.addTask(task2);

        try (FileOutputStream stream = new FileOutputStream(tempFile);
             ObjectOutputStream output = new ObjectOutputStream(stream)) {
            output.writeObject(testObject);
        }

        Module readTask = null;
        try (FileInputStream inputStream = new FileInputStream(tempFile);
             ObjectInputStream input = new ObjectInputStream(inputStream)) {
            readTask = (Module) input.readObject();
        }

        Assertions.assertNotNull(readTask);

        Assertions.assertEquals(testObject.getName(), readTask.getName());
        Assertions.assertEquals(testObject.getProfessors().size(), readTask.getProfessors().size());
        for (Person p : readTask.getProfessors()) {
            Assertions.assertTrue(testObject.getProfessors().contains(p));
        }

        Assertions.assertEquals(testObject.getTasks().size(), readTask.getTasks().size());
        for (Task t : readTask.getTasks()) {
            System.out.println("Checking task " + t.getName());
            Assertions.assertTrue(testObject.getTasks().contains(t));
        }

    }

}
