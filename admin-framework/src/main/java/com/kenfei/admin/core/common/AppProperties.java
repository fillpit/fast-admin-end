package com.kenfei.admin.core.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fei
 * @date 2021/8/27 14:50
 */
@ConfigurationProperties(AppProperties.PREFIX)
public class AppProperties {
  public static final String PREFIX = "app";

  /** 服务名 */
  private String name;
  /** redis key 前缀 */
  private String redisKeyPrefix;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRedisKeyPrefix() {
    return redisKeyPrefix;
  }

  public void setRedisKeyPrefix(String redisKeyPrefix) {
    this.redisKeyPrefix = redisKeyPrefix;
  }
}
