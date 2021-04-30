package sample.subproject.arcanoid;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import sample.window.Popup;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Arcanoid
{
    Stage stage;
    Group root;

    Timer timer;
    ExecutorService executor;

    ArrayList<Block> blocks;
    Plank plank;
    ArrayList<Ball> balls;

    int lives;
    int count;
    boolean running = false;
    boolean autoPlank = false;

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
        CheckMenuItem autoMenuItem = new CheckMenuItem("Auto pank");
        autoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        autoMenuItem.setOnAction(actionEvent -> autoPlank = !autoPlank);
        toolMenu.getItems().addAll(newBallItem, autoMenuItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutItemMenuItem.setOnAction(actionEvent -> new Popup().show("Arcanoid"));
        aboutMenu.getItems().add(aboutItemMenuItem);

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
        commandGameEnd();
        stage.hide();
    }

    public void commandNew()
    {
        System.out.println("new arcanoid");

        lives = 3;
        count = 0;
        running = true;

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

        plank = new Plank(root);
        plank.width = 150;
        plank.height = 30;
        plank.x = stage.getScene().getWidth() / 2 - plank.width / 2;
        plank.y = stage.getScene().getHeight() - plank.height - 5;

        balls = new ArrayList<>();
        Ball ball = new Ball(root);
        ball.x = plank.x;
        ball.y = plank.y - 30;
        balls.add(ball);

        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (running) {
                    update();
                    render();
                    check();
                }
            }
        };

        if (timer != null) {
            timer.cancel();
        }
        executor = Executors.newCachedThreadPool();

        timer = new Timer("loop");
        timer.schedule(timerTask, 0, 20L);
    }

    public void commandGameEnd()
    {
        running = false;
        timer.cancel();
        executor.shutdown();

        for (Block block : blocks) {
            block.destroy();
        }
        blocks.clear();
        plank.destroy();

        for (Ball ball : balls) {
            ball.destroy();
        }
        balls.clear();
        stage.setTitle("Arcanoid");
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
        });
    }

    public void commandAutoPlank()
    {
        if (!autoPlank) {
            return;
        }

        int index = -1;
        double max = 0;
        for (Ball ball : balls) {
            if (ball.y > max && ball.dy > 0) {
                index = balls.indexOf(ball);
                max = ball.y;
            }
        }

        if (index >= 0) {
            Ball ball = balls.get(index);
            plank.x = ball.x - plank.width / 2;
        }
    }

    private void check()
    {
        Platform.runLater(() -> {
            if (!running) {
                return;
            }

            ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
            stage.setTitle("Arcanoid - lives: " + lives + ", count: " + count + " threads: " + poolExecutor.getQueue().size());

            if (blocks.size() == 0) {
                commandGameEnd();
                new Popup().show("Games win\nCount: " + count);
                return;
            }

            if (balls.size() == 0) {
                lives--;
                commandNewBall();
            }

            if (lives <= 0) {
                commandGameEnd();
                new Popup().show("Games failed\nCount: " + count);
            }
        });
    }

    public void render()
    {
        if (!running) {
            return;
        }

        synchronized (blocks.iterator()) {
            for (Block block : blocks) {
                Platform.runLater(() -> {
                    if (running && !executor.isShutdown()) {
                        executor.execute(block);
                    }
                });
            }
        }

        synchronized (balls.iterator()) {
            for (Ball ball : balls) {
                ball.render();
                /*Platform.runLater(() -> {
                    if (running && !executor.isShutdown()) {
                        executor.execute(ball);
                    }
                });*/
            }
        }

        plank.render();
    }

    public void update()
    {
        if (!running) {
            return;
        }

        commandAutoPlank();

        for (Ball ball : balls) {
            if (!ball.circle.isVisible()) {
                continue;
            }

            ball.update();

            if (CollisionDetection.BoxCircle(plank.rectangle, ball.getCircle())) {
                ball.dy = -ball.dy; // @todo
                ball.dy += Math.random() * 2 - 1;
            }

            if (ball.y + ball.radius + ball.dy > stage.getScene().getHeight() - plank.height) {
                Platform.runLater(() -> {
                    ball.destroy();
                    balls.remove(ball);
                });
                return;
            }

            // ball to ball cd
            int index = balls.indexOf(ball);
            if (balls.size() > 1 && index < balls.size() - 1) {
                for (Ball ballOther : balls.subList(index + 1, balls.size())) {
                    if (!ballOther.circle.isVisible() || ballOther.equals(ball)) {
                        continue;
                    }
                    if (CollisionDetection.CircleCircle(ballOther.getCircle(), ball.getCircle())) {
                        if ((ballOther.dy > 0 && ball.dy < 0) || (ballOther.dy < 0 && ball.dy > 0)) {
                            ballOther.dy = -ballOther.dy; // @todo
                            ballOther.dy += Math.random() * 2 - 1;
                        }
                        ball.dy = -ball.dy;
                        break;
                    }
                }
            }
        }

        for (Block block : blocks) {
            for (Ball ball : balls) {
                if (ball.circle.isVisible() && block.rectangle.isVisible() &&
                    CollisionDetection.BoxCircle(block.rectangle, ball.getCircle())) {
                    ball.dy = -ball.dy; // @todo
                    ball.dy += Math.random() * 2 - 1;
                    count++;

                    if (Math.random() * 100 < (10 - balls.size()) * lives) {
                        Platform.runLater(() -> {
                            Ball newBall = new Ball(root);
                            newBall.radius = Math.random() * 10 + 10;
                            newBall.x = ball.x + newBall.radius + ball.radius;
                            newBall.y = ball.y + newBall.radius;
                            newBall.dx = Math.random() * 3 - 3;
                            newBall.dy = Math.random() * 1 + 2;
                            newBall.randomColor();
                            balls.add(newBall);
                        });
                    }

                    Platform.runLater(() -> {
                        block.destroy();
                        blocks.remove(block);
                    });

                    return;
                }
            }
        }
    }
}
