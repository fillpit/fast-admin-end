package com.admin.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author fei
 * @since 2018-12-22
 */
@Slf4j
public class UrlTest {

  @Test
  public void test() {
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    String url = "/admin/user/list?name=fei";
//    UrlUtils.buildRequestUrl()
    log.info("匹配结果: {}", antPathMatcher.matchStart("/admin/user/list", url));
  }

  @Test
  public void test3() {
    SimpleIdGenerator idGenerator = new SimpleIdGenerator();
    String str1 = idGenerator.generateId().toString();
    log.info("枚举输出结果: {}", str1);
    String str2 = idGenerator.generateId().toString();
    log.info("枚举输出结果: {}", str2);
    String str3 = idGenerator.generateId().toString();
    log.info("枚举输出结果: {}", str3);
  }

  public static enum Gender {
    MALE,
    FEMALE;

    public static Gender value(String val){
      for (Gender g : values()) {
        if (g.name().equals(val)){
          return g;
        }
      }
      throw new RuntimeException();
    }
  }
}


