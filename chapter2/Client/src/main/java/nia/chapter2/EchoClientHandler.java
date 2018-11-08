package nia.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 同服务器,客户端亦拥有一个用来处理数据的ChannelInboundHandler.
 * 在此场景下,将扩展SimpleChannelInboundHandler类以处理所有必须的任务.
 * 重写以下方法:
 *  1.channelActive() -- 在到服务器的连接已经建立之后将被调用
 *  2.channelRead0() -- 当从服务器接收到一条消息时被调用
 *  3.exceptionCaught() -- 在处理过程中引发异常时被调用
 */
// 标记该类的实例可以被多个Channel共享
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知Channel是活跃的时候,发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        // 记录已接收消息的存储
        System.out.println("Client received : " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当发生错误时,记录错误并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }
}
