package constants;

import database.DatabaseConnection;

import java.sql.SQLException;

public class ChatHelper {

    public static void WriteChatToDB(int to, int from, String msg) throws SQLException {
        var connection = DatabaseConnection.getConnection();
        var ps = connection.prepareStatement("INSERT INTO chat_log (toId, fromId, spanedTime, spanedMessage) values (?, ?, ?, ?)");

        ps.setInt(1, to);
        ps.setInt(2, from);
        ps.setLong(3, System.currentTimeMillis());
        ps.setString(4, msg);

        ps.executeUpdate();
        ps.close();
        connection.close();
    }
}
