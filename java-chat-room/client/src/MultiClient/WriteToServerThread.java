package MultiClient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WriteToServerThread extends Thread {

    private final Socket client;

    public WriteToServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            Scanner scanner = new Scanner(System.in);

            PrintStream printStream = new PrintStream(this.client.getOutputStream());

            while(true) {
                System.out.print("请输入> ");
                String message = scanner.nextLine();
                printStream.println(message);
                if (message.equals("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
