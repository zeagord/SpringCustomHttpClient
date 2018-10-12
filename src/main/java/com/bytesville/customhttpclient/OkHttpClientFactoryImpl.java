package com.bytesville.customhttpclient;

import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;

public class OkHttpClientFactoryImpl implements OkHttpClientFactory {
  @Override public OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    ConnectionPool okHttpConnectionPool = new ConnectionPool(50, 30, TimeUnit.SECONDS);
    builder.connectionPool(okHttpConnectionPool);
    builder.connectTimeout(20, TimeUnit.SECONDS);
    builder.retryOnConnectionFailure(false);
    return builder;
  }
}
