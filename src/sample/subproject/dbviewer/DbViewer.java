package sample.subproject.dbviewer;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.subproject.dbviewer.ui.Database;
import sample.subproject.dbviewer.ui.Query;
import sample.subproject.dbviewer.ui.Table;
import sample.window.Popup;

import java.util.ArrayList;

public class DbViewer
{
    Stage stage;
    FlowPane root;

    String database = "test";
    sample.subproject.dbviewer.ui.tableview.Table tableView;

    public DbViewer()
    {
        stage = new Stage();
        root = new FlowPane();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction(actionEvent -> {
            Database db = new Database();
            db.setOnCloseRequest(windowEvent -> database = db.getDatabase());
        });
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> commandExit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(newItem, seperator, exitItem);

        Menu toolMenu = new Menu("Tool");
        MenuItem listDatabaseItem = new MenuItem("List datatables");
        listDatabaseItem.setOnAction(actionEvent -> {});
        MenuItem listTablesItem = new MenuItem("List tables");
        listTablesItem.setOnAction(actionEvent -> commandTableList());
        MenuItem newTableItem = new MenuItem("New table");
        newTableItem.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        newTableItem.setOnAction(actionEvent -> {
            if (this.database.length() > 0) {
                new Table(this.database);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Empty database");
                alert.showAndWait();
            }
        });
        toolMenu.getItems().addAll(listDatabaseItem, listTablesItem, newTableItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutItemMenuItem.setOnAction(actionEvent -> new Popup().show("DB viewer"));
        aboutMenu.getItems().add(aboutItemMenuItem);

        menuBar.getMenus().addAll(fileMenu, toolMenu, aboutMenu);

        root.getChildren().addAll(menuBar);

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("DbView");
        stage.show();
        stage.setOnCloseRequest(windowEvent -> commandExit());
    }

    public void commandExit()
    {
        System.out.println("exit dbviewer");
        stage.hide();
    }

    public void commandTableList()
    {
        Query query = new Query();
        ArrayList<String> rows = query.query(database, "SELECT name FROM sqlite_master WHERE type='table'");
        tableView = new sample.subproject.dbviewer.ui.tableview.Table(rows);
    }
}
