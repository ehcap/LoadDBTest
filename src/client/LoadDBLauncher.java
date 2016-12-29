package client;

import java.io.IOException;

public class LoadDBLauncher {

    public LoadDBLauncher() {


        super();
    }

    public static void main(String[] args) {

        LoadDBStarter dbs = new LoadDBStarter();

        dbs.InitClients(20);
        dbs.StartClients();
        System.out.println("Клиенты запущены");
        System.out.println("Для остановки введите любой символ");
        try {
            //char inChar = (char)System.in.read();
            Thread.sleep(300 * 1000);
        } catch (InterruptedException e) {
        }
        dbs.StopClients();
        //} catch (IOException e) {
        //    System.out.println("Input Error");}
    }
}
                                             