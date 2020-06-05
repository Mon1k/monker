package sample.subproject.dbviewer.ui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Table extends Stage
{
    String database;
    ArrayList<Column> columns;
    ScrollPane scrollPane;
    VBox scrollPaneVBox;

    public Table(String database)
    {
        this.database = database;
        this.columns = new ArrayList<>();

        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
        HBox hBox = new HBox();
        Label label = new Label("Enter table name:");
        TextField name = new TextField();
        hBox.getChildren().addAll(label, name);

        Button addColumn = new Button("+");
        addColumn.setOnAction(actionEvent -> {
            Column column = new Column();
            column.show();
            column.setOnHidden(windowEvent -> {
                addColumnToUi(column);
                column.hide();
            });
        });
        root.getChildren().addAll(hBox, addColumn);

        scrollPane = new ScrollPane();
        scrollPaneVBox = new VBox();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        for (int i = 0; i < 10; i++) {
            Column col1 = new Column();
            col1.type = "integer";
            col1.name = "id_"+i;
            addColumnToUi(col1);
        }
        root.getChildren().addAll(new Label("Columns:"), scrollPane);

        Button button = new Button("Create");
        button.setOnAction(actionEvent -> {
            if (name.getText().length() > 0) {
                commandCreateTable(name.getText());
                hide();
            }
        });
        root.getChildren().addAll(button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 400);
        setScene(scene);
        setResizable(false);
        setTitle("Create table");
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

    public void addColumnToUi(Column column)
    {
        this.columns.add(column);
        scrollPaneVBox.getChildren().clear();

        for (Column c : this.columns) {
            HBox hBox = new HBox(10);
            Label name = new Label(c.name);
            Label type = new Label(c.type);
            Button delButton = new Button("X");
            delButton.setOnAction(actionEvent -> {
                scrollPaneVBox.getChildren().remove(hBox);
                columns.remove(c);
            });
            hBox.getChildren().addAll(name, type, delButton);
            scrollPaneVBox.getChildren().add(hBox);
        }

        scrollPane.setContent(scrollPaneVBox);
        scrollPane.setVmax(200);
        scrollPane.setPrefSize(400, 200);
    }
}
