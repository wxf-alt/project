package javase.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 服务器
public class TestServer {
    public static void main(String[] args) {
        try {
            // 创建服务器 Socket 类,用于监听客户端请求
            ServerSocket ss = new ServerSocket(6666);
            // 监听
            Socket socket = ss.accept();
            // 和 socket建立输入流，就是从socket中读取数据，socket封装了客户端传递的数据
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            System.out.println("客户端，这里是服务器，我已经收到你的信息 -》" + line);
            // 关闭输入流
            socket.shutdownInput();

            // 开启输出流响应客户端
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("服务器开始响应客户端");
            bw.flush();
            bw.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
