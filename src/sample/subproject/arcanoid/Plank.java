package sample.subproject.arcanoid;

import javafx.scene.Group;

public class Plank extends Block
{
    public Plank(Group pane)
    {
        super(pane);

        pane.getScene().setOnMouseMoved(mouseEvent -> {
            x = mouseEvent.getX() - width / 2;
            if (x < 5) {
                x = 5;
            }
            if (x + width > pane.getScene().getWidth() - 10) {
                x = pane.getScene().getWidth() - width;
            }
        });
    }
}
