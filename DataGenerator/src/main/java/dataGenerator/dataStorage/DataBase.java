package dataGenerator.dataStorage;

import javafx.concurrent.Task;
import java.sql.*;

public class DataBase
{
    private Connection connection = null;
    private Thread worker;
    private WorkerRunnable workerRunnable;


    public DataBase()
    {
        this.workerRunnable = new WorkerRunnable();
        this.worker = new Thread(this.workerRunnable);
        worker.start();
    }

    public void createDB(String name) {
        try {
            closeConnection();
            String myDriver = "com.mysql.cj.jdbc.Driver";
            Class.forName(myDriver);
                this.connection = DriverManager.getConnection
                        ("jdbc:mysql://localhost:3306/?user=root&password=root");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name);
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
        try
        {
            closeConnection();
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
            synchronized (this.connection) {
                preparedStmt = this.connection.prepareStatement(query);
                preparedStmt.setString(1, message);
                preparedStmt.setBoolean(2, yesNo);
                preparedStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void write(String messsage, boolean yesNo) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    if (messageAlreadyExists(messsage))
                        update(messsage, yesNo);
                    else
                        writeMessage(messsage, yesNo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        workerRunnable.queueTask(task);
    }

    public Boolean getValue(String message) throws SQLException {
        final ResultSet resultSet = findmessageDB(connection, message);
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

    private ResultSet findmessageDB(Connection connection, String message) throws SQLException {
        String query = "SELECT * from message WHERE message = ?";
        ResultSet resultSet;
        synchronized (this.connection) {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, message);
            resultSet = ps.executeQuery();
        }
        return resultSet;
    }

    public boolean messageAlreadyExists(String message) throws SQLException {
        final ResultSet resultSet = findmessageDB(connection, message);
        return resultSet.isBeforeFirst();
    }

    private void stopThread() throws InterruptedException {
        this.workerRunnable.stop();
        this.worker.join();
    }

    private void closeConnection() throws SQLException {
        if (this.connection != null)
        this.connection.close();
    }

    public void disconnect() {
        try {
            stopThread();
            closeConnection();
            this.connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
