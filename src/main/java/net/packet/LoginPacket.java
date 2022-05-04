package net.packet;

import client.Profile;
import net.ByteBufWriter;
import net.packet.opcode.CInOpcode;

import java.sql.SQLException;

public class LoginPacket {
    public static byte[] loginResult(boolean isSuccess, String accountId, int uid) throws SQLException {
        var w = new ByteBufWriter();
        w.writeOpcode(CInOpcode.LoginResult);
        w.writeBoolean(isSuccess);
        w.writeAsciiString(accountId);
        Profile.loadFromDB(uid).encode(w);

        return w.getPacket();
    }

    public static byte[] registrationResult(boolean isSuccess) {
        var w = new ByteBufWriter();
        w.writeOpcode(CInOpcode.RegistrationResult);
        w.writeBoolean(isSuccess);
        return w.getPacket();
    }
}
