package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: DiscardHandler
 * @Decription: ChannelHandler -> ChannelInboundHandler -> ChannelInboundHandlerAdapter
 *              -> channelRead
 *              channelRead方法被重写时,将负责显式释放与池化的ByteBuf实例相关的内存
 * @Author: nya
 * @Date: 18-10-25 下午2:00
 * @Version: 1.0
 **/
@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {

    /**
     * 消费并释放入站消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 此处对象类型,理论上应为ReferenceCount类型 , 专用于引用计数 , 即释放ReferenceCount , 完成丢弃已接收消息的作用
        ReferenceCountUtil.release(msg);
    }
}
