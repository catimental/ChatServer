import io.netty.buffer.Unpooled;
import net.ByteBufReader;
import net.ByteBufWriter;

import java.nio.ByteOrder;

public class Test {
    public static void main(String[] args) {
        //Unpooled.wrappedBuffer(byteArr).order(ByteOrder.LITTLE_ENDIAN)
        var writter = new ByteBufWriter();
        writter.write((byte) 1);
        writter.write((byte) 2);
        writter.write((byte) 3);

        writter.writeShort((byte) 1);
        writter.writeShort((byte) 2);
        writter.writeShort((byte) 3);
        System.out.println(writter);

        var bbr = new ByteBufReader(Unpooled.wrappedBuffer(writter.getPacket()).order(ByteOrder.LITTLE_ENDIAN));
        System.out.println(bbr.toString());

    }
}
