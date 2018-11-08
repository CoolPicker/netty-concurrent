package threads.memoizer;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName: Memoizer
 * @Decription: Memoizer的最终实现
 * @Author: nya
 * @Date: 18-11-8 下午2:29
 * @Version: 1.0
 **/
public class Memoizer<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A,V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    /**
     * 为避免底层Map在执行复合操作("若没有则添加")时,无法保证原子性的问题,此处使用了原子方法 ConcurrentHashMap.putIfAbsent()
     *  - BTW,当缓存的是Future而不是值时,将导致Cache Pollution(缓存污染)问题:
     *      如果某个计算被取消或者失败,那么在计算这个结果时将指明计算过程被取消或者失败.
     *      - 为避免此情况,如果Memoizer发现计算被取消,那么将把Future从缓存中移除.
     *      仍有两个问题未解决
     *      1.缓存逾期 - 通过FutureTask的子类来解决,为每个结果指定一个逾期时间,并定期扫描缓存中的元素
     *      2.缓存清理-更新问题
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public V compute(A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg,ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg,f);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }

    private RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked",t);
    }

}
