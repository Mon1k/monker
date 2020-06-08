package sample.subproject.dbviewer.ui.tableview;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class Table
{
    TableView<TableRow> table;

    public Table(ArrayList<String> columns)
    {
        ArrayList<TableRow> rows = new ArrayList<>();
        for (String column: columns) {
            TableRow tableRow = new TableRow();
            tableRow.setName(column);
            rows.add(tableRow);
        }


        table = new TableView<>(FXCollections.observableArrayList(rows));
        for (TableRow row: rows) {
            TableColumn<TableRow, String> columnFactory = new TableColumn<>(row.getName());
            columnFactory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        }
    }

    public void addRows(ArrayList<TableRow> rows)
    {
        ObservableList<TableRow> rowsRefresh = FXCollections.observableArrayList(rows);
        table.setItems(rowsRefresh);
        table.refresh();
    }
}
