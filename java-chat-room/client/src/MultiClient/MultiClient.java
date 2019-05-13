package MultiClient;

import java.net.Socket;

public class MultiClient {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8080 ;

        Socket socket = new Socket(host,port);

        Thread write = new WriteToServerThread(socket);
        write.setName("Thread-Client-Write");
        write.start();

        Thread read = new ReadFromServerYThread(socket);
        read.setName("Thread-Client-Read");
        read.start();
    }
}
