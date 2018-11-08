package threads.concurrentLinkedDeque;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @ClassName: TestConcurrentLinkedDeque
 * @Decription: ConcurrentLinkedDeque测试
 *  ConcurrentLinkedDeque - 非阻塞线程安全列表
 *  并发列表允许多个线程同时进行增删且不会造成数据不一致.
 * @Author: nya
 * @Date: 18-11-6 下午5:53
 * @Version: 1.0
 **/
public class TestConcurrentLinkedDeque {

    public static void main(String[] args) {
        long aa = System.currentTimeMillis();
        ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
        Thread threads[] = new Thread[100];

        // 新增100个线程,执行非阻塞集合的add任务
        for (int i = 0 ; i < threads.length ; i++) {
            AddTask task = new AddTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }

        System.out.printf("Main: %d AddTask threads have been launched \n" , threads.length);

        // 使用join()阻塞主线程,优先完成100个 add 的子线程任务
        for (int i = 0 ; i < threads.length ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: Size of the List: %d\n",list.size());

        // 新增100个线程,执行非阻塞集合的remove任务
        for (int i = 0 ; i < threads.length ; i++) {
            RemoveTask task = new RemoveTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }

        System.out.printf("Main: %d RemoveTask threads have been launched\n",threads.length);

        // 使用join()阻塞主线程,优先完成100个 remove 的子线程任务
        for (int i = 0 ; i < threads.length ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: Size of the List : %d\n",list.size());
        long bb = System.currentTimeMillis();
        System.out.println("cost millis : " + (bb - aa ));
    }

}
