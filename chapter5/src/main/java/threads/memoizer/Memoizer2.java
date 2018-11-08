package threads.memoizer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: Memoizer2
 * @Decription: 用ConcurrentHashMap替换HashMap
 * @Author: nya
 * @Date: 18-11-8 下午2:01
 * @Version: 1.0
 **/
public class Memoizer2<A,V> implements Computable<A,V> {

    private final Map<A,V> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer2(Computable<A,V> c) {
        this.c = c;
    }

    /**
     * 漏洞:
     *  当两个线程同时调用compute时,可能会导致计算得到相同的值.
     *  - 即 如果某个线程启动了一个开销很大的计算,而其他线程并不知道这个计算正在进行,
     *      则将重复这个计算.
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null)
            result = c.compute(arg);
            cache.put(arg,result);
        return result;
    }
}
