package threads.threadFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CustomThreadFactory
 * @Decription: ThreadFactory接口的实现类
 * Java多线程之创建线程,extends Thread / implements Runnable / implements ThreadFactory 创建自定义的线程对象工厂
 * @Author: nya
 * @Date: 18-11-7 上午9:57
 * @Version: 1.0
 **/
public class CustomThreadFactory implements ThreadFactory {

    private int counter;
    private String name;
    private List<String> stats;

    public CustomThreadFactory(String name) {
        counter = 1;
        this.name = name;
        stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable,name + "-Thread_" + counter);
        counter++;
        stats.add(String.format("Created thread %d with name %s on %s \n",thread.getId(),thread.getName(),new Date()));
        return thread;
    }

    public String getStats() {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = stats.iterator();
        while (iterator.hasNext()) {
            buffer.append(iterator.next());
        }
        return buffer.toString();
    }


    public static void main(String[] args) {

        CustomThreadFactory factory = new CustomThreadFactory("ThreadFactory");
        Task task = new Task();
        Thread thread;
        System.out.printf("Starting the Threads\n\n");
        for (int i = 1 ; i <= 10 ; i++) {
            thread = factory.newThread(task);
            thread.start();
        }
        System.out.printf("All Threads are created now\n\n");
        System.out.printf("Give me CustomThreadFactory stats:\n\n" + factory.getStats());
    }


    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
