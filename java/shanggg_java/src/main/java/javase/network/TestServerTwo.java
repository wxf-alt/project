package javase.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 服务器
public class TestServerTwo {
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
