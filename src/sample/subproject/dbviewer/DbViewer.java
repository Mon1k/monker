package sample.subproject.dbviewer;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.window.Popup;

public class DbViewer
{
    Stage stage;
    FlowPane root;

    public DbViewer()
    {
        stage = new Stage();
        root = new FlowPane();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> commandExit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(newItem, seperator, exitItem);

        Menu toolMenu = new Menu("Tool");
        MenuItem newTableItem = new MenuItem("New table");
        newTableItem.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        toolMenu.getItems().addAll(newTableItem);

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
    }

    public void commandExit()
    {
        System.out.println("exit dbviewer");
        stage.hide();
    }
}
