package nia.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName: InboundExceptionHandler
 * @Decription: 基本的入站异常处理
 * 1.ChannelHandler.exceptionCaught()的默认实现是简单地将当前异常转发给ChannelPipeline的下一个ChannelHanler
 * 2.如果异常到达了ChannelPipeline的尾端,它将会被记录为未被处理
 * 3.要想实现自定义的处理逻辑,需要重写exceptionCaught()方法
 * @Author: nya
 * @Date: 18-10-26 下午3:14
 * @Version: 1.0
 **/
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
