package io.netty.channel;

/**
 * @ClassName: DummyChannelPipeline
 * @Decription: 模拟出ChannelPipeline实例
 * @Author: nya
 * @Date: 18-10-25 下午3:25
 * @Version: 1.0
 **/
public class DummyChannelPipeline extends DefaultChannelPipeline {

    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);

    public DummyChannelPipeline(Channel channel) {
        super(channel);
    }

}
