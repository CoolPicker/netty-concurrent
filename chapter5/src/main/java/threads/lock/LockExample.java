package threads.lock;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: LockExample
 * @Decription: 线程同步 - synchronized同步块 - lock和它的实现类
 *  interface Lock 与 synchronized关键字的区别
 *  1.不存在设置synchronized代码块同步时超时的情况,而获取锁则可以设置超时,使用Lock.tryLock(long timeout,TimeUnit timeUnit)
 *      Lock.tryLock(long timeout,TimeUnit timeUnit) - 如果在给定的时间内空闲并且当前线程未被中断,则获取锁.否则则sleep当前线程,直到以下三种情况出现:
 *          锁由当前线程获取
 *          其它线程中断了当前线程,且获取到了中断锁
 *          指定的等待时间已过去
 *  2.synchronized必须完全包含在单个方法体内,而Lock的lock()和unlock()可以在不同的方法.
 * @Author: nya
 * @Date: 18-11-7 上午10:40
 * @Version: 1.0
 **/
public class LockExample {

    public static void main(String[] args) {
        PrinterQueue printerQueue = new PrinterQueue();
        Thread thread[] = new Thread[10];
        for (int i = 0 ;i < 10 ; i++) {
            thread[i] = new Thread(new PrintingJob(printerQueue),"Thread" + i);
        }
        for (int i = 0 ; i < 10 ; i++) {
            thread[i].start();
        }
    }

    static class PrinterQueue {
        private final Lock queueLock = new ReentrantLock();

        public void printJob(Object document) {
            queueLock.lock();
            try {
                Long duration = (long)Math.random() * 10000;
                System.out.println(Thread.currentThread().getName() + ": PrintQueue: Printing a Job during " + (duration / 1000) + " seconds :: Time - " + new Date());
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
                queueLock.unlock();
            }
        }
    }

    static class PrintingJob implements Runnable {

        private PrinterQueue printerQueue;

        public PrintingJob(PrinterQueue printerQueue) {
            this.printerQueue = printerQueue;
        }

        @Override
        public void run() {
            System.out.printf("%s: Going to print a document\n",Thread.currentThread().getName());
            printerQueue.printJob(new Object());
        }
    }
}
