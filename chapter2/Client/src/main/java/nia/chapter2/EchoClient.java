package nia.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 类似与引导服务器,但引导客户端是使用主机和端口参数来连接远程地址,即Echo服务器的地址,
 * 而不是绑定到一个一直被监听的端口
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host,int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host,port).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            // 指定EventLoopGroup以处理客户端事件,需要适用于NIO的实现
            bootstrap.group(group)
                    // 适用于NIO传输的Channel类型
                    .channel(NioSocketChannel.class)
                    // 设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host,port))
                    // 在创建Channel时,向ChannelPipeline中添加一个EchoClientHandler实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 连接到远程节点,阻塞等待直到连接完成
            ChannelFuture future = bootstrap.connect().sync();
            // 阻塞,直到Channel关闭
            future.channel().closeFuture().sync();
        } finally {
            // 关闭线程池并释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

}
