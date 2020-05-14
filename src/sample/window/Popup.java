package sample.window;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup
{
    public void show(String title)
    {
        this.show(title, 300, 200);
    }

    public void show()
    {
        this.show("");
    }

    public void show(String title, int width, int height)
    {
        Label info = new Label(title);

        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(info);

        Scene scene = new Scene(root, width, height);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Info");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
