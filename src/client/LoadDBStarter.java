package client;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

public class LoadDBStarter {
    private Map<Integer, Object> clients = new HashMap<>();

    public LoadDBStarter() {
        super();
    }

    public void InitClients(int numClients) {

        DBClient client;
        for (int i = 1; i <= numClients; i++) {
            client = new DBClient("DBClient#" + String.valueOf(i));
            /*Инициализируем сессию*/
            clients.put(i, client);
            System.out.println("Init " + client.getNameClient());
        }
    }

    public void StartClients() {
        DBClient client;
        for (Map.Entry entry : clients.entrySet()) {
            client = (DBClient) entry.getValue();
            client.start();
        }
    }

    public void StopClients() {
        DBClient client;
        for (Map.Entry entry : clients.entrySet()) {
            client = (DBClient) entry.getValue();
            client.interrupt();
        }
    }

}
