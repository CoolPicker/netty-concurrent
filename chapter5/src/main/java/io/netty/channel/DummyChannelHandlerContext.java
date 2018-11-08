package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/**
 * @ClassName: DummyChannelHandlerContext
 * @Decription:
 * @Author: nya
 * @Date: 18-10-17 上午11:07
 * @Version: 1.0
 **/
public class DummyChannelHandlerContext extends AbstractChannelHandlerContext {
    public static ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(
            null,
            null,
            null,
            true,
            true
    );

    public DummyChannelHandlerContext(DefaultChannelPipeline pipeline,
                                      EventExecutor executor,
                                      String name,boolean inbound,boolean outbound) {
        super(pipeline,executor,name,inbound,outbound);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
