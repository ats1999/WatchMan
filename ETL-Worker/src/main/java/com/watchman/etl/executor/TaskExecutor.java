package com.watchman.etl.executor;

public interface TaskExecutor<T, R> {
  R submit(T task);
}
