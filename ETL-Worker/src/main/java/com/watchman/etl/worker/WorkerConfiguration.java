package com.watchman.etl.worker;

public interface WorkerConfiguration {
    String getTopic();
    String getBootStrapServers();
    String getGroupId();
}
