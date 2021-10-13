package com.admin.app;

import com.admin.Application;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author fei
 * @since 2019-06-06 10:43
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RestTest {
  @Autowired private RestTemplate restTemplate;

  @Test
  public void post() {
    User user = new User();
    user.setId("1000");
    user.setName("fei");

//    Object b = restTemplate.postForObject("http://127.0.0.1:8090/admin/hello/restPost", user, Object.class);

//    log.info("返回结果====={}", b);
  }

  @Data
  class User{
    private String id;
    private String name;
  }
}
