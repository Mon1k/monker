package sample.subproject.dbviewer.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Column extends Stage
{
    String name;
    String type;

    public Column()
    {
        FlowPane root = new FlowPane();
        Label label = new Label("Enter name:");
        TextField name = new TextField();
        ChoiceBox<String> types = new ChoiceBox<>();
        types.getItems().addAll("String", "Integer");
        types.getSelectionModel().select(0);

        Button button = new Button("Create");
        button.setOnAction(actionEvent -> {
            this.name = name.getText();
            this.type = types.getSelectionModel().getSelectedItem();
            hide();
        });
        root.getChildren().addAll(label, name, types, new Separator(), button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Create database");
        show();
    }
}
