package de.hsharz.abgabeverwaltung;

import com.google.gson.Gson;
import de.hsharz.abgabeverwaltung.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.time.LocalDate;

class TaskTest {

    @TempDir
    File tempDir;

    private static Task testObject;


    @BeforeAll
    public static void setup() {
        testObject = new Task("TestTask#1");
        testObject.setDescription("Sample Description");
        testObject.setCustomSubmissionTitle("Submission Title");
        testObject.setDueDate(LocalDate.now());
        testObject.addAttachments(new File("C:\\Users\\test.txt"), new File("/some/other/file.extension"));
    }

    @Test
    void writeReadJson() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(testObject));

        Task task = gson.fromJson(gson.toJson(testObject), Task.class);
        System.out.println(task);

    }

    @Test
    void writeReadExternal() throws IOException, ClassNotFoundException {
        File saveToFile = new File(tempDir, "task.test");
        System.out.println("Save task to test file: " + saveToFile);
        try (FileOutputStream stream = new FileOutputStream(saveToFile);
             ObjectOutputStream output = new ObjectOutputStream(stream)) {
            output.writeObject(testObject);
        }

        Task readTask = null;
        try (FileInputStream inputStream = new FileInputStream(saveToFile);
        ObjectInputStream input = new ObjectInputStream(inputStream)) {
            readTask = (Task) input.readObject();
        }

        Assertions.assertNotNull(readTask);

        Assertions.assertEquals(testObject.getName(), readTask.getName());
        Assertions.assertEquals(testObject.getDueDate(), readTask.getDueDate());
        Assertions.assertEquals(testObject.getDescription(), readTask.getDescription());
        Assertions.assertEquals(testObject.getCustomSubmissionTitle(), readTask.getCustomSubmissionTitle());

        Assertions.assertEquals(testObject.getAttachments().size(), readTask.getAttachments().size());
        for (File attachment : readTask.getAttachments()) {
            Assertions.assertTrue(testObject.getAttachments().contains(attachment));
        }

    }

}