package sample.window;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Editor
{
    private File file;
    private File directory;

    private final TextArea textArea;
    private final Stage stage;

    public Editor()
    {
        stage = new Stage();
        VBox root = new VBox();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            if (directory != null) {
                fileChooser.setInitialDirectory(directory);
            }
            file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                open();
            }
        });
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveItem.setOnAction(actionEvent -> {
            if (file == null) {
                FileChooser fileChooser = new FileChooser();
                if (directory != null) {
                    fileChooser.setInitialDirectory(directory);
                }
                file = fileChooser.showSaveDialog(stage);
            }
            if (file != null) {
                save();
            }
        });
        MenuItem saveAsItem = new MenuItem("SaveAs");
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        saveAsItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            if (directory != null) {
                fileChooser.setInitialDirectory(directory);
            }
            file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                save();
            }
        });
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> {
            stage.hide();
        });
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, seperator, exitItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenu.getItems().add(aboutItemMenuItem);
        aboutItemMenuItem.setOnAction(actionEvent -> {
            new About().show();
        });

        menuBar.getMenus().addAll(fileMenu, aboutMenu);

        textArea = new TextArea();
        textArea.setWrapText(true);
        HBox hBox = new HBox();
        hBox.getChildren().add(textArea);
        HBox.setHgrow(textArea, Priority.ALWAYS);
        VBox.setVgrow(hBox, Priority.ALWAYS);


        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(menuBar, hBox);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Scene scene = new Scene(root, screenSize.getWidth() / 1.5, screenSize.getHeight() / 2);
        stage.setScene(scene);
        stage.setTitle("Editor");
    }

    public void show()
    {
        stage.show();
    }

    public void save()
    {
        FileWriter writer;
        try {
            writer = new FileWriter(file.getAbsolutePath());
            writer.write(textArea.getText());
            writer.close();
            System.out.println("write it file: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open()
    {
        System.out.println("Read: " + file.getAbsolutePath());
        stage.setTitle("Open - " + file.getAbsolutePath());
        try {
            long time = System.currentTimeMillis();
            StringBuilder text = new StringBuilder();
            BufferedReader reader = Files.newBufferedReader(file.toPath());
            while (reader.ready()) {
                text.append(reader.readLine()).append("\n");
            }
            reader.close();
            System.out.println("read time=" + (System.currentTimeMillis() - time));

            textArea.setText(text.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public void setDirectory(File directory)
    {
        this.directory = directory;
    }

    public Stage getStage()
    {
        return stage;
    }
}
