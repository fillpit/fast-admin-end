package com.admin.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

/**
 * @author fei
 * @date 2021/8/18 15:42
 */
@Slf4j
public class BCryptPasswordTest {
  @Test
  public void test1() throws IOException {
    String pwd = "123456";

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(pwd);

    boolean valid = encoder.matches(pwd, "$2a$10$SU.aGn.QyryHmWPwsLy.a.MW/rgjgJaubwQTA.Uy..fxOP1HSz8Oi");

    log.info("加密结果: ==== {}", encode);
    log.info("匹配结果: ==== {}", valid);
  }
}
