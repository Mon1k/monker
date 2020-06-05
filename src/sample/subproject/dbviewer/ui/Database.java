package sample.subproject.dbviewer.ui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database extends Stage
{
    String database = "test";

    public Database()
    {
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
        Label label = new Label("Enter name:");
        TextField name = new TextField(this.database);
        Button button = new Button("Create");
        button.setOnAction(actionEvent -> {
            if (name.getText().length() > 0) {
                commandCreateDatabase(name.getText());
                hide();
            }
        });
        root.getChildren().addAll(label, name, new Separator(), button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 100);
        setScene(scene);
        setResizable(false);
        setTitle("Create database");
        show();
    }

    public void commandCreateDatabase(String name)
    {
        System.out.println("Create database - " + name);
        database = name;

        File file = new File("");
        String url = "jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/") + "/" + name;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database " + url + " with name " + name + " is created");
                System.out.println(meta);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getDatabase()
    {
        return database;
    }
}
