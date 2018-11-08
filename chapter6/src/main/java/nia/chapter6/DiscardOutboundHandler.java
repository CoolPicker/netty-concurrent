package nia.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: DiscardOutboundHandler
 * @Decription: 出站方向上,write()操作将丢弃一个消息,并释放
 *  注意:不仅要释放资源,还要通知ChannelPromise.
 *  否则可能出现ChannelFutureListener收不到某个消息已经被处理了的通知情况
 * @Author: nya
 * @Date: 18-10-25 下午2:54
 * @Version: 1.0
 **/
@ChannelHandler.Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 通过使用ReferenceCountUtil.release()方法释放资源
        ReferenceCountUtil.release(msg);
        // 通知ChannelPromise,数据已经被处理
        promise.setSuccess();
    }
}
