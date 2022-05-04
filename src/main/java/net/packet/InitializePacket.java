package net.packet;

import net.ByteBufWriter;
import net.packet.opcode.*;
public class InitializePacket {
    public static byte[] HandshakeResult() {
        var bw = new ByteBufWriter();
        bw.writeOpcode(CInOpcode.HandShakeResult);
        bw.writeAsciiString("reulst packet");
        return bw.getPacket();
    }
}
