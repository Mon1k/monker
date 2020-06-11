package sample.subproject.dbviewer.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.subproject.dbviewer.DbViewer;
import sample.subproject.dbviewer.resultset.Row;

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
        submit.setPrefWidth(200);
        submit.setOnAction(actionEvent -> {
            sample.subproject.dbviewer.Query query = new sample.subproject.dbviewer.Query(database);
            result = query.exec(queryText.getText());
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
        show();
    }
}
