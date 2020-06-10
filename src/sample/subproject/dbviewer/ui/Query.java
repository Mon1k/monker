package sample.subproject.dbviewer.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.subproject.dbviewer.DbViewer;
import sample.subproject.dbviewer.resultset.Cell;
import sample.subproject.dbviewer.resultset.Row;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Query extends Stage
{
    String database;
    ArrayList<Row> result;

    public Query(DbViewer viewer, String database)
    {
        this.database = database;

        VBox root = new VBox();
        HBox hBox = new HBox();
        TextArea queryText = new TextArea("select * from test");
        queryText.setWrapText(true);
        queryText.setPrefHeight(350);
        hBox.getChildren().add(queryText);
        HBox.setHgrow(queryText, Priority.ALWAYS);
        Button submit = new Button("run");
        submit.setOnAction(actionEvent -> {
            result = query(queryText.getText());
            viewer.addViewTable(result);
            if (!result.isEmpty()) {
                hide();
            }
        });
        root.getChildren().addAll(hBox, submit);

        Scene scene = new Scene(root, 600, 400);
        setScene(scene);
        setResizable(false);
        setTitle("Query editor");
    }

    public ArrayList<Row> query(String query)
    {
        System.out.println("query - " + query);
        result = new ArrayList<>();

        if (query.length() <= 2) {
            System.out.println("Empty query");
            return result;
        }

        File file = new File("");
        String url = "jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/") + "/" + database;
        System.out.println("database - " + url);
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();

                if (query.contains("select")) {
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        Row row = new Row();
                        row.cells = new ArrayList<>();
                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                            String value = resultSet.getString(i);
                            System.out.println(resultSetMetaData.getColumnName(i) + "=" + value);

                            Cell cell = new Cell();
                            cell.name = resultSetMetaData.getColumnName(i);
                            cell.value = value;
                            row.cells.add(cell);
                        }
                        result.add(row);

                    }
                    resultSet.close();
                } else {
                    statement.execute(query);
                }
                statement.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public ArrayList<Row> getResult()
    {
        return result;
    }

}
