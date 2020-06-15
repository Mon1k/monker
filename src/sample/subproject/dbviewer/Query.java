package sample.subproject.dbviewer;

import sample.subproject.dbviewer.resultset.Cell;
import sample.subproject.dbviewer.resultset.Row;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Query
{
    String database;
    ArrayList<Row> result;

    public Query(String database)
    {
        this.database = database;
    }

    public ArrayList<Row> exec(String query)
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

                if (sample.utils.String.ContainsIgnoreCase(query, "select")) {
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
