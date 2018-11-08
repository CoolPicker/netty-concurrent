package nia.chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @ClassName: ChannelOperationExamples
 * @Decription: 基于netty实现写数据并将其冲刷到远程节点的常规任务实现测试.
 * 注意: Netty的Channel实现是线程安全的,所以可存储一个到Channel的引用,且每当需要向远程节点写数据时,
 * 都可使用此Channel引用,即使是多线程的场景下,且消息将会被保证按顺序发送
 * @Author: nya
 * @Date: 18-10-16 上午11:29
 * @Version: 1.0
 **/
public class ChannelOperationExamples {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    public static void writingToChannel() {

        Channel channel = CHANNEL_FROM_SOMEWHERE;
        // 创建并持有要写数据的ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("my datagram " , CharsetUtil.UTF_8);
        // 写数据并冲刷它
        ChannelFuture cf = channel.writeAndFlush(buf);
        // 添加ChannelFutureListener以便在写操作完成后接收通知
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("Write successful");
                } else {
                    System.err.println("Write error");
                    future.cause().printStackTrace();
                }
            }
        });

    }

    public static void writingToChannelFromManyThreads () {
        final Channel channel = CHANNEL_FROM_SOMEWHERE;
        final ByteBuf buf = Unpooled.copiedBuffer("my datagram",CharsetUtil.UTF_8).retain();
        Runnable writer = new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(buf.duplicate());
            }
        };
        // 获取到线程池Executor的引用
        Executor executor = Executors.newCachedThreadPool();
        // write in one thread
        executor.execute(writer);
        // write in another thread
        executor.execute(writer);
    }

}
