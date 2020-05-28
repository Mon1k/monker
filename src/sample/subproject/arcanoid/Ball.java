package sample.subproject.arcanoid;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Ball implements Runnable
{
    double x;
    double y;
    double radius = 15;
    double dx = 3;
    double dy = -2;
    Group pane;
    Circle circle;

    public Ball(Group pane)
    {
        this.pane = pane;
        this.circle = new Circle();
        this.pane.getChildren().add(circle);
    }

    public Rectangle getRectangle()
    {
        return getRectangle(true);
    }

    public Rectangle getRectangle(boolean useDelta)
    {
        if (!useDelta) {
            return new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
        }

        return new Rectangle(x + dx - radius, y + dy - radius, radius * 2, radius * 2);
    }

    public Circle getCircle()
    {
        return getCircle(true);
    }

    public Circle getCircle(boolean useDelta)
    {
        if (!useDelta) {
            return circle;
        }

        return new Circle(x + dx, y + dy, radius);
    }

    public void render()
    {
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
    }

    public void move()
    {
        if (x - radius < 0) {
            dx = -dx;
        }
        if (y - radius < 0) {
            dy = -dy;
        }
        if (x + radius + dx > pane.getScene().getWidth()) {
            dx = -dx;
        }
        if (y + radius + dy > pane.getScene().getHeight()) {
            dy = -dy;
        }

        x += dx;
        y += dy;
    }

    public void destroy()
    {
        Platform.runLater(() -> {
            pane.getChildren().remove(circle);
        });
    }

    public void randomColor()
    {
        circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
    }

    @Override
    public void run()
    {
        move();
        render();
    }
}
