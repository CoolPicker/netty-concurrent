package nia.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @ClassName: PlainOioServer
 * @Decription: 基于JDK API完成application OIO(阻塞)的实现,可以处理中等数量的并发客户端,
 *  但不能很好的伸缩到支撑成千上万的并发连入连接, -> 改用异步网络编程
 *  未使用Netty的阻塞网络编程
 * @Author: nya
 * @Date: 18-10-15 上午10:42
 * @Version: 1.0
 **/
public class PlainOioServer {

    public void serve(int port) throws IOException {
        // 将服务器绑定到指定端口
        final ServerSocket socket = new ServerSocket(port);
        try {
            // for(;;) -> 死循环
            for (;;) {
                // 接受连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                // 创建一个新的线程来处理该连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                // ignore on close
                            }
                        }
                    }
                }).start();// 启动线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int i = 0;
        for (;;) {
            i++;
            System.out.println(i);
        }
    }

}
