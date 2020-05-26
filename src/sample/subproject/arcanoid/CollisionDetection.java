package sample.subproject.arcanoid;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CollisionDetection
{
    public static boolean BoxBox(Rectangle first, Rectangle second)
    {
        if (first.getX() < second.getX() + second.getWidth() &&
            first.getX() + first.getWidth() > second.getX() &&
            first.getY() < second.getY() + second.getHeight() &&
            first.getY() + first.getHeight() > second.getY()
        ) {
            return true;
        }

        return false;
    }

    public static boolean BoxCircle(Rectangle first, Circle second)
    {
        if (first.getX() < second.getCenterX() + second.getRadius() &&
            first.getX() + first.getWidth() > second.getCenterX() - second.getRadius() &&
            first.getY() < second.getCenterY() + second.getRadius() &&
            first.getY() + first.getHeight() > second.getCenterY() - second.getRadius()
        ) {
            return true;
        }

        return false;
    }

    public static boolean CircleCircle(Circle first, Circle second)
    {
        double dx = first.getCenterX() - second.getCenterX();
        double dy = first.getCenterY() - second.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < first.getRadius() + second.getRadius()) {
            return true;
        }

        return false;
    }
}
