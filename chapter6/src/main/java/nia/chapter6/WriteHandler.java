package nia.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: WriteHandler
 * @Decription: ChannelHandler和ChannelHandlerContext的高级用法:
 *  缓存到ChannelHandlerContext的引用以供稍后使用
 * @Author: nya
 * @Date: 18-10-26 下午1:32
 * @Version: 1.0
 **/
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    /**
     * 存储到ChannelHandlerContext的引用以供稍后使用
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 使用之前存储的到ChannelHandlerContext的引用来发送消息
     * @param msg
     */
    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }

}
