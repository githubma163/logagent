package com.max.log.agent.service;

import com.max.log.agent.Log;
import com.max.log.agent.constant.Constant;
import com.max.log.agent.constant.DataSourceType;
import com.max.log.agent.util.ConfigUtil;
import com.max.log.agent.util.LogESUtil;
import com.max.log.agent.util.LogMongoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogProduceConsumeService {

    private static LogProduceConsumeService logService = new LogProduceConsumeService();

    public static LogProduceConsumeService getLogService() {
        return logService;
    }

    private LogProduceConsumeService() {

    }

    public static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void removeThreadLocal() {
        threadLocal.remove();
    }

    public ExecutorService PRODUCER_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    public ExecutorService CONSUMER_EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);

    public BlockingQueue<Log> LOG_BLOCKING_QUEUE = new ArrayBlockingQueue<Log>(10000);

    public void produce(Log log) {
        this.PRODUCER_EXECUTOR_SERVICE.submit(new Producer(log, this.LOG_BLOCKING_QUEUE));
        this.CONSUMER_EXECUTOR_SERVICE.submit(new Consumer(this.LOG_BLOCKING_QUEUE));
    }

    public static class Producer extends Thread {

        private Log log;
        private BlockingQueue<Log> blockingQueue;

        public Producer(Log log, BlockingQueue<Log> blockingQueue) {
            this.log = log;
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            try {
                blockingQueue.put(log);
                System.out.println("LogProducer: " + Thread.currentThread().getName() + ",queueSize:" + blockingQueue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static class Consumer extends Thread {

        private BlockingQueue<Log> blockingQueue;

        public Consumer(BlockingQueue<Log> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            try {
                Integer batchSize = Constant.DEFAULT_BATCH_SIZE;
                if (null != ConfigUtil.CONFIG_MAP && ConfigUtil.CONFIG_MAP.containsKey(Constant.BATCH)) {
                    batchSize = Integer.parseInt(ConfigUtil.CONFIG_MAP.get(Constant.BATCH));
                    if (batchSize <= 0) {
                        batchSize = Constant.DEFAULT_BATCH_SIZE;
                    }
                }
                if (blockingQueue.size() < batchSize) {
                    return;
                }
                List<Log> logList = new ArrayList<Log>();
                for (int i = 0; i < batchSize; i++) {
                    Log log = blockingQueue.take();
                    logList.add(log);
                }
                System.out.println("LogConsumer: " + Thread.currentThread().getName() + ",queueSize:" + blockingQueue.size() + ",batchSize:" + batchSize);
                String dataSource = ConfigUtil.CONFIG_MAP.get(Constant.SEND);
                if (dataSource.equalsIgnoreCase(DataSourceType.ES.name())) {
                    LogESUtil.bulkSaveLog(ConfigUtil.CONFIG_MAP, logList);
                }
                if (dataSource.equalsIgnoreCase(DataSourceType.MONGO.name())) {
                    LogMongoUtil.bulkSaveLog(ConfigUtil.CONFIG_MAP, logList);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
