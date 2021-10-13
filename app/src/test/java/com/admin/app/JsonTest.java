package com.admin.app;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @author fei
 * @since 2019-06-01 09:57
 */
@Slf4j
public class JsonTest {

  @Test
  public void test1() throws IOException {
    String json = "{\"TEST_A\": \"fei\", \"TEST_B\": \"123\"}";

    ObjectMapper objectMapper = new ObjectMapper();
    TestJson t = objectMapper.readValue(json, TestJson.class);

    log.info("读取结果: ==== {}", t.toString());
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class TestJson {
    @JsonAlias("TEST_A")
    private String testA;
    @JsonAlias("TEST_B")
    private String testB;
  }
}
