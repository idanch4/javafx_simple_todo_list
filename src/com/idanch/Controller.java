package com.idanch;

import com.idanch.data.ToDoItem;
import com.idanch.data.ToDoItemsLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class Controller {
    @FXML
    private ListView<ToDoItem> todoItemsListView;
    @FXML
    private TextArea todoItemsDescription;
    @FXML
    private Label dueDateLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ToggleButton todaysItemsToggle;
    @FXML
    private ContextMenu listContextMenu;

    private FilteredList<ToDoItem> filteredList;

    @FXML
    public void initialize() {

        SortedList<ToDoItem> sortedList = new SortedList<>(
                ToDoItemsLoader.getInstance().getToDoItems(),
                Comparator.comparing(ToDoItem::getDue));

        filteredList = new FilteredList<>(sortedList);
        todoItemsListView.setItems(filteredList);

        // Delete Item Menu
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem();
        deleteMenuItem.setText("Delete");
        deleteMenuItem.setOnAction((e) -> {
            showDeleteItemDialog(todoItemsListView.getSelectionModel().getSelectedItem());
        });



        //ListView settings
        todoItemsListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> listView) {
                ListCell<ToDoItem> listCell = new ListCell<>() {
                    @Override
                    protected void updateItem(ToDoItem toDoItem, boolean empty) {
                        super.updateItem(toDoItem, empty);
                        System.out.println("updateItem was called on " + (toDoItem != null ? toDoItem.getTitle() : "{}"));
                        if (empty) {
                            setText(null);
                        }else if (toDoItem != null){
                            setText(toDoItem.getTitle());
                            if (toDoItem.getDue().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);
                            } else if (toDoItem.getDue().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.GREENYELLOW);
                            }
                        }
                    }
                };

                listCell.emptyProperty().addListener((obs, wasEmpty, isEmpty) -> {
                    if (isEmpty) {
                        listCell.setContextMenu(null);
                    } else {
                        listCell.setContextMenu(listContextMenu);
                    }
                });

                return listCell;
            }
        });

        todoItemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoItemsListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null ) {
                ToDoItem selectedItem = todoItemsListView.getSelectionModel().getSelectedItem();
                todoItemsDescription.setText(selectedItem.getDescription());

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                dueDateLabel.setText("Due: " + dtf.format(selectedItem.getDue()));
            }
        });

        todoItemsListView.getSelectionModel().selectFirst();
    }

    @FXML
    public void showNewItemDialog() {
        String dialogContentFileName = "/fxml/newItemDialog.fxml";

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Add New ToDo Item");
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(dialogContentFileName));

        try {
            dialog.getDialogPane().setContent(loader.load());

        }catch (IOException ioe) {
            System.out.println("Failed to load dialog pane from " + dialogContentFileName);
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> response = dialog.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            NewItemDialogController controller = loader.getController();
            ToDoItem newItem = controller.addNewItem();
            todoItemsListView.getSelectionModel().select(newItem);

            System.out.println("new todo item has been added");
        }
    }

    @FXML
    public void handleKeyPress(KeyEvent e) {
        ToDoItem selectedItem = todoItemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (e.getCode().equals(KeyCode.DELETE) || e.getCode().equals(KeyCode.BACK_SPACE)) {
                showDeleteItemDialog(selectedItem);
            }
        }
    }

    @FXML
    public void toggleTodaysItems(ActionEvent e) {
        ToDoItem selectedItem = todoItemsListView.getSelectionModel().getSelectedItem();
        if (todaysItemsToggle.isSelected()) {
            filterTodaysItems();
            if (selectedItem != null && filteredList.contains(selectedItem)) {
                todoItemsListView.getSelectionModel().select(selectedItem);
            } else {
                todoItemsDescription.setText("");
                dueDateLabel.setText("");
            }
        } else {
            showAllItems();
        }
    }

    @FXML
    public void exitApp() {
        Platform.exit();
    }

    private void showDeleteItemDialog(ToDoItem selectedItem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete ToDo Item");
        alert.setContentText("Are you sure you want to delete this?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            ToDoItemsLoader.getInstance().deleteItem(selectedItem);
        }
    }

    private SortedList<ToDoItem> getSortedItems(ObservableList<ToDoItem> obsItems) {
        return new SortedList<>(
                obsItems,
                Comparator.comparing(ToDoItem::getDue));
    }

    private void filterTodaysItems() {
        filteredList.setPredicate(
                (item) -> item.getDue().isBefore(LocalDate.now().plusDays(1))
        );
    }

    private void showAllItems() {
        filteredList.setPredicate(
                (item) -> true
        );
    }
}
