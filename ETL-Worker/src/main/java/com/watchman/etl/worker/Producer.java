package com.watchman.etl.worker;

import java.io.IOException;

public interface Producer<M, C> extends Worker {
  void publish(M message, C config) throws IOException;
}
