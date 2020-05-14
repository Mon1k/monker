package sample.window;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;

public class About
{
    public void show()
    {
        Label info = new Label(Main.VERSION);

        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(info);

        Scene scene = new Scene(root, 300, 200);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("About");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
