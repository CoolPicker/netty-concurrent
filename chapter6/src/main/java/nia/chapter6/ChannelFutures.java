package nia.chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName: ChannelFuture
 * @Decription: 处理出站异常 - 添加CHannelFutureListener到ChannelFuture
 * 1.每个出站操作都将返回一个ChannelFuture,注册到ChannelFuture的ChannelFutureListener将在操作完成时被通知该操作是成功了还是出错了
 * 2.几乎所有的ChannelOutboundHandler上的方法都会传入一个ChannelPromise的实例.作为ChannelFuture的子类,ChannelPromise也可以被分配用于异步通知的监听器
 * 但出站异常的处理,不推荐下述方法,不够简便,推荐OutboundExceptionHandler中定义的方法
 * @Author: nya
 * @Date: 18-10-26 下午3:16
 * @Version: 1.0
 **/
public class ChannelFutures {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ByteBuf SOME_MSG_FROM_SOMEWHERE = Unpooled.buffer(1024);

    public static void addingChannelFutureListener() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBuf someMessage = SOME_MSG_FROM_SOMEWHERE;
        // ...
        ChannelFuture future = channel.write(someMessage);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });

    }
}
