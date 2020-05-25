package sample.subproject.arcanoid;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public class Block
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

    public void render()
    {
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }
}
