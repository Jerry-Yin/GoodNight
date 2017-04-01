package com.hdu.team.hiwanan.manager;

import android.media.MediaPlayer;
import android.util.Log;

import com.hdu.team.hiwanan.model.HiAlarmTask;
import com.hdu.team.hiwanan.util.HiLog;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by JerryYin on 3/29/17.
 * ThreadPoolExecutor -- 系统线程池类
 * HiThreadPoolExecutor -- 自定义线程池类
 */

class HiThreadPoolExecutor extends ThreadPoolExecutor {


    private static final String TAG = "HiThreadPoolExecutor";


    private List<HiAlarmTask> mTaskList = new ArrayList<>();

    /**
     * @param corePoolSize    //当某个核心任务执行完毕，会依次从缓冲队列中取出等待任务
     * @param maximumPoolSize //5,先corePoolSize,然后new LinkedBlockingQueue<Runnable>(),然后maximumPoolSize,但是它的数量是包含了corePoolSize的
     * @param keepAliveTime   //表示的是maximumPoolSize当中等待任务的存活时间
     * @param unit
     * @param workQueue       //缓冲队列，用于存放等待任务，Linked的先进先出
     * @param threadFactory   //创建线程的工厂
     * @param handler         //用来对超出maximumPoolSize的任务的处理策略
     */
    public HiThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, HiBlockingQueue<HiAlarmTask> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    @Override
    public void execute(Runnable command) {
        if (!mTaskList.contains(command))
            mTaskList.add((HiAlarmTask) command);
        HiAlarmTask task = (HiAlarmTask) command;
        int curId = task.getTaskId();
        stopCurPlayingExcept(curId);
        super.execute(command);
    }

    /**
     * @param task
     * @return
     */
    @Override
    public boolean remove(Runnable task) {
        return super.remove(task);
    }

    /**
     * 判断并且结束当前“正在播放”的其他task
     *
     * @param id
     */
    private void stopCurPlayingExcept(int id) {
//        HiBlockingQueue<HiAlarmTask> queue = (HiBlockingQueue<HiAlarmTask>) getQueue();
//        Log.d(TAG, "getQueue.size: "+queue.size());
//        Iterator iterator = queue.iterator();
//        if (iterator != null && iterator.hasNext()) {
//            HiAlarmTask task = (HiAlarmTask) iterator.next();
//            MediaPlayer player = task.getPlayer();
//
//            //this is not the current task
//            if (!(task.getTaskId() == id) && player.isPlaying())
//                task.shutdown();
//        }

        for (HiAlarmTask t : mTaskList){
            if (!(t.getTaskId() == id) && t.getPlayer().isPlaying())
                t.shutdown();
        }
    }


    @Override
    public long getTaskCount() {
        return super.getTaskCount();
    }

    /**
     * @param taskId
     * @return
     */
    public HiAlarmTask getTaskById(int taskId) {
        HiAlarmTask task = null;
        for (HiAlarmTask t : mTaskList){
            if (t.getTaskId() == taskId)
                task = t;
        }
        return task;
    }

    /**
     * does the pool contain the task now?
     *
     * @param taskId
     * @return
     */
    public boolean hasTask(int taskId) {
        boolean has = false;
        Iterator i = getQueue().iterator();
        if (i != null && i.hasNext()) {
            HiAlarmTask t = (HiAlarmTask) i.next();
            if (t.getTaskId() == taskId) {
                return has = true;
            }
        }
        return has;
    }
}
