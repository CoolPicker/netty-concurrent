package nia.chapter6;

import io.netty.channel.*;

/**
 * @ClassName: OutboundExceptionHandler
 * @Decription: 处理出站异常 - 添加ChannelFutureListener到ChannelPromise
 * 推荐的出站异常处理实现方式
 * @Author: nya
 * @Date: 18-10-26 下午3:22
 * @Version: 1.0
 **/
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.cause().printStackTrace();
                    future.channel().close();
                }
            }
        });
    }
}
