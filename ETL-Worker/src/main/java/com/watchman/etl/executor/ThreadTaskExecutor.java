package com.watchman.etl.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTaskExecutor implements TaskExecutor<Runnable, Future<?>> {
  private final ExecutorService executorService;
  private static volatile ThreadTaskExecutor threadWorker;

  private ThreadTaskExecutor() {
    System.out.println("Thread task executor started...");
    int numCores = Runtime.getRuntime().availableProcessors();
    executorService = Executors.newFixedThreadPool(numCores);
  }

  @Override
  public Future<?> submit(Runnable task) {
    return executorService.submit(task);
  }

  // TODO: check if there is any way to implement a generic solution to creating singleton instances
  public static ThreadTaskExecutor getInstance() {
    if (threadWorker == null) {
      synchronized (ThreadTaskExecutor.class) {
        if (threadWorker == null) {
          threadWorker = new ThreadTaskExecutor();
        }
      }
    }

    return threadWorker;
  }
}
