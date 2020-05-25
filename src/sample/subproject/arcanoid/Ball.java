package sample.subproject.arcanoid;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Ball
{
    double x;
    double y;
    double radius = 15;
    double dx = 3;
    double dy = 2;
    Group pane;
    Circle circle;

    public Ball(Group pane)
    {
        this.pane = pane;
        this.circle = new Circle();
        this.pane.getChildren().add(circle);
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
}
