package com.bytesville.customhttpclient;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

public class IdleConnectionMonitorThread extends Thread {
  private static final Logger log = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
  private final PoolingHttpClientConnectionManager connMgr;
  private volatile boolean shutdown;
  private AtomicInteger availableConnections;
  private MeterRegistry registry;

  public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr, MeterRegistry registry) {
    super();
    this.connMgr = connMgr;
    this.registry = registry;
    this.availableConnections = this.registry.gauge("availableConnections", new AtomicInteger(0));
  }
  @Override
  public void run() {
    try {
      while (!shutdown) {
        synchronized (this) {
          wait(1000);
          log.info(connMgr.getTotalStats().toString());
          availableConnections.set(connMgr.getTotalStats().getAvailable());
          connMgr.closeExpiredConnections();
          connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
        }
      }
    } catch (InterruptedException ex) {
      shutdown();
    }
  }
  public void shutdown() {
    shutdown = true;
    synchronized (this) {
      notifyAll();
    }
  }
}
