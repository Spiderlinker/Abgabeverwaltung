package de.hsharz.abgabeverwaltung.model;

import java.io.File;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task {

    private StringProperty            name                  = new SimpleStringProperty("DefaultName");
    private StringProperty            description           = new SimpleStringProperty();
    private SimpleListProperty<File>  attachments           = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty            customSubmissionTitle = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dueDate               = new SimpleObjectProperty<>();
    private BooleanProperty           isFinished            = new SimpleBooleanProperty();

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
            this.addAttachment(attachment);
        }
    }

    private void addAttachment(final File attachment) {
        Objects.requireNonNull(attachment);
        if (!this.attachments.contains(attachment)) {
            this.attachments.add(attachment);
        }
    }

    public void removeAttachment(final File attachment) {
        this.attachments.remove(attachment);
    }

    public ObservableList<File> getAttachments() {
        return this.attachments.get();
    }

    public SimpleListProperty<File> attachmentsProperty() {
        return this.attachments;
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
        return this.description;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public LocalDate getDueDate() {
        return this.dueDate.get();
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return this.dueDate;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return this.name.get().equals(task.name.get()) && this.description.getValueSafe().equals(task.description.getValueSafe())
                && this.customSubmissionTitle.getValueSafe().equals(task.customSubmissionTitle.getValueSafe())
                && new HashSet<>(this.attachments.get()).equals(new HashSet<>(task.attachments.get()))
                && Objects.equals(this.dueDate.get(), task.dueDate.get()) && Objects.equals(this.isFinished.get(), task.isFinished.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description, this.attachments, this.customSubmissionTitle, this.dueDate, this.isFinished);
    }

    @Override
    public String toString() {
        return "Task{" + "name=" + this.name + '}';
    }
}
