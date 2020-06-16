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
                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("Not found");
                        return result;
                    }

                    while (resultSet.next()) {
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        Row row = new Row();
                        row.cells = new ArrayList<>();
                        System.out.println("Column count=" + resultSetMetaData.getColumnCount());
                        System.out.println("Column row=" + resultSet.getRow());
                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                            String value = resultSet.getString(i);
                            System.out.println("Column=" + resultSetMetaData.getCatalogName(i));
                            System.out.println("Column value=" + value);
                            System.out.println("Column type=" + resultSetMetaData.getColumnType(i));
                            System.out.println("Column type name=" + resultSetMetaData.getColumnTypeName(i));
                            System.out.println("Column classname=" + resultSetMetaData.getColumnClassName(i));
                            System.out.println("Column table=" + resultSetMetaData.getTableName(i));
                            System.out.println("Column label=" + resultSetMetaData.getColumnLabel(i));

                            Cell cell = new Cell();
                            cell.name = resultSetMetaData.getColumnName(i);
                            cell.value = value;
                            row.cells.add(cell);
                        }
                        result.add(row);

                    }
                    System.out.println("rows=" + result.size());
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
