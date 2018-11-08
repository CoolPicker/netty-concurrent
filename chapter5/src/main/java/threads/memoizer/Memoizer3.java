package threads.memoizer;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName: Memoizer3
 * @Decription: 基于FutureTask的Memoizing封装器
 *  FutureTask
 *  - FutureTask 表示一个计算过程,这个过程可能已经计算完成,也可能正在进行.
 *      如果有结果可用,那么FutureTask.get将立即返回结果,否则它会一直阻塞,
 *          直到结果计算出来再将其返回.
 * @Author: nya
 * @Date: 18-11-8 下午2:07
 * @Version: 1.0
 **/
public class Memoizer3<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    /**
     * 此处将用于缓存值的Map重新定义为 ConcurrentHashMap<A,FutureTask<V>>
     *     - 首先检查某个相应的计算是否已经开始
     *          如果还没有启动,那么就创建一个FutureTask,并注册到Map中,然后启动计算
     *          如果已经启动,那么等待现有计算的结果
 *         - 一个缺陷 : 存在两个线程计算出相同值的漏洞
     *         由于方法体中的 if 代码块仍然是非原子(nonatomic)的"先检查后执行"操作,
     *         因此两个线程仍有可能在同一时间内调用compute来计算相同的值,
     *         即二者都没有在缓存中找到期望的值,因此都开始计算.
     *         - 究其原因 : 复合操作("若没有则添加")实在底层的Map对象上执行的,
     *                  而这个对象无法通过加锁来确保原子性.
 *                  这里可以使用ConcurrentHashMap的原子方法 putIfAbsent,避免此漏洞
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg,ft);
            ft.run();
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
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
