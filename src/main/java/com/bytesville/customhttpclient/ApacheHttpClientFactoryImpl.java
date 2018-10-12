package com.bytesville.customhttpclient;

import java.util.concurrent.TimeUnit;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.context.annotation.Configuration;

public class ApacheHttpClientFactoryImpl implements ApacheHttpClientFactory {

  @Autowired
  private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

  @Override public HttpClientBuilder createBuilder() {
    RequestConfig requestConfig = RequestConfig
        .custom()
        .setConnectionRequestTimeout(20000)
        .setSocketTimeout(30000)
        .build();

    return HttpClientBuilder
        .create()
        .setMaxConnTotal(400)
        .setMaxConnPerRoute(200)
        .setKeepAliveStrategy(connectionKeepAliveStrategy)
        .evictIdleConnections(30, TimeUnit.SECONDS)
        .setDefaultRequestConfig(requestConfig);
  }
}
