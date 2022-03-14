package javase.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
