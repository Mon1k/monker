package sample.subproject.arcanoid;

import javafx.application.Platform;
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

    int lives;
    int count;

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
        stage.show();
        stage.setOnHiding(windowEvent -> commandExit());
    }

    public void commandExit()
    {
        System.out.println("exit arcanoid");
        stage.hide();
        timer.cancel();
    }

    public void commandNew()
    {
        System.out.println("new arcanoid");

        lives = 3;
        count = 0;

        blocks = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 14; i++) {
                Block block = new Block(root);
                block.randomColor();
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
                checkGame();
            }
        };

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer("loop");
        timer.schedule(timerTask, 0, 20L);

        stage.getScene().setOnMouseMoved(mouseEvent -> {
            plank.x = mouseEvent.getX() - plank.width / 2;
            if (plank.x < 5) {
                plank.x = 5;
            }
            if (plank.x + plank.width > stage.getScene().getWidth()) {
                plank.x = stage.getScene().getWidth() - plank.width;
            }
        });
    }

    private void checkGame()
    {
        Platform.runLater(() -> {
            stage.setTitle("Arcanoid - lives: " + lives + ", count: " + count);

            boolean isEmpty = true;
            boolean isEmptyBall = true;
            for (Block block : blocks) {
                if (block.rectangle.isVisible()) {
                    isEmpty = false;
                    break;
                }
            }
            for (Ball ball: balls) {
                if (ball.circle.isVisible()) {
                    isEmptyBall = false;
                    break;
                }
            }
            if (isEmpty) {
                timer.cancel();
                stage.setTitle("Arcanoid");
                new Popup().show("Games win\nCount: " + count);
            }

            if (lives <= 0 || isEmptyBall) {
                timer.cancel();
                stage.setTitle("Arcanoid");
                new Popup().show("Games failed\nCount: " + count);
            }
        });
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
            if (!ball.circle.isVisible()) {
                continue;
            }
            if (CollisionDetection.BoxBox(plank.rectangle, ball.getRectangle())) {
                ball.dy = -ball.dy; // @todo
                ball.dy += Math.random();
                break;
            }

            if (ball.y + ball.radius + ball.dy > stage.getScene().getHeight()) {
                lives--;
                ball.destroy();
                continue;
            }

            // ball to ball cd
            for (Ball ballOther : balls) {
                if (!ballOther.circle.isVisible()) {
                    continue;
                }
                if (ballOther.equals(ball)) {
                    continue;
                }
                if (CollisionDetection.CircleCircle(ballOther.getCircle(), ball.getCircle())) {
                    ballOther.dy = -ballOther.dy; // @todo
                    ballOther.dy += Math.random();
                    break;
                }
            }

            ball.move();
        }

        for (Block block : blocks) {
            for (Ball ball : balls) {
                if (ball.circle.isVisible() && block.rectangle.isVisible() &&
                    CollisionDetection.BoxBox(block.rectangle, ball.getRectangle())) {
                    block.destroy();
                    blocks.remove(block);
                    ball.dy = -ball.dy; // @todo
                    ball.dy += Math.random();
                    count++;

                    if (Math.random() * 100 < 20 * lives) {
                        Platform.runLater(() -> {
                            Ball newBall = new Ball(root);
                            newBall.radius = Math.random() * 10 + 10;
                            newBall.x = ball.x;
                            newBall.y = ball.y + newBall.radius;
                            newBall.dx = Math.random() * 6 - 3;
                            newBall.dy = Math.random() * 2 + 2;
                            newBall.randomColor();
                            balls.add(newBall);
                        });
                    }

                    return;
                }
            }
        }
    }
}
