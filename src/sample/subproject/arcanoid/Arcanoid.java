package sample.subproject.arcanoid;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import sample.window.Popup;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Arcanoid
{
    Stage stage;
    Group root;
    Timer timer;

    ArrayList<Block> blocks;
    Block plank;
    ArrayList<Ball> balls;

    public Arcanoid()
    {
        stage = new Stage();
        root = new Group();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction(actionEvent -> commandNew());
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> commandExit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(newItem, seperator, exitItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenu.getItems().add(aboutItemMenuItem);
        aboutItemMenuItem.setOnAction(actionEvent -> new Popup().show("Arcanoid"));

        menuBar.getMenus().addAll(fileMenu, aboutMenu);

        root.getChildren().addAll(menuBar);

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Arcanoid");
        stage.setResizable(false);

        commandNew();
        stage.showAndWait();
        stage.setOnCloseRequest(windowEvent -> commandExit());
    }

    public void commandExit()
    {
        stage.hide();
        timer.cancel();
    }

    public void commandNew()
    {
        blocks = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 13; i++) {
                Block block = new Block(root);
                block.width = 70;
                block.height = 30;
                block.x = 5 + i * (block.width + 2);
                block.y = 25 + j * (block.height + 2);
                blocks.add(block);
            }
        }

        plank = new Block(root);
        plank.width = 150;
        plank.height = 30;
        plank.x = stage.getScene().getWidth() / 2 - plank.width / 2;
        plank.y = stage.getScene().getHeight() - plank.height - 5;

        balls = new ArrayList<>();
        Ball ball = new Ball(root);
        ball.x = plank.x;
        ball.y = plank.y - 30;
        balls.add(ball);

        render();

        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                update();
                render();
            }
        };

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer("loop");
        timer.schedule(timerTask, 1000L, 20L);
    }

    public void render()
    {
        for (Block block : blocks) {
            block.render();
        }

        plank.render();

        for (Ball ball : balls) {
            ball.render();
        }
    }

    public void update()
    {
        for (Ball ball : balls) {
            ball.move();
        }
    }
}
