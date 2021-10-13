package com.admin.core.config.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * httpClient 配置（统一错误响应处理）
 *
 * @author fei
 * @date 2018/6/24
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    // 当状态码不是 200 或 返回的标示里包含错误约束时
    if (!response.getStatusCode().is2xxSuccessful()) {
      return true;
    }

    // 获取请求值，判断是否有错误的约束
    InputStream inputStream = response.getBody();
//    String result = StreamUtils.copyToString(response.getBody(), UTF_8);

    // 错误内容的判断标志
    //    JsonNode node = OBJECT_MAPPER.readTree(result);
    //    boolean success = node.get("success").asBoolean();
    //    return !success;
    return false;
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    // 取出返回值
    InputStream inputStream = response.getBody();
    String result =
        new BufferedReader(new InputStreamReader(inputStream))
            .lines()
            .parallel()
            .collect(Collectors.joining("\n"));

    // 抛出提示内容
    JsonNode node = OBJECT_MAPPER.readTree(result);
    String message = node.get("message").asText();
    throw new HttpResponseException(message);
  }
}
