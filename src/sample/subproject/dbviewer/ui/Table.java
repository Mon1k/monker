package sample.subproject.dbviewer.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Table extends Stage
{
    String database;
    ArrayList<Column> columns;

    public Table(String database)
    {
        this.database = database;

        FlowPane root = new FlowPane();
        Label label = new Label("Enter name:");
        TextField name = new TextField();
        Label columns = new Label("Columns:");
        Button addColumn = new Button("+");
        addColumn.setOnAction(actionEvent -> {
            Column column = new Column();
            column.setOnHidden(windowEvent -> {
                this.columns.add(column);
                String text = "Columns:\n";
                for (Column c : this.columns) {
                    text += c.name + ": " + c.type + "\n";
                }
                columns.setText(text);
                System.out.println(text);
                column.hide();
            });
        });


        Button button = new Button("Create");
        button.setOnAction(actionEvent -> {
            commandCreateTable(name.getText());
        });
        root.getChildren().addAll(label, name, addColumn, columns, new Separator(), button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Create database");
        show();
    }

    public void commandCreateTable(String name)
    {
        System.out.println("Create table - " + name);

        File file = new File("");
        String url = "jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/") + "/" + name;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();
                statement.execute("");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
