package nia.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName: SharableHandler
 * @Decription: 可共享的ChannelHandler
 * 因为一个ChannelHandler可以从属于多个ChannelPipeline,所以它可以绑定到多个ChannelHandlerContext实例.
 * 对于这种用法,对应的ChannelHandler必须要使用@Sharable注解标注;
 * 否则,试图将它添加到多个ChannelPipeline时将会触发异常.
 * 同时,为了安全地用于多个并发的Channel(即连接),
 *  这样的ChannelHandler必须是线程安全的.
 * @Author: nya
 * @Date: 18-10-26 下午1:39
 * @Version: 1.0
 **/
@ChannelHandler.Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Channel read message:" + msg);
        ctx.fireChannelRead(msg);
    }
}
