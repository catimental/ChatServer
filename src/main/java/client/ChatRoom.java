package client;

import database.DatabaseConnection;
import net.ByteBufWriter;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatRoom {
    private int to;
    private int from;
    private ArrayList<Chat> chats= new ArrayList<>();

    public ChatRoom(int to, int from) {
        this.to = to;
        this.from = from;
    }

    public void encode(ByteBufWriter w) {
        w.writeInt(to);
        w.writeInt(from);
        w.writeInt(chats.size());
        for (Chat chat : chats) {
            chat.encode(w);
        }
    }

    public static ChatRoom loadFromDB(int to, int from) throws SQLException {
        var chatRoom = new ChatRoom(to, from);
        var connection = DatabaseConnection.getConnection();
        var ps = connection.prepareStatement("" +
                "select * " +
                "from chat_log " +
                "where (toId = ? and fromId = ?) or (toId = ? and fromId = ?)");
        ps.setInt(1, to);
        ps.setInt(2, from);
        ps.setInt(3, from);
        ps.setInt(4, to);

        var rs = ps.executeQuery();

        while(rs.next()) {
            chatRoom.addChat(Chat.loadFromResultSet(rs));
        }
        rs.close();
        ps.close();
        connection.close();
        return chatRoom;
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public int getCount() {
        return chats.size();
    }
}
