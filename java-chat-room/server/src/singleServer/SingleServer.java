package singleServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SingleServer {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("等待客户端连接。。。。");
        Socket socket = serverSocket.accept();
        System.out.println("客户端连接，端口号为： "+socket.getLocalPort());

        PrintStream clientOutput = new PrintStream(socket.getOutputStream());
        clientOutput.flush();
        clientOutput.println("你好。客户端");

        Scanner clientInput = new Scanner(socket.getInputStream());
        String message = clientInput.next();
        System.out.println("客户端发送消息: "+message);



        clientInput.close();
        clientOutput.close();
        serverSocket.close();
    }
}
