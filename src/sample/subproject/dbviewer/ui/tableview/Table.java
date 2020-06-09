package sample.subproject.dbviewer.ui.tableview;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.subproject.dbviewer.resultset.Cell;
import sample.subproject.dbviewer.resultset.Row;

import java.util.ArrayList;

public class Table
{
    TableView<TableRow> table;

    public Table(ArrayList<Row> rows)
    {
        System.out.println(rows);

        ArrayList<TableRow> rowsTable = new ArrayList<>();
        for (Row row : rows) {
            TableRow tableRow = new TableRow();
            for (Cell cell : row.cells) {
                TableCell tableCell = new TableCell();
                tableCell.value = cell.value;
                tableCell.name = cell.name;
                tableRow.cells.add(tableCell);
            }
            rowsTable.add(tableRow);
        }


        table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(rowsTable));

        for (Cell cell : rows.get(0).cells) {
            System.out.println("column=" + cell.name);
            TableColumn<TableRow, String> columnFactory = new TableColumn<>(cell.name);
            columnFactory.setCellValueFactory(cellData -> {
                String name = cellData.getTableColumn().getText();
                for (TableCell cellInTable : cellData.getValue().cells) {
                    if (cellInTable.name.equals(name)) {
                        return new SimpleStringProperty(cellInTable.value);
                    }
                }
                return null;
            });
            table.getColumns().add(columnFactory);
        }
    }

    public TableView<TableRow> getTable()
    {
        return table;
    }
}
