package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final ThreadLocal<Connection> con = new ThreadLocalConnection();

    public static Connection getConnection() {
        try {
            Connection c = con.get();
            if (c == null || !c.isValid(0)) {
                con.set(null);
                con.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con.get();
    }


    private static class ThreadLocalConnection extends ThreadLocal<Connection> {

        @Override
        protected Connection initialValue() {
            final var driver = "com.mysql.jdbc.Driver";
            final var schema = "chatserver";
            final var url = "jdbc:mysql://localhost:3306/" + schema + "?autoReconnect=true&characterEncoding=euckr&maxReconnects=5";
            final var user = "root";
            final var password = "root";

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}