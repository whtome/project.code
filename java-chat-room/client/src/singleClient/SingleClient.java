package singleClient;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SingleClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("127.0.0.1",6666);

        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println("你好，服务端！");
        printStream.flush();

        Scanner scanner = new Scanner(socket.getInputStream());
        System.out.println("服务端发送的消息： "+scanner.nextLine());

        printStream.close();
        scanner.close();
        socket.close();
    }
}
