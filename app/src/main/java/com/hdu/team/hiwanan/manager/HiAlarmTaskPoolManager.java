package com.hdu.team.hiwanan.manager;

import com.hdu.team.hiwanan.model.HiAlarmTask;
import com.hdu.team.hiwanan.util.HiLog;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by JerryYin on 3/29/17.
 * 单例模式
 * ThreadPoolExecutor --- java中的一个线程管理类
 *
 */

public class HiAlarmTaskPoolManager {

    private static final java.lang.String TAG = "HiAlarmTaskPoolManager";
    private int corePoolSize;//核心线程池的数量，同时能够执行的线程数量
    private int maximumPoolSize;//最大线程池数量，表示当缓冲队列满的时候能继续容纳的等待任务的数量
    private long keepAliveTime = 1;//存活时间
    private TimeUnit unit = TimeUnit.HOURS;
    private HiThreadPoolExecutor executor;

    private static HiAlarmTaskPoolManager mInstance;
    private HiBlockingQueue<HiAlarmTask> mHiBlockingQueue = new HiBlockingQueue<>();


    private HiAlarmTaskPoolManager() {

        new HiBlockingQueue<HiAlarmTask>();

        /**
        * 给corePoolSize赋值：当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行（有研究论证的）
        */
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maximumPoolSize = corePoolSize; //虽然maximumPoolSize用不到，但是需要赋值，否则报错
        executor = new HiThreadPoolExecutor(
                corePoolSize, //当某个核心任务执行完毕，会依次从缓冲队列中取出等待任务
                maximumPoolSize, //5,先corePoolSize,然后new LinkedBlockingQueue<Runnable>(),然后maximumPoolSize,但是它的数量是包含了corePoolSize的
                keepAliveTime, //表示的是maximumPoolSize当中等待任务的存活时间
                unit,
                mHiBlockingQueue, //缓冲队列，用于存放等待任务，Linked的先进先出
                Executors.defaultThreadFactory(), //创建线程的工厂
                new ThreadPoolExecutor.AbortPolicy() //用来对超出maximumPoolSize的任务的处理策略
        );
    }

    public static HiAlarmTaskPoolManager getInstance(){
        if (mInstance == null){
            mInstance = new HiAlarmTaskPoolManager();
        }
        return mInstance;
    }

    /**
     * 执行任务
     * 注意，每个任务执行之前，需要先判断此时有无其他正在执行的任务（正在播放音乐的，需要关闭）
     * @param task
     */
    public void executeTask(HiAlarmTask task){
        if (task == null)
            return;
//        mHiBlockingQueue.add(task);
        executor.execute(task);
//        HiLog.d(TAG, "mQueue.size: "+mHiBlockingQueue.size());
    }




    /**
     * 移除任务
     * 在用户关闭闹钟 时候触发
     * @param task
     */
    public void removeTask(HiAlarmTask task){
        if (task == null)
            return;
        task.shutdown();
        executor.remove(task);
    }


    /**
     * 根据id获取task, 匹配不到id则返回null
     * @param taskId
     * @return
     */
    public HiAlarmTask getTaskById(int taskId){
        return executor.getTaskById(taskId);
    }

    /**
     * 判断是否含有这个task
     * @param taskId
     * @return
     */
    public boolean hasTask(int taskId){
        return executor.hasTask(taskId);
    }
}
