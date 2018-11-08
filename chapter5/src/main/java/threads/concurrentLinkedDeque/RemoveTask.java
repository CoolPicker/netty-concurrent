package threads.concurrentLinkedDeque;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @ClassName: RemoveTask
 * @Decription: 任务类 remove 大量删除同一列表中的数据
 * @Author: nya
 * @Date: 18-11-6 下午5:50
 * @Version: 1.0
 **/
public class RemoveTask implements Runnable {

    private ConcurrentLinkedDeque<String> list;

    public RemoveTask(ConcurrentLinkedDeque<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0 ; i < 5000 ; i++) {
            list.pollFirst();
            list.pollLast();
        }
    }
}
