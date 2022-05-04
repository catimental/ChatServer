package client;

import database.DatabaseConnection;
import io.netty.buffer.ByteBuf;
import net.ByteBufWriter;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile {

    private int id;
    private String displayName;
    private String description;

    public Profile(int id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    public static Profile loadFromDB(int id) throws SQLException {
        var connection = DatabaseConnection.getConnection();
        var profile = Profile.loadFromDB(connection, id);
        connection.close();
        return profile;
    }

    public static Profile loadFromDB(Connection connection, int id) throws SQLException {
        var ps = connection.prepareStatement("select * from accounts where id = ?");
        Profile profile = null;
        ps.setInt(1, id);

        var rs = ps.executeQuery();

        if(rs.next()) {
            profile = Profile.loadFromDB(rs);
        }

        rs.close();
        ps.close();
        return profile;
    }

    public static Profile loadFromDB(ResultSet rs) throws SQLException {
        return new Profile(
                rs.getInt("id"),
                rs.getString("displayName"),
                rs.getString("displayDescription")
        );
    }

    public void encode(ByteBufWriter b) {
        b.writeInt(this.id);
        b.writeAsciiString(displayName);
        b.writeAsciiString(description);
    }
}
