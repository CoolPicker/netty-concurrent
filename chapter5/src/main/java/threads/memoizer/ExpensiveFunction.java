package threads.memoizer;

import java.math.BigInteger;

/**
 * @ClassName: ExpensiveFunction
 * @Decription: 实现Computable , 封装缓存过程
 * @Author: nya
 * @Date: 18-11-8 下午1:50
 * @Version: 1.0
 **/
public class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        // 经过长时间的计算后
        return new BigInteger(arg);
    }
}
