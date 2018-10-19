package com.bytesville.customhttpclient;

import io.micrometer.core.instrument.MeterRegistry;
import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

@Configuration
public class RequestFactoryConfig {

  private final ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
  private final MeterRegistry registry;
  RequestFactoryConfig(ConnectionKeepAliveStrategy connectionKeepAliveStrategy, MeterRegistry registry){
    this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
    this.registry = registry;
  }

  @Bean
  @Qualifier("apacheRestTemplate")
  public ClientHttpRequestFactory createRequestFactory() throws InterruptedException {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(400);
    connectionManager.setDefaultMaxPerRoute(200);
    IdleConnectionMonitorThread
        connectionMonitor = new IdleConnectionMonitorThread(connectionManager, registry);
    connectionMonitor.start();
    connectionMonitor.join(2000);
    RequestConfig requestConfig = RequestConfig
        .custom()
        .setConnectionRequestTimeout(5000)
        .setSocketTimeout(10000)
        .build();

    CloseableHttpClient httpClient = HttpClients
        .custom()
        .setConnectionManager(connectionManager)
        .setKeepAliveStrategy(connectionKeepAliveStrategy)
        .setDefaultRequestConfig(requestConfig)
        .build();
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  }

  @Bean
  @Qualifier("apacheSpringCommonsRestTemplate")
  public ClientHttpRequestFactory createCommonsRequestFactory() {
    ApacheHttpClientFactoryImpl httpClientFactory = new ApacheHttpClientFactoryImpl();
    HttpClient client = httpClientFactory.createBuilder().build();
    return new HttpComponentsClientHttpRequestFactory(client);
  }
  @Bean
  @Qualifier("OKSpringCommonsRestTemplate")
  public ClientHttpRequestFactory createOKCommonsRequestFactory() {
    OkHttpClientFactoryImpl  httpClientFactory= new OkHttpClientFactoryImpl();
    OkHttpClient client = httpClientFactory.createBuilder(false).build();
    return new OkHttp3ClientHttpRequestFactory(client);
  }
}
