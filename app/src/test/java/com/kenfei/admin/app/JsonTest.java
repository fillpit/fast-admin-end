package com.kenfei.admin.app;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

  @Test
  public void test2() throws IOException {
    String json = "{\"TEST_A\": \"fei\", \"TEST_B\": \"123\"}";

    ObjectMapper objectMapper = new ObjectMapper();
    JavaType javaType = getCollectionType(ArrayList.class, Long.class);
    List<Long> t = objectMapper.readValue("", javaType);

    log.info("读取结果: ==== {}", t.toString());
  }

  /**   element

   * 获取泛型的Collection Type

   * @param collectionClass 泛型的Collection

   * @param elementClasses 元素类

   * @return JavaType Java类型

   * @since 1.0

   */

  public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

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
