package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/**
 * @ClassName: DummyChannelHandlerContext
 * @Decription: 虚拟一个ChannelHandlerContext的自定义实现类
 * @Author: nya
 * @Date: 18-10-26 上午11:33
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
                                      String name, boolean inbound, boolean outbound) {
        super(pipeline, executor, name, inbound, outbound);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }


}
