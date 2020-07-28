package com.idanch;

import com.idanch.data.ToDoItem;
import com.idanch.data.ToDoItemsLoader;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class NewItemDialogController {
    @FXML
    private TextField titleText;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker dueDate;

    @FXML
    public ToDoItem addNewItem() {
        String title = titleText.getText().trim();
        String description = descriptionTextArea.getText().trim();
        LocalDate dueDateVal = dueDate.getValue();

        ToDoItem newItem = new ToDoItem(title, description, dueDateVal);
        ToDoItemsLoader.getInstance().addNewItem(newItem);
        return newItem;
    }
}
