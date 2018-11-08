package threads.concurrentLinkedDeque;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @ClassName: AddTask
 * @Decription: 任务类 add 将数据大量添加到列表
 * @Author: nya
 * @Date: 18-11-6 下午5:47
 * @Version: 1.0
 **/
public class AddTask implements Runnable {

    private ConcurrentLinkedDeque<String> list;

    public AddTask(ConcurrentLinkedDeque<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (int i = 0 ; i < 10000 ; i++) {
            list.add(name + ": Element " + i);
        }
    }
}
