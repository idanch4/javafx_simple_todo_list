package com.idanch.data;

import java.time.LocalDate;

public class ToDoItem {
    private String title;
    private String description;
    private LocalDate due;
    private boolean isDone = false;

    public ToDoItem(String title, String description, LocalDate due) {
        this.title = title;
        this.description = description;
        this.due = due;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDoItem toDoItem = (ToDoItem) o;

        if (isDone != toDoItem.isDone) return false;
        if (!title.equals(toDoItem.title)) return false;
        if (!description.equals(toDoItem.description)) return false;
        return due.equals(toDoItem.due);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + due.hashCode();
        result = 31 * result + (isDone ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return title;
    }
}
