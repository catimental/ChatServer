package client;

import net.ByteBufWriter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Chat {
    private int from;
    private long time;
    private String msg;

    public Chat(int from, long time, String msg) {
        this.from = from;
        this.time = time;
        this.msg = msg;
    }

    public void encode(ByteBufWriter w) {
        w.writeInt(from);
        w.writeLong(time);
        w.writeAsciiString(msg);
    }

    public static Chat loadFromResultSet(ResultSet rs) throws SQLException {
        return new Chat(
                rs.getInt("fromId"),
                rs.getLong("spanedTime"),
                rs.getString("spanedMessage")
        );
    }

    public int getFrom() {
        return from;
    }

    public String getMsg() {
        return msg;
    }

    public long getTime() {
        return time;
    }
}
