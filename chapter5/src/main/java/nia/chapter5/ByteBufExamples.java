package nia.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * @ClassName: ByteBufExamples
 * @Decription: ByteBuf - Netty的数据容器 - 示例
 * @Author: nya
 * @Date: 18-10-17 上午11:03
 * @Version: 1.0
 **/
public class ByteBufExamples {
    private final static Random random = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    // private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;

    /**
     * 堆缓冲区的使用,将数据存储在JVM的堆空间中 - backing array 支撑数组模式
     * 能在没有使用池化的情况下提供快速的分配和释放
     */
    public static void heapByffer(){
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
        // 检查ByteBuf是否有一个支撑数组
        // 预先知道容器中的数据将被作为数组来访问,推荐使用堆缓冲区
        if (heapBuf.hasArray()) {
            // 获取对该数组的引用
            byte[] array = heapBuf.array();
            // 计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            // 获取可读字节数
            int length = heapBuf.readableBytes();
            // 使用数组/偏移量/长度作为参数调用本地方法
            handleArray(array,offset,length);
        }
    }

    /**
     * 直接缓冲区的内容在常规会被垃圾回收的堆之外.
     * 主要缺点: 相对于基于堆的缓冲区,直接缓冲区的分配和释放都较为昂贵.
     */
    public static void directBuffer(){
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        // 检查ByteBuf是否由数组支撑.如果不是,则是一个直接缓冲区
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            // 分配一个新的数组来保存具有该长度的字节数据
            byte[] array = new byte[length];
            // 将字节复制到该数组
            directBuf.getBytes(directBuf.readerIndex(),array);
            handleArray(array,0,length);
        }
    }

    /**
     * 使用ByteBuffer的复合缓冲区模式
     * @param header
     * @param body
     */
    public static void byteBufferComposite(ByteBuffer header , ByteBuffer body) {
        // use an array to hold the message parts
        ByteBuffer[] message = new ByteBuffer[]{header,body};

        // create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }

    /**
     * 使用CompositeByteBuf的复合缓冲区模式,(CompositeByteBuf可能不支持访问其支撑数组)
     */
    public static void byteBufComposite(){
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;// can be backing or direct
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE; // can be backing or direct
        messageBuf.addComponents(headerBuf,bodyBuf);
        // ...
        messageBuf.removeComponent(0); // remove the header
        for (ByteBuf buf :
                messageBuf) {
            System.out.println(buf.toString());
        }
    }

    /**
     * 针对堆缓冲区 backing array的优化
     * 注:Netty使用了CompositeByteBuf来优化套接字的I/O操作,尽可能地消除了由JDK的缓冲区实现所导致的性能以及内存使用率的惩罚.
     */
    public static void byteBufCompositeArray(){
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(),array);
        handleArray(array,0,array.length);
    }

    public static void byteBufRelativeAccess(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        for (int i = 0 ; i < buffer.capacity() ; i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    public static void readAllData(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        // 获取所有可读字节
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    public static void write(){
        // fills the writable bytes of a buffer with random integers
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while (buffer.writableBytes() > 4) {
            buffer.writeInt(random.nextInt());
        }
    }

    /**
     * 对ByteBuf进行切片 slice
     * 这里使用了assert关键字,断言,一种调试方法,判定更新主容器内容,分段切片容器的变化
     * 注意: 切片 slice 是数据共享的
     */
    public static void byteBufSlice(){
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!",utf8);
        // 创建该ByteBuf从索引0开始到索引15结束的新切片
        ByteBuf sliced = buf.slice(0,15);
        System.out.println(sliced.toString(utf8));
        // 更新索引0处的字节
        buf.setByte(0,(byte)'J');
        // 将会成功,因为数据是共享的,对其中一个所做的更改对另外一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0) : "assert slice fail";
        System.out.println("assert slice success");
    }

    /**
     * 对ByteBuf进行复制 , 注意 推荐使用slice()切片的形式,避免复制内存的开销
     * 注意: 复制 copy 是数据不共享的
     */
    public static void byteBufCopy(){
        Charset utf8 = Charset.forName("UTF-8");
        // 创建ByteBuf以保存所提供的字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks !",utf8);
        // 创建该ByteBuf从索引0开始到索引15结束的分段的副本
        ByteBuf copy = buf.copy(0,15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0,(byte)'J');
        assert buf.getByte(0) != copy.getByte(0) : "assert copy failure";
        System.out.println("assert copy success");
    }

    /**
     * get()和set()操作,从给定的索引开始,并且保持索引不变
     */
    public static void byteBufGetSet(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks !" ,utf8);
        System.out.println((char)buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0,(byte)'B');
        System.out.println((char)buf.getByte(0));
        assert readerIndex == buf.readerIndex() : "reader index already changed";
        System.out.println("reader index never changed");
        assert writerIndex == buf.writerIndex() : "writer index already changed";
        System.out.println("writer index never changed");
    }

    /**
     * read() / write() , 从给定的索引开始,并且会根据已经访问过的字节数对索引进行调整
     */
    public static void byteBufReadWrite() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!",utf8);
        System.out.println((char)buf.readByte());
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte)'?');
        System.out.println(writerIndex);
        System.out.println((char)buf.getByte(writerIndex));
        assert readerIndex == buf.readerIndex() : "reader index already changed";
        System.out.println("reader index now no changed");
        assert writerIndex == buf.writerIndex() : "writer index already changed";
        System.out.println("writer index now no changed");
    }

    /**
     * why : 为了降低分配和释放内存的开销,Netty通过interface ByteBufAllocator 实现了ByteBuf的池化
     * what: 用来分配任意类型的ByteBuf实例
     * how: Netty提供了两种ByteBufAllocator的实现: PooledByteBufAllocator 和 UnpooledByteBufAllocator
     *  PooledByteBufAllocator池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片 注:使用jemalloc来分配内存 (默认使用)
     *  UnpooledByteBufAllocator的实现不池化ByteBuf实例,并且每次被调用时都会返回一个新的实例
     */
    public static void obtainingByteBufAllocatorReference() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        // 从Channel中获取一个ByteBufAllocator的引用
        ByteBufAllocator allocator = channel.alloc();
        // ...
        // ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        // 从ChannelHandlerContext获取一个到ByteBufAllocator的引用
        // ByteBufAllocator allocator2 = ctx.alloc();
    }

    /**
     * Reference counting 引用计数技术在ByteBuf / ByteBufHolder上的使用
     * 引用计数 主要 涉及跟踪到某个特定对象的活动引用的数量.
     * reference counting 对于池化实现(eg:PooledByteBufAllocator)来说是至关重要的,
     *  它降低了内存分配的开销
     */
    public static void referenceCounting(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();

        // ...

        ByteBuf byteBuf = allocator.directBuffer();
        // 检查引用计数是否为预期的1
        assert byteBuf.refCnt() == 1 : "reference count is not meet expectation";
    }

    /**
     * Release reference-counted object
     * 释放引用计数的对象
     * 试图访问一个已经释放的引用计数的对象,将会导致 IllegalReferenceCountException - 非法引用计数异常
     */
    public static void releaseReferenceCountedObject(){
        ByteBuf buf = BYTE_BUF_FROM_SOMEWHERE;
        // 减少该对象的活动引用.当减少到0时,该对象被释放,并且该方法返回true
        boolean released = buf.release();
    }


    private static void handleArray (byte[] array , int offset , int len) {}

    public static void main(String[] args) {
//        for (int i = 0 ; i < 100 ; i ++ ) {
//            System.out.println(random.nextInt());
//        }
//        byteBufSlice();
//        byteBufCopy();
        // write();
//        byteBufGetSet();
//        byteBufReadWrite();
        referenceCounting();

    }
}
