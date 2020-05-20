package sample.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.io.File;

public class TableCommander
{
    private final TablePanel panel;
    private final TableView<TableRowCommander> table;
    private final Text path;

    public TableCommander()
    {
        panel = new TablePanel();
        panel.scaner();

        ObservableList<TableRowCommander> rows = FXCollections.observableArrayList(
            panel.getPanel()
        );

        table = new TableView<>(rows);
        TableColumn<TableRowCommander, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(300);
        TableColumn<TableRowCommander, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        TableColumn<TableRowCommander, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<TableRowCommander, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(nameColumn, sizeColumn, typeColumn, dateColumn);
        table.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                openDirectory();
            }
        });
        table.setOnMouseClicked(mouseEvent -> {
            if (table.getSelectionModel().getSelectedItem() == null) {
                table.getSelectionModel().select(0);
            }
            if (mouseEvent.getClickCount() >= 2) {
                openDirectory();
            }
        });

        path = new Text(getPanel().current.getAbsolutePath());
        path.prefHeight(15);
    }

    private void openDirectory()
    {
        TableRowCommander rowCommander = table.getSelectionModel().getSelectedItem();
        if (rowCommander.getName().equals("...")) {
            File parent = rowCommander.getFile().getAbsoluteFile().getParentFile();
            File current = panel.current;
            refresh(parent);
            table.getSelectionModel().select(panel.getIndex(current));
        } else if (rowCommander.getFile().isDirectory()) {
            refresh(rowCommander.getFile());
            if (table.getSelectionModel().getSelectedItem() == null) {
                table.getSelectionModel().select(0);
            }
        }
    }

    public TableRowCommander getCurrentRow()
    {
        return table.getSelectionModel().getSelectedItem();
    }

    public String getCurrentDirectory()
    {
        return panel.current.getAbsolutePath();
    }

    public TablePanel getPanel()
    {
        return panel;
    }

    public TableView<TableRowCommander> getTable()
    {
        return table;
    }

    public void refresh()
    {
        System.out.println("reload panel");
        panel.scaner();
        ObservableList<TableRowCommander> rowsRefresh = FXCollections.observableArrayList(
            panel.getPanel()
        );
        table.setItems(rowsRefresh);
        table.refresh();
        path.setText(getPanel().current.getAbsolutePath());
    }

    public void refresh(File parentFile)
    {
        panel.setCurrent(parentFile.getAbsolutePath());
        refresh();
    }

    public Text getPath()
    {
        return path;
    }
}
