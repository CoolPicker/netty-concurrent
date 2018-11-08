package threads.memoizer;

/**
 * 计算 - 接口
 * @param <A>
 * @param <V>
 */
public interface Computable<A,V> {
    V compute(A arg) throws InterruptedException;
}
