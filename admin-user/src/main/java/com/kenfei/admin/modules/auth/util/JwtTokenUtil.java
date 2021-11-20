package com.kenfei.admin.modules.auth.util;


import com.kenfei.admin.modules.auth.config.properties.TokenProperties;
import com.kenfei.admin.modules.auth.config.security.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 工具
 * @author fei
 * @date 2018/10/22
 */
@Component
public class JwtTokenUtil implements Serializable {
  private static final long serialVersionUID = 2303336164034855732L;

  private static final String CLAIM_KEY_USERNAME = "sub";
  private static final String CLAIM_KEY_CREATED = "created";

  @Autowired private TokenProperties tokenProperties;

  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  private Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  private Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
        .setSigningKey(tokenProperties.getSecret())
        .parseClaimsJws(token)
        .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + tokenProperties.getExpiration() * 1000);
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>(16);
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    claims.put(CLAIM_KEY_CREATED, new Date());
    return generateToken(claims);
  }

  String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
      .setClaims(claims)
      .setExpiration(generateExpirationDate())
      .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret())
      .compact();
  }

  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getCreatedDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
      && !isTokenExpired(token);
  }

  /**
   * 刷新 token
   * @param token token
   * @return 刷新后的 token
   */
  public String refreshToken(String token) {
    String refreshedToken;
    try {
      final Claims claims = getClaimsFromToken(token);
      claims.put(CLAIM_KEY_CREATED, new Date());
      refreshedToken = generateToken(claims);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  /**
   * 检查token
   * @param token token
   * @param userDetails 用户对象
   * @return true: 通过， false: 非法token
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    JwtUser user = (JwtUser) userDetails;
    final String username = getUsernameFromToken(token);
//    final Date created = getCreatedDateFromToken(token);
    //final Date expiration = getExpirationDateFromToken(token);
    return (
      username.equals(user.getUsername())
        && !isTokenExpired(token));
  }

  /**
   * 获取 token
   * @param request /
   * @return /
   */
  public String getLoginToken(HttpServletRequest request) {
    String tokenHeader = request.getHeader(tokenProperties.getHeader());
    return subTokenPrefix(tokenHeader);
  }

  /**
   * 获取 token
   * @param tokenHeader 请求 token header 的 值
   * @return /
   */
  public String subTokenPrefix(String tokenHeader) {
    if (StringUtils.hasLength(tokenHeader)){
      return tokenHeader.substring(tokenProperties.getTokenHead().length());
    }
    return "";
  }
}

