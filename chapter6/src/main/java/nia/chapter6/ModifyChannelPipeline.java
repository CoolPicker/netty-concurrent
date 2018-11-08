package nia.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import static io.netty.channel.DummyChannelPipeline.DUMMY_INSTANCE;

/**
 * @ClassName: ModifyChannelPipeline
 * @Decription: 测试调用,ChannelPipeline的相关修改方法,
 * 这些方法可轻松实现极其灵活的逻辑
 * @Author: nya
 * @Date: 18-10-25 下午3:28
 * @Version: 1.0
 **/
public class ModifyChannelPipeline {

    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DUMMY_INSTANCE;

    public static void modifyChannelPipeline() {
        ChannelPipeline pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE;
        FirstHandler firstHandler = new FirstHandler();
        pipeline.addLast("handler1",firstHandler);
        pipeline.addFirst("handler2",new SecondHandler());
        pipeline.addLast("handler3",new ThirdHandler());
        // ...
        pipeline.remove("handler3");// 通过名称移除
        pipeline.remove(firstHandler); // 通过引用移除
        pipeline.replace("handler2","handler4",new ForthHandler());
    }

    private static final class FirstHandler extends ChannelHandlerAdapter {}

    private static final class SecondHandler extends ChannelHandlerAdapter {}

    private static final class ThirdHandler extends ChannelHandlerAdapter {}

    private static final class ForthHandler extends ChannelHandlerAdapter {}

}
