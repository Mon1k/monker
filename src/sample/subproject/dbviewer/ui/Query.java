package sample.subproject.dbviewer.ui;

import sample.subproject.dbviewer.resultset.Cell;
import sample.subproject.dbviewer.resultset.Row;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Query
{
    public ArrayList<Row> query(String dbName, String query)
    {
        System.out.println("query - " + query);

        File file = new File("");
        ArrayList<Row> result = new ArrayList<>();
        String url = "jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/") + "/" + dbName;
        System.out.println("database - " + url);
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();
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
                statement.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
