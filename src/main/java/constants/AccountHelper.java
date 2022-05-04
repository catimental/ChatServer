package constants;

import database.DatabaseConnection;

import java.sql.SQLException;

public class AccountHelper {
    public static boolean isExistAccount(String accountId) throws SQLException {
        var conncetion = DatabaseConnection.getConnection();
        var ps = conncetion.prepareStatement("SELECT * FROM accounts where accountId = ?");
        ps.setString(1, accountId);

        var rs = ps.executeQuery();
        var isExist = rs.next();
        ps.close();
        rs.close();
        conncetion.close();
        return isExist;
    }

    public static void createNewAccount(String accountId, String password) throws SQLException {
        var connection = DatabaseConnection.getConnection();
        var ps = connection.prepareStatement("INSERT INTO accounts (accountId, password) values(?, ?)");

        ps.setString(1, accountId);
        ps.setString(2, password);

        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    public static boolean loginAccountCheck(String accountId, String password) throws SQLException {
        var connection = DatabaseConnection.getConnection();
        var ps = connection.prepareStatement("SELECT * FROM accounts where accountId = ?");
        ps.setString(1, accountId);
        var rs = ps.executeQuery();
        String dbPassword = null;
        if(rs.next()) {
            dbPassword = rs.getString("password");
        }
        rs.close();
        ps.close();
        connection.close();

        return dbPassword.equals(password);
    }

    public static int getAccountUID(String accountId) throws SQLException {
        var connection = DatabaseConnection.getConnection();
        var ps = connection.prepareStatement("SELECT * FROM accounts where accountId = ?");
        ps.setString(1, accountId);
        var rs = ps.executeQuery();
        int uid = -1;
        if(rs.next()) {
            uid = rs.getInt("id");
        }
        rs.close();
        ps.close();
        connection.close();
        return uid;
    }
}
