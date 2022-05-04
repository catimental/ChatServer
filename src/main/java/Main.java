import constants.ServerConstants;
import net.Server;

import java.net.InetSocketAddress;

public class Main {
    private static Server server;
    public static void main(String[] args) {
        server = new Server(ServerConstants.PORT);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
