package DataProgram;

import java.sql.*;

public class DataBase
{
    private Connection connection = null;

    public void createDB(String name) {
        disconnect();
        try {
            String myDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(myDriver);
            this.connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/?user=root&password=root");
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name);
            disconnect();
            connectionDB(name);
            String myTableName = "CREATE TABLE IF NOT EXISTS  message ("
                    + "idNo INT(64) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "message VARCHAR(10000),"
                    + "value BOOLEAN)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(myTableName);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connectionDB(String name)
    {
        disconnect();
        try
        {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/" + name, "root", "root");
            System.out.println("Database connected!");
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    private void writeMessage(String message, boolean yesNo) {

        String query = " insert into message (message, value)"
                + " values (?, ?)";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = this.connection.prepareStatement(query);
            preparedStmt.setString (1, message);
            preparedStmt.setBoolean (2, yesNo);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void write(String messsage, boolean yesNo)
    {
        try{
            if (messageAlreadyExists(messsage))
                update(messsage, yesNo);
            else
                writeMessage(messsage, yesNo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean getValue(String message) throws SQLException {
        String query = "SELECT * from message WHERE message = ?";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, message);
        final ResultSet resultSet = ps.executeQuery();
        if (resultSet.isBeforeFirst()) {
            resultSet.next();
            return resultSet.getBoolean("value");
        }
        return null;
    }

    private void update(String message, boolean yesNo) throws SQLException {
        String query = "UPDATE message t1 SET t1.value = ? WHERE t1.message = ?";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setBoolean(1, yesNo);
        ps.setString(2, message);
        ps.execute();
    }

    public boolean messageAlreadyExists(String message) throws SQLException {
        String query = "SELECT * from message WHERE message = ?";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, message);
        final ResultSet resultSet = ps.executeQuery();
        if (!resultSet.isBeforeFirst())
            return false;
        return true;
    }

    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
