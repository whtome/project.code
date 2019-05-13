package MultiClient;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ReadFromServerYThread extends Thread{
    private final Socket client;

    public ReadFromServerYThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
            try {
                Scanner scanner = new Scanner(this.client.getInputStream());
                while (true) {
                    String message = scanner.nextLine();
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
