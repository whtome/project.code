package MultiServer;

import com.sun.javafx.collections.MappingChange;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHander implements Runnable {

    public static final Map<Socket,String> SOCKET_MAP = new ConcurrentHashMap<>();

    private Socket client;

    public ClientHander(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(this.client.getInputStream());
            while (true) {
                String line = scanner.nextLine();
                //注册
                if (line.startsWith("register")) {
                    String[] strings = line.split(":");
                    if (strings.length == 2 && strings[0].equals("register")) {
                        String name = strings[1];
                        register(name);
                    }
                    continue;
                }

                //群聊
                if (line.startsWith("group")) {
                    String[] strings = line.split(":");
                    if (strings.length == 2 && strings[0].equals("group")) {
                        String message = strings[1];
                        groupChat(message);
                    }
                    continue;
                }

                //私聊
                if (line.startsWith("private")) {
                    String[] strings = line.split(":");
                    if (strings.length == 3 && strings[0].equals("private")) {
                        String name = strings[1];
                        String message = strings[2];
                        privateChat(name,message);
                    }
                    continue;
                }

                //退出
                if (line.startsWith("quit")) {
                    quit();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(String name) {
        SOCKET_MAP.put(this.client,name);
        this.sendMessage(this.client,"恭喜<"+name+">注册成功");
        printOnClient();
    }

    private  void sendMessage(Socket socket,String message) {
        try {
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(message);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void printOnClient() {
        System.out.println("当前客户端个数："+SOCKET_MAP.size()+"  名称列表如下");
        for(String volue : SOCKET_MAP.values()) {
            System.out.println(volue);
        }
    }

    private  void groupChat(String message) {
        //群聊
        //发送消息给当前在线客户端，不包含自己
        //谁 发 消息
        Set<Map.Entry<Socket,String>> entrySet = SOCKET_MAP.entrySet();
        String currentName = "";
        for (Map.Entry<Socket,String> entry : entrySet) {
            Socket socket = entry.getKey();
            String value = entry.getValue();
            if (socket == this.client) {
                currentName = value;
            }
        }

        for (Map.Entry<Socket,String> entry : entrySet) {
            Socket socket = entry.getKey();
            if (socket != this.client) {
                this.sendMessage(socket,currentName+"说>"+message);
            }
        }
    }

    private  void privateChat(String name,String message) {
        //私聊
        //发送消息给当前在线客户端，不包含自己
        //谁 发 消息
        Set<Map.Entry<Socket,String>> entrySet = SOCKET_MAP.entrySet();
        String currentName = "";
        for (Map.Entry<Socket,String> entry : entrySet) {
            Socket socket = entry.getKey();
            String value = entry.getValue();
            if (socket == this.client) {
                currentName = value;
            }
        }
//        String value = SOCKET_MAP.get(name);
//        Socket socket = SOCKET_MAP.
//        if (value != null) {
//            this.sendMessage(socket,currentName+"说>"+message);
//        }
        for (Map.Entry<Socket,String> entry : entrySet) {
            Socket socket = entry.getKey();
            String value = entry.getValue();
            if (value.equals(name)) {
                this.sendMessage(socket,currentName+"说>"+message);
            }
        }
    }

    private  void quit() {
        //退出
        Iterator<Map.Entry<Socket,String>> iterator = SOCKET_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Socket,String> entry = iterator.next();
            if (entry.getKey() == this.client) {
                System.out.println(entry.getValue()+"退出");
                iterator.remove();
                break;
            }
        }
        printOnClient();
    }
}
