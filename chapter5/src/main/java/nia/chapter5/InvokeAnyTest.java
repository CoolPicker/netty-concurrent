package nia.chapter5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


/**
 * @ClassName: InvokeAnyTest
 * @Decription: 测试线程池 ExecutorService的invokeAny()
 * @Author: nya
 * @Date: 18-10-22 下午2:50
 * @Version: 1.0
 **/
public class InvokeAnyTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<String>> callables = Arrays.asList(
                callable("task1",2),
                callable("task2",1),
                callable("task3",3)
        );
        String result= executor.invokeAny(callables);
        System.out.println(result);
    }

    static Callable<String> callable(final String result, final long sleepSeconds ) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }
}
