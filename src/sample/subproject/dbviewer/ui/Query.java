package sample.subproject.dbviewer.ui;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Query
{
    public ArrayList<String> query(String dbName, String query)
    {
        System.out.println("query - " + query);

        File file = new File("");
        ArrayList<String> result = new ArrayList<>();
        String url = "jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/") + "/"+dbName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();
                statement.execute(query);
                while (statement.getMoreResults()) {
                    ResultSet resultSet = statement.getResultSet();
                    String row = resultSet.getString(0);
                    System.out.println(row);
                    result.add(row);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
