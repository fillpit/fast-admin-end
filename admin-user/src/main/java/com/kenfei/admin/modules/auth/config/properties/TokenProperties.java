package com.kenfei.admin.modules.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fei
 * @date 2021/8/27 17:19
 */
@ConfigurationProperties(TokenProperties.PREFIX)
public class TokenProperties {
  public static final String PREFIX = "app.jwt";
  /** token 请求头标识 */
  private String header = "Access-Token";
  /** token 加密密钥 */
  private String secret = "mySecret";
  /** token 有效期 */
  private Long expiration = 86400L;
  /** token 前缀 */
  private String tokenHead = "Bearer ";

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Long getExpiration() {
    return expiration;
  }

  public void setExpiration(Long expiration) {
    this.expiration = expiration;
  }

  public String getTokenHead() {
    return tokenHead;
  }

  public void setTokenHead(String tokenHead) {
    this.tokenHead = tokenHead;
  }
}
