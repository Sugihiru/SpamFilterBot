package DataProgram;

import java.sql.*;

public class SaveDataDB
{
    public static Connection createDB(String name) {
        Connection connection = null;
        try {
            String myDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(myDriver);
            connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/?user=root&password=root");
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name);

            connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/" + name + "?user=root&password=root");
            String myTableName = "CREATE TABLE Message ("
                    + "idNo INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "message VARCHAR(10000),"
                    + "value BOOLEAN)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(myTableName);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection connectionDB(String name)
    {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + name, "root", "root")) {
            System.out.println("Database connected!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(Connection connection, String message, boolean yesNo) {

        String query = " insert into Message (message, value)"
                + " values (?, ?)";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, message);
            preparedStmt.setBoolean (2, yesNo);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
