package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by Andreas on 2017-03-20.
 */
public class DatabaseConnection {

    // JDBC driver name and database URL
    static final String DB_URL = "jdbc:mysql://localhost:3306/heroes";

    // Database credentials
    static final String USER = "user";
    static final String PASS = "user";

    public Connection Connect() throws SQLException {

        Connection connection = null;

        try {
            // Open a connection
            System.out.println("- Hal: Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("- Hal: Connected to database successfully...");
        } catch (SQLException e) {
            System.err.println(e);
        }


        return connection;
    }
}
