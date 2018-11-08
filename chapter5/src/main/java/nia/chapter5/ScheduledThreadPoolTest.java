package nia.chapter5;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ScheduledThreadPoolTest
 * @Decription: ScheduledThreadPoolExecutor 使用
 * @Author: nya
 * @Date: 18-10-22 下午5:40
 * @Version: 1.0
 **/
public class ScheduledThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

        for (int i = 0 ; i < 10 ; i++) {
            Task task = new Task("task-" + i);

//            scheduledThreadPool.scheduleAtFixedRate(task,
//                    0,
//                    1,
//                    TimeUnit.SECONDS);
            scheduledThreadPool.scheduleWithFixedDelay(task,
                    0,
                    1,
                    TimeUnit.SECONDS);
        }
        TimeUnit.SECONDS.sleep(2);

        System.out.println("shutdown executor ...");

        try {
            //scheduledThreadPool.shutdownNow();
            scheduledThreadPool.shutdown();
            scheduledThreadPool.awaitTermination(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("shutdown error " + e);
        } finally {
            if (!scheduledThreadPool.isTerminated()){
                System.err.println("is not done error");
            }
            scheduledThreadPool.shutdownNow();
        }

    }

}

class Task implements Runnable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("name = " + name + " , startTime = " + System.currentTimeMillis());
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //System.out.println("name = " + name + " , endTime = " + System.currentTimeMillis());
    }
}