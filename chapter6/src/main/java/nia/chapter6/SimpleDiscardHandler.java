package nia.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: SimpleDiscardHandler
 * @Decription:
 *  Netty使用WARN级别的日志消息记录未释放的资源,使得可以简单地在代码中发现违规的实例.
 *  但如此管理资源的方式可能很繁琐.由此可选择使用SimpleChannelInboundHandler(自动释放资源)
 * @Author: nya
 * @Date: 18-10-25 下午2:08
 * @Version: 1.0
 **/
@ChannelHandler.Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler {


    /**
     * 消费并释放入站消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // No need to do anything special
        // 不需要任何显式的资源释放
    }
}
