package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @ClassName: NettyNioServer
 * @Decription: 使用Netty的异步网络处理
 *  Netty为每种传输(OIO,NIO)都暴露了同样的API,修改对应的实现或参数即可完成切换.
 *  同时,在所有的情况下,传输的实现都依赖于interface Channel / ChannelPipeline / ChannelHandler
 * @Author: nya
 * @Date: 18-10-15 上午11:23
 * @Version: 1.0
 **/
public class NettyNioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        // 为非阻塞模式使用NIOEventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        // Epoll -- 用于Linux的本地非阻塞传输
        //EventLoopGroup group1 = new EpollEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            // b.group(group).channel(EpollServerSocketChannel.class) -- for Epoll
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定ChannelInitializer,对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    // 添加ChannelInboundHandlerAdapter以接收和处理事件
                                    new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端,并添加ChannelFutureListener,以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(buf.duplicate())
                                        .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            // 绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();// 释放所有的资源
        }
    }

}
