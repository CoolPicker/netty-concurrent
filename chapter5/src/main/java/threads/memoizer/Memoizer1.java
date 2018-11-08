package threads.memoizer;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Memoizer1
 * @Decription: 使用HashMap和同步机制来初始化缓存
 * @Author: nya
 * @Date: 18-11-8 下午1:52
 * @Version: 1.0
 **/
public class Memoizer1<A,V> implements Computable<A,V> {

    private final Map<A,V> cache = new HashMap<>();

    private final Computable<A,V> c;

    public Memoizer1(Computable<A,V> c) {
        this.c = c;
    }

    /**
     * 首先检查需要的结果是否已经在缓存中,如果存在则返回之前计算的值.
     * 否则,将把计算结果缓存在HashMap中,然后再返回.
     * 存在问题:
     *  HashMap非线程安全,此处采用保守方法,对整个compute方法进行同步.
     *  - 能确保线程安全性,但带来了明显的可伸缩性问题 : 每次只有一个线程能够执行 compute
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null)
            result = c.compute(arg);
            cache.put(arg,result);
        return result;
    }
}
