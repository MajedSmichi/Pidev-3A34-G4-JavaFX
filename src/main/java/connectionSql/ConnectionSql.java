package connectionSql;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionSql {
    public Connection databaseLinck;

    private Connection cnx;
    private static ConnectionSql instance;

    public Connection getConnection() {
        String databaseName = "pidev";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            databaseLinck = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("connect to the database.");

            return databaseLinck;
        } catch (Exception e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        // Return null if connection fails
        return null;
    }

    public Connection getCnx() {
        return cnx;
    }

    public static ConnectionSql getInstance() {
        if (instance == null) {
            instance = new ConnectionSql();
        }
        return instance;
    }
}

