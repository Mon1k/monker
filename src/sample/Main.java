package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.file.FileExt;
import sample.file.FileUtils;
import sample.subproject.arcanoid.Arcanoid;
import sample.subproject.dbviewer.DbViewer;
import sample.table.TableCommander;
import sample.table.TableRowCommander;
import sample.tests.fileread.FileRead;
import sample.window.About;
import sample.window.Editor;
import sample.window.Popup;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application
{
    public static final String VERSION = "Monker v1.00.000.0001";

    public ArrayList<TableCommander> panels;

    private VBox root;

    @Override
    public void start(Stage stage)
    {
        root = new VBox();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(root, screenSize.width / 1.5, screenSize.height / 1.5);
        stage.setScene(scene);
        stage.setTitle(VERSION);

        initMenu();
        initTable();
        initContextMenu();
        initPanelBottom();

        stage.show();
        stage.setOnCloseRequest(windowEvent -> commandExit());
    }

    private void initMenu()
    {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction(actionEvent -> commandNew());
        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.F3));
        openItem.setOnAction(actionEvent -> commandOpen());
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> commandExit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(newItem, openItem, seperator, exitItem);
        menuBar.getMenus().add(fileMenu);

        Menu commandMenu = new Menu("Command");
        MenuItem commandItemMenuItem = new MenuItem("Reload");
        commandItemMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        commandItemMenuItem.setOnAction(actionEvent -> getCurrentPanel().refresh());
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setAccelerator(KeyCombination.keyCombination("Delete"));
        deleteItem.setOnAction(actionEvent -> commandDelete());
        Menu subCommandMenu = new Menu("Tools");
        MenuItem generateFileItem = new MenuItem("Generate Big File");
        subCommandMenu.getItems().add(generateFileItem);
        generateFileItem.setOnAction(actionEvent -> {
            TextInputDialog textInputDialog = new TextInputDialog("100000");
            textInputDialog.setTitle("Create big file");
            textInputDialog.setContentText("Set size file");
            Optional<String> result = textInputDialog.showAndWait();
            result.ifPresent(s -> {
                System.out.println("command - create big file");
                File file = new File("bigFile.txt");
                FileUtils.generateBigFile(file.getAbsolutePath(), Long.parseLong(result.get()));
                getCurrentPanel().refresh();
            });
        });
        MenuItem speedTestReadFileItem = new MenuItem("Speed test read file");
        speedTestReadFileItem.setOnAction(actionEvent -> new FileRead());
        subCommandMenu.getItems().add(speedTestReadFileItem);
        MenuItem parallelAracanoidItem = new MenuItem("Parallel arcanoid");
        parallelAracanoidItem.setOnAction(actionEvent -> new Arcanoid());
        subCommandMenu.getItems().add(parallelAracanoidItem);
        MenuItem dbViewerItem = new MenuItem("DB Viewer");
        dbViewerItem.setOnAction(actionEvent -> new DbViewer());
        subCommandMenu.getItems().add(dbViewerItem);
        MenuItem dbHibernateViewerItem = new MenuItem("Hibernate DB Viewer");
        dbHibernateViewerItem.setOnAction(actionEvent -> new Alert(Alert.AlertType.CONFIRMATION, "Soon").show());
        subCommandMenu.getItems().add(dbHibernateViewerItem);

        commandMenu.getItems().addAll(commandItemMenuItem, deleteItem, subCommandMenu);
        menuBar.getMenus().add(commandMenu);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenu.getItems().addAll(aboutItemMenuItem);
        menuBar.getMenus().add(aboutMenu);
        aboutItemMenuItem.setOnAction(actionEvent -> new About().show());
        root.getChildren().add(menuBar);
    }

    private void initContextMenu()
    {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(actionEvent -> commandOpen());
        contextMenu.getItems().addAll(openItem, new SeparatorMenuItem());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(actionEvent -> commandDelete());
        contextMenu.getItems().addAll(deleteItem, new SeparatorMenuItem());

        MenuItem infoItem = new MenuItem("Info");
        infoItem.setOnAction(actionEvent -> commandInfo());
        contextMenu.getItems().add(infoItem);

        for (TableCommander table : panels) {
            table.getTable().setContextMenu(contextMenu);
        }
    }

    private void commandNew()
    {
        Editor editor = new Editor();
        TableRowCommander tableRowCommander = getCurrentPanel().getCurrentRow();

        if (tableRowCommander != null && tableRowCommander.getFile().isFile()) {
            editor.setFile(tableRowCommander.getFile());
            editor.open();
        }

        editor.setDirectory(getCurrentPanel().getPanel().current);
        editor.show();
        editor.getStage().setOnHiding(windowEvent -> getCurrentPanel().refresh());

        System.out.println("command - new file");
    }

    private void commandCreateDirectory()
    {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Create directory");
        textInputDialog.setHeaderText("");
        textInputDialog.setContentText("Enter name");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(s -> {
            File file = new File(result.get());
            if (file.isDirectory()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Directory is exists");
                alert.showAndWait();
            } else {
                if (!file.mkdir()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error create directory");
                    alert.showAndWait();
                } else {
                    getCurrentPanel().refresh();
                }
            }
        });

        System.out.println("command - create directory");
    }

    private void commandOpen()
    {
        System.out.println("command - open file");
        TableRowCommander tableRowCommander = getCurrentPanel().getCurrentRow();

        if (tableRowCommander != null && tableRowCommander.getFile().isDirectory()) {
            commandOpenDirectory();
        } else {
            Editor editor = new Editor();
            if (tableRowCommander != null && tableRowCommander.getFile().isFile()) {
                editor.setFile(getCurrentPanel().getCurrentRow().getFile());
                editor.open();
            }
            editor.show();
            editor.getStage().setOnHiding(windowEvent -> getCurrentPanel().refresh());
        }
    }

    private void commandOpenDirectory()
    {
        System.out.println("command - open dir");
        getCurrentPanel().refresh(getCurrentPanel().getCurrentRow().getFile());
    }

    private void commandDelete()
    {
        System.out.println("command - delete");
        TableCommander tableCommander = this.getCurrentPanel();
        TableView<TableRowCommander> table = tableCommander.getTable();
        TableRowCommander rowCommander = table.getSelectionModel().getSelectedItem();
        if (rowCommander.getFile().isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Directory is not delete, temporaly");
            alert.showAndWait();
        } else if (rowCommander.getFile().isFile()) {
            File file = rowCommander.getFile();
            System.out.println("File " + file.getName() + " is delete");
            if (!file.delete()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error delete file");
                alert.showAndWait();
            } else {
                tableCommander.refresh();
            }
        }
    }

    private void commandExit()
    {
        System.out.println("command - exit");
        Platform.exit();
    }

    private void commandInfo()
    {
        File file = getCurrentPanel().getCurrentRow().getFile();
        if (file.isFile()) {
            try {
                BasicFileAttributes view = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                String info = file.getName() + "\n"
                    + "Path: " + file.getAbsolutePath() + "\n"
                    + "Size: " + file.length() + "\n"
                    + "Date create: " + (new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(view.creationTime().toMillis())) + "\n"
                    + "Date change: " + (new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(file.lastModified())) + "\n";
                new Popup().show(info);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            BasicFileAttributes view;
            try {
                view = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                String info = "Directory: " + file.getName() + "\n"
                    + "Path: " + file.getAbsolutePath() + "\n"
                    + "Size: " + file.length() + "\n"
                    + "Date create: " + (new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(view.creationTime().toMillis())) + "\n"
                    + "Date change: " + (new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(file.lastModified())) + "\n";
                new Popup().show(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void commandCopy()
    {
        System.out.println("command - copy");

        TableRowCommander tableRowCommander = getCurrentPanel().getCurrentRow();
        if (tableRowCommander.getFile().isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Copy only file");
            alert.showAndWait();
            return;
        }

        String name = FileExt.getName(tableRowCommander.getFile()) + " copy." + FileExt.getExtension(tableRowCommander.getFile());
        String directory = getOtherPanel().getCurrentDirectory();
        System.out.println("Copy " + tableRowCommander.getFile().getAbsolutePath() + " to " + directory + "\\" + name);

        File newFile = new File(directory + "\\" + name);
        FileExt.copy(tableRowCommander.getFile(), newFile);
        getOtherPanel().refresh();
    }

    private void commandRename()
    {
        System.out.println("command - rename");

        TableRowCommander tableRowCommander = getCurrentPanel().getCurrentRow();

        TextInputDialog textInputDialog = new TextInputDialog(tableRowCommander.getFile().getName());
        textInputDialog.setTitle("Rename");
        textInputDialog.setContentText("Enter name");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(s -> {
            String newName = result.get();
            if (!tableRowCommander.getFile().renameTo(new File(newName))) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error rename " + tableRowCommander.getFile().getName());
                alert.showAndWait();
            } else {
                getCurrentPanel().refresh();
            }
        });
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
        Button f1 = new Button("F1 - about");
        f1.setOnAction(actionEvent -> new About().show());

        Button f3 = new Button("F3 - read");
        f3.setOnAction(actionEvent -> commandOpen());

        Button f4 = new Button("F4 - edit");
        f4.setOnAction(actionEvent -> commandOpen());

        Button f5 = new Button("F5 - copy");
        f5.setOnAction(actionEvent -> commandCopy());

        Button f6 = new Button("F6 - rename");
        f6.setOnAction(actionEvent -> commandRename());

        Button f7 = new Button("F7 - create dir");
        f7.setOnAction(actionEvent -> commandCreateDirectory());

        Button f9 = new Button("F9 - delete");
        f9.setOnAction(actionEvent -> commandDelete());

        Button f10 = new Button("F10 - exit");
        f10.setOnAction(actionEvent -> commandExit());

        f1.setPrefWidth(1000000);
        f3.setPrefWidth(1000000);
        f4.setPrefWidth(1000000);
        f5.setPrefWidth(1000000);
        f6.setPrefWidth(1000000);
        f7.setPrefWidth(1000000);
        f9.setPrefWidth(1000000);
        f10.setPrefWidth(1000000);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(f1, f3, f4, f5, f6, f7, f9, f10);
        HBox.setHgrow(f1, Priority.ALWAYS);
        HBox.setHgrow(f3, Priority.ALWAYS);
        HBox.setHgrow(f4, Priority.ALWAYS);
        HBox.setHgrow(f5, Priority.ALWAYS);
        HBox.setHgrow(f6, Priority.ALWAYS);
        HBox.setHgrow(f7, Priority.ALWAYS);
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

        panels.get(0).getTable().getSelectionModel().select(0);
        return panels.get(0);
    }

    public TableCommander getOtherPanel()
    {
        for (TableCommander table : panels) {
            if (table.getTable().getSelectionModel().getSelectedItem() == null) {
                System.out.println("Current for loop: " + table.getPanel().current.getAbsolutePath());
                return table;
            }
        }

        panels.get(0).getTable().getSelectionModel().select(0);
        System.out.println("Current for loop: " + panels.get(0).getPanel().current.getAbsolutePath());
        return panels.get(0);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
