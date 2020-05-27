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

    ArrayList<Thread> blocksThread;
    ArrayList<Thread> ballsThread;

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

        Menu toolMenu = new Menu("Tool");
        MenuItem newBallItem = new MenuItem("New ball");
        newBallItem.setOnAction(actionEvent -> commandNewBall());
        newBallItem.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        toolMenu.getItems().add(newBallItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenu.getItems().add(aboutItemMenuItem);
        aboutItemMenuItem.setOnAction(actionEvent -> new Popup().show("Arcanoid"));

        menuBar.getMenus().addAll(fileMenu, toolMenu, aboutMenu);

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
        timer.cancel();
        stage.hide();
    }

    public void commandNew()
    {
        System.out.println("new arcanoid");

        lives = 3;
        count = 0;

        blocks = new ArrayList<>();
        blocksThread = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 14; i++) {
                Block block = new Block(root);
                block.randomColor();
                block.width = 70;
                block.height = 30;
                block.x = 5 + i * (block.width + 2);
                block.y = 25 + j * (block.height + 2);
                blocks.add(block);
                Thread thread = new Thread(block);
                blocksThread.add(thread);
            }
        }

        plank = new Block(root);
        plank.width = 150;
        plank.height = 30;
        plank.x = stage.getScene().getWidth() / 2 - plank.width / 2;
        plank.y = stage.getScene().getHeight() - plank.height - 5;

        balls = new ArrayList<>();
        ballsThread = new ArrayList<>();
        Ball ball = new Ball(root);
        ball.x = plank.x;
        ball.y = plank.y - 30;
        balls.add(ball);
        Thread thread = new Thread(ball);
        ballsThread.add(thread);

        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                update();
                render();
                check();
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
            if (plank.x + plank.width > stage.getScene().getWidth() - 5) {
                plank.x = stage.getScene().getWidth() - plank.width;
            }
        });
    }

    public void commandGameEnd()
    {
        for (Block block : blocks) {
            block.render();
        }
        blocks.clear();
        plank.destroy();

        for (Ball ball : balls) {
            ball.destroy();
        }
        balls.clear();
    }

    public void commandNewBall()
    {
        Platform.runLater(() -> {
            Ball newBall = new Ball(root);
            newBall.x = plank.x + plank.width / 2;
            newBall.y = plank.y - newBall.radius;
            newBall.dx = Math.random() * 6 - 3;
            newBall.dy = Math.random() * 4 - 4;
            newBall.randomColor();
            balls.add(newBall);
            Thread thread = new Thread(newBall);
            ballsThread.add(thread);
        });
    }

    private void check()
    {
        Platform.runLater(() -> {
            stage.setTitle("Arcanoid - lives: " + lives + ", count: " + count);

            if (blocks.size() == 0) {
                timer.cancel();
                stage.setTitle("Arcanoid");
                new Popup().show("Games win\nCount: " + count);
                commandGameEnd();
            }

            if (balls.size() == 0) {
                lives--;
                commandNewBall();
            }

            if (lives <= 0) {
                timer.cancel();
                stage.setTitle("Arcanoid");
                new Popup().show("Games failed\nCount: " + count);
                commandGameEnd();
            }
        });
    }

    public void render()
    {
        for (Thread thread : blocksThread) {
            thread.start();
        }

        plank.render();

        for (Thread thread : ballsThread) {
            thread.start();
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
            }

            if (ball.y + ball.radius + ball.dy > stage.getScene().getHeight()) {
                ball.destroy();
                continue;
            }

            // ball to ball cd
            int index = balls.indexOf(ball);
            if (balls.size() > 1 && index < balls.size() - 1) {
                for (Ball ballOther : balls.subList(index + 1, balls.size())) {
                    if (!ballOther.circle.isVisible() || ballOther.equals(ball)) {
                        continue;
                    }
                    if (CollisionDetection.CircleCircle(ballOther.getCircle(), ball.getCircle())) {
                        ballOther.dy = -ballOther.dy; // @todo
                        ballOther.dy += Math.random();
                        break;
                    }
                }
            }
        }

        for (Block block : blocks) {
            for (Ball ball : balls) {
                if (ball.circle.isVisible() && block.rectangle.isVisible() &&
                    CollisionDetection.BoxCircle(block.rectangle, ball.getCircle())) {
                    block.destroy();
                    blocks.remove(block);
                    ball.dy = -ball.dy; // @todo
                    ball.dy += Math.random();
                    count++;

                    if (Math.random() * 100 < (20 - balls.size()) * lives) {
                        Platform.runLater(() -> {
                            Ball newBall = new Ball(root);
                            newBall.radius = Math.random() * 10 + 10;
                            newBall.x = ball.x + newBall.radius + ball.radius;
                            newBall.y = ball.y + newBall.radius;
                            newBall.dx = Math.random() * 6 - 3;
                            newBall.dy = Math.random() * 2 + 2;
                            newBall.randomColor();
                            balls.add(newBall);
                            Thread thread = new Thread(newBall);
                            ballsThread.add(thread);
                        });
                    }

                    return;
                }
            }
        }
    }
}
