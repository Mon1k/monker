package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.table.TableCommander;
import sample.table.TableRowCommander;
import sample.window.About;
import sample.window.Editor;
import sample.window.Popup;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application
{
    public static final String VERSION = "Monker v1.00.000.0001";

    public ArrayList<TableCommander> panels;

    private VBox root;

    @Override
    public void start(Stage stage) throws Exception
    {
        root = new VBox();

        initMenu();
        initTable();
        initPanelBottom();

        Scene scene = new Scene(root, 1200, 600);
        stage.setScene(scene);
        stage.setTitle(VERSION);
        stage.show();
    }

    private void initMenu()
    {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction(actionEvent -> {
            Editor editor = new Editor();
            if (getCurrentPanel().getCurrentRow() != null) {
                editor.setFile(getCurrentPanel().getCurrentRow().getFile());
            }
            editor.show();
        });
        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.F3));
        openItem.setOnAction(actionEvent -> {
            Editor editor = new Editor();
            if (getCurrentPanel().getCurrentRow() != null) {
                editor.setFile(getCurrentPanel().getCurrentRow().getFile());
                editor.open();
            }
            editor.show();
        });
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> Platform.exit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(newItem, openItem, seperator, exitItem);
        menuBar.getMenus().add(fileMenu);

        Menu commandMenu = new Menu("Command");
        MenuItem commandItemMenuItem = new MenuItem("Reload");
        commandItemMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        commandItemMenuItem.setOnAction(actionEvent -> {
            getCurrentPanel().refresh();
        });
        commandMenu.getItems().addAll(commandItemMenuItem);
        menuBar.getMenus().add(commandMenu);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenu.getItems().addAll(aboutItemMenuItem);
        menuBar.getMenus().add(aboutMenu);
        aboutItemMenuItem.setOnAction(actionEvent -> {
            new About().show();
        });
        root.getChildren().add(menuBar);
    }

    private void initTable()
    {
        panels = new ArrayList<>();
        TableCommander left = new TableCommander();
        left.getTable().getSelectionModel().select(0);
        panels.add(left);
        TableCommander right = new TableCommander();
        panels.add(right);

        HBox hBox = new HBox();

        VBox leftVBox = new VBox();
        leftVBox.getChildren().add(left.getPath());
        leftVBox.getChildren().add(left.getTable());
        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        hBox.getChildren().add(leftVBox);
        VBox.setVgrow(left.getTable(), Priority.ALWAYS);

        VBox rightVBox = new VBox();
        rightVBox.getChildren().add(right.getPath());
        rightVBox.getChildren().add(right.getTable());
        HBox.setHgrow(rightVBox, Priority.ALWAYS);
        hBox.getChildren().add(rightVBox);
        VBox.setVgrow(right.getTable(), Priority.ALWAYS);

        VBox.setVgrow(hBox, Priority.ALWAYS);
        root.getChildren().add(hBox);
    }

    public void initPanelBottom()
    {
        Button f1 = new Button("F1");
        f1.setOnAction(actionEvent -> {
            new About().show();
        });
        Button f3 = new Button("F3");
        f3.setOnAction(actionEvent -> {
            Editor editor = new Editor();
            editor.setFile(getCurrentPanel().getCurrentRow().getFile());
            editor.open();
            editor.show();
        });
        Button f9 = new Button("F9");
        f9.setOnAction(actionEvent -> {
            TableCommander tableCommander = this.getCurrentPanel();
            TableView<TableRowCommander> table = tableCommander.getTable();
            TableRowCommander rowCommander = table.getSelectionModel().getSelectedItem();
            if (rowCommander.getFile().isDirectory()) {
                new Popup().show("Directory is not delete, temporaly");
            } else if (rowCommander.getFile().isFile()) {
                File file = rowCommander.getFile();
                file.delete();
                tableCommander.refresh();
            }
        });
        Button f10 = new Button("F10");
        f10.setOnAction(actionEvent -> Platform.exit());

        f1.setPrefWidth(1000000);
        f3.setPrefWidth(1000000);
        f9.setPrefWidth(1000000);
        f10.setPrefWidth(1000000);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(f1, f3, f9, f10);
        HBox.setHgrow(f1, Priority.ALWAYS);
        HBox.setHgrow(f3, Priority.ALWAYS);
        HBox.setHgrow(f9, Priority.ALWAYS);
        HBox.setHgrow(f10, Priority.ALWAYS);
        root.getChildren().add(hBox);
    }

    public TableCommander getCurrentPanel()
    {
        for (TableCommander table : panels) {
            if (table.getTable().getSelectionModel().getSelectedItem() != null) {
                return table;
            }
        }

        return null;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
