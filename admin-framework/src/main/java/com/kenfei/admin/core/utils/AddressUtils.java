package com.kenfei.admin.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 获取 Http 请求来源地址类
 *
 * @author fei
 * @since 2020/10/24 3:06 下午
 */
public class AddressUtils {
  /** IP地址查询 */
  public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
  /** 未知地址 */
  public static final String UNKNOWN = "XX XX";

  static ObjectMapper mapper = new ObjectMapper();

  private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

  public static String getRealAddressByIp(String ip) {
    // 内网不查询
    if (IpUtils.internalIp(ip)) {
      return "内网IP";
    }
    try {
      String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
      if (StringUtils.isEmpty(rspStr)) {
        log.error("获取地理位置异常 {}", ip);
        return UNKNOWN;
      }
      JsonNode jsonNode = mapper.readTree(rspStr);
      String region = jsonNode.path("pro").asText();
      String city = jsonNode.path("city").asText();
      return String.format("%s %s", region, city);
    } catch (Exception e) {
      log.error("获取地理位置异常 {}", e);
    }
    return UNKNOWN;
  }
}
