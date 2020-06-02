package sample.subproject.arcanoid;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block implements Runnable
{
    public double x;
    public double y;
    public double width;
    public double height;
    public Group pane;
    public Rectangle rectangle;

    public Block(Group pane)
    {
        this.pane = pane;
        this.rectangle = new Rectangle();
        this.pane.getChildren().add(rectangle);
    }

    public void update()
    {
    }

    public void render()
    {
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }

    public void destroy()
    {
        Platform.runLater(() -> pane.getChildren().remove(rectangle));
    }

    public void randomColor()
    {
        rectangle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
    }

    @Override
    public void run()
    {
        update();
        render();
    }
}
