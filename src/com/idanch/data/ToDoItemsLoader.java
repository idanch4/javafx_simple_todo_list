package com.idanch.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ToDoItemsLoader {
    private static ToDoItemsLoader instance = new ToDoItemsLoader();

    private String fileName = "items.txt";
    private ObservableList<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;

    private ToDoItemsLoader() {
        toDoItems = FXCollections.observableArrayList();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static ToDoItemsLoader getInstance() {
        return instance;
    }

    public void loadToDoItems() throws IOException {
        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(path)) {

            String data;
            while ((data = br.readLine()) != null) {
                String[] splittedData = data.split("\t");

                String title = splittedData[0];
                String description = splittedData[1];
                LocalDate due = LocalDate.parse(splittedData[2], formatter);

                toDoItems.add(new ToDoItem(title, description, due));
            }
        }
    }

    public void saveItemsToFile() throws IOException {
        Path path = Paths.get(fileName);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (ToDoItem item : toDoItems) {
                bw.write(String.format("%s\t%s\t%s",
                        item.getTitle(),
                        item.getDescription(),
                        formatter.format(item.getDue())));
                bw.newLine();
            }
        }
    }

    public ObservableList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void addNewItem(ToDoItem newItem) {
        if (newItem != null ) {
            toDoItems.add(newItem);
        }

    }

    public void deleteItem(ToDoItem item) {
        toDoItems.remove(item);
    }
}
