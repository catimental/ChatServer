package client;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientStorage {
    private final Map<String, Client> storage = new ConcurrentHashMap<>();

    public Client getClientFromIP(SocketAddress socketAddress) {
        return getClientFromIP(socketAddress.toString());
    }

    public Client getClientFromIP(String ip) {
        return storage.get(ip);
    }

    public void registerClient(Client client) {
        storage.put(client.toString(), client);
    }

    public void deRegisterClient(Client client) {
        storage.remove(client.getAccountId());
    }

    public boolean isExist(Client client) {
        return storage.containsKey(client.toString());
    }

}
