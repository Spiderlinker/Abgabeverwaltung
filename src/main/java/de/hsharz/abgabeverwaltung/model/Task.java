package de.hsharz.abgabeverwaltung.model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task implements Externalizable {

    private StringProperty name = new SimpleStringProperty("DefaultName");
    private StringProperty description = new SimpleStringProperty();
    private SimpleListProperty<File> attachments = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty customSubmissionTitle = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private BooleanProperty isFinished = new SimpleBooleanProperty();

    public Task() {
        // Used for
    }

    public Task(final String name) {
        this.setName(name);
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public void addAttachments(final File... attachments) {
        Objects.requireNonNull(attachments);
        for (File attachment : attachments) {
            addAttachment(attachment);
        }
    }

    private void addAttachment(File attachment) {
        Objects.requireNonNull(attachment);
        if (!attachments.contains(attachment)) {
            this.attachments.add(attachment);
        }
    }

    public void removeAttachment(File attachment) {
        attachments.remove(attachment);
    }

    public ObservableList<File> getAttachments() {
        return this.attachments.get();
    }

    public SimpleListProperty<File> attachmentsProperty() {
        return attachments;
    }

    public void setCustomSubmissionTitle(final String customSubmissionTitle) {
        this.customSubmissionTitle.set(customSubmissionTitle);
    }

    public String getCustomSubmissionTitle() {
        return this.customSubmissionTitle.get();
    }

    public StringProperty customSubmissionTitleProperty() {
        return this.customSubmissionTitle;
    }

    public void setDescription(final String description) {
        this.description.set(description);
    }

    public String getDescription() {
        return this.description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public LocalDate getDueDate() {
        return this.dueDate.get();
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public void setFinished(final boolean isFinished) {
        this.isFinished.set(isFinished);
    }

    public boolean isFinished() {
        return this.isFinished.get();
    }

    public BooleanProperty isFinishedProperty() {
        return this.isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.get().equals(task.name.get()) &&
                description.getValueSafe().equals(task.description.getValueSafe()) &&
                customSubmissionTitle.getValueSafe().equals(task.customSubmissionTitle.getValueSafe()) &&
                new HashSet<>(attachments.get()).equals(new HashSet<>(task.attachments.get())) &&
                Objects.equals(dueDate.get(), task.dueDate.get()) &&
                Objects.equals(isFinished.get(), task.isFinished.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, attachments, customSubmissionTitle, dueDate, isFinished);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name=" + name +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name.get());
        out.writeUTF(description.getValueSafe());
        out.writeUTF(customSubmissionTitle.getValueSafe());
        out.writeObject(dueDate.get());
        out.writeBoolean(isFinished.get());

        out.writeObject(new ArrayList<>(attachments.get()));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name.set(in.readUTF());
        description.set(in.readUTF());
        customSubmissionTitle.set(in.readUTF());
        dueDate.set((LocalDate) in.readObject());
        isFinished.set(in.readBoolean());

        attachments.addAll((List<File>) in.readObject());
    }
}
