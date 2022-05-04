package net.packet;

import client.Chat;
import client.ChatRoom;
import client.Profile;
import database.DatabaseConnection;
import net.ByteBufWriter;
import net.packet.opcode.CInOpcode;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChatPacket {
    public static byte[] SendFriendList(int id) throws SQLException {
        var b = new ByteBufWriter();
        var connection = DatabaseConnection.getConnection();
        var profiles = new ArrayList<Profile>();
        //임시로 freinds 테이블을 구현하는거보다는 accounts테이블의 모든 계정을 가져오게 처리함
        var ps = connection.prepareStatement("select * from accounts where id != ?");
        ps.setInt(1, id);
        var rs = ps.executeQuery();
        while(rs.next()) {
            var profile = Profile.loadFromDB(rs);
            profiles.add(profile);
        }

        rs.close();
        ps.close();
        connection.close();
        b.writeOpcode(CInOpcode.FriendsListResult);
        b.writeInt(profiles.size());
        for (Profile profile : profiles) {
            profile.encode(b);
        }

        return b.getPacket();
    }

    public static byte[] SendChatRoomInfo(int toId, int fromId) throws SQLException {
        var w = new ByteBufWriter();
            w.writeOpcode(CInOpcode.ChatRoomResult);
        var chatRoom = ChatRoom.loadFromDB(toId, fromId);
        if(chatRoom.getCount() == 0) {
            chatRoom.addChat(new Chat(toId, System.currentTimeMillis(), "hello world"));
        }
        chatRoom.encode(w);

        return w.getPacket();
    }

    public static byte[] SendChat(int to, int from, String msg) {
        var b = new ByteBufWriter();
        b.writeOpcode(CInOpcode.SendChat);
        b.writeInt(to);
        b.writeInt(from);
        b.writeAsciiString(msg);
        return b.getPacket();
    }

}
