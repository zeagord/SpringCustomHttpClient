package com.bytesville.customhttpclient;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeepAliveConfig {
  @Bean ConnectionKeepAliveStrategy connectionKeepAliveStrategy(){
    return (response, context) -> {
      HeaderElementIterator it = new BasicHeaderElementIterator(
          response.headerIterator(HTTP.CONN_KEEP_ALIVE));
      while (it.hasNext()){
        HeaderElement he = it.nextElement();
        String param = he.getName();
        String value = he.getValue();
        if (value != null && param.equalsIgnoreCase("timeout")) {
          try {
            return Long.parseLong(value) * 1000;
          } catch(NumberFormatException exception) {
            exception.printStackTrace();
          }
        }
      }
      // If there is no Keep-Alive header. Keep the connection for 30 seconds
      return 30*1000;
    };
  }
}
