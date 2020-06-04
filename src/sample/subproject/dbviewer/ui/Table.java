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
import java.sql.*;
import java.util.ArrayList;

public class Table extends Stage
{
    String database;
    ArrayList<Column> columns;

    public Table(String database)
    {
        this.database = database;
        this.columns = new ArrayList<>();

        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
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
            if (name.getText().length() > 0) {
                commandCreateTable(name.getText());
                hide();
            }
        });
        root.getChildren().addAll(label, name, addColumn, columns, new Separator(), button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 400);
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
                String sql = "CREATE TABLE IF NOT EXISTS " + name + "(";
                for (Column column : columns) {
                    sql += column.name + " " + column.type + ",";
                }
                sql = sql.substring(0, sql.length() - 1) + ");";
                System.out.println("sql: " + sql);
                Statement statement = conn.createStatement();
                statement.execute(sql);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
