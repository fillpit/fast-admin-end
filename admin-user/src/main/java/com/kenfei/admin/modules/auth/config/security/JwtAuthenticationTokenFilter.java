package com.kenfei.admin.modules.auth.config.security;

import com.kenfei.admin.modules.auth.util.JwtTokenUtil;
import com.kenfei.admin.modules.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 访问转换过滤器
 * @author fei
 * @date 2018/10/22
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
  @Autowired private JwtUserDetailsServiceImpl userDetailsService;
  @Autowired private AuthService authService;
  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Value("${app.jwt.header}")
  private String tokenHeader;

  @Value("${app.jwt.token-head}")
  private String tokenHead;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader(this.tokenHeader);
    if (authHeader != null && authHeader.startsWith(tokenHead)) {
      // The part after "Bearer "
      final String authToken = authHeader.substring(tokenHead.length());
      String username = jwtTokenUtil.getUsernameFromToken(authToken);

      logger.info("checking authentication " + username);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        // 看当前 token 是否已经注销
        if (authService.isLogout(authToken)) {
          if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("authenticated user " + username + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
