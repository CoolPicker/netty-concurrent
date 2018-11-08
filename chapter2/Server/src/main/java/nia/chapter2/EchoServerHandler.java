package nia.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * ChannelHandler --- 负责接受和响应事件通知,即业务逻辑
 * 除 ChannelInboundHandlerAdapter 外,需学习的ChannelHandler的子类型和实现还有很多,
 * 关键点如下:
 * 1.针对不同类型的事件来调用ChannelHandler
 * 2.应用程序通过实现或者扩展ChannelHandler来挂钩到事件的生命周期,并且提供自定义的应用程序逻辑
 * 3.在架构上,CHannelHandler有助于保持业务逻辑与网络处理代码的分离
 */
// 标示一个ChannelHandler可以被多个Channel安全地共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in); // 将接受到的消息写给发送者,而不冲刷出站消息
    }

    /**
     * 通知ChannelInboundHandlerAdapter最后一次对 channelRead()的调用
     *  是当前批量读取中的最后一条消息
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将未决消息冲刷到远程节点,并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间,有异常抛出时会调用
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
