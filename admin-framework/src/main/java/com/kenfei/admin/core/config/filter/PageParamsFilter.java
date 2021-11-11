package com.kenfei.admin.core.config.filter;

import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 处理 请求里的 page 参数
 * @author fei
 * @since 2019/10/30 17:09
 */
public class PageParamsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    // 只处理 get 请求
    if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
      ParameterRequestWrapper paramRequest = new ParameterRequestWrapper(request);
      filterChain.doFilter(paramRequest, servletResponse);
    } else {
      filterChain.doFilter(request, servletResponse);
    }
  }

  static class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private Map<String , String[]> params = new HashMap<>();

    ParameterRequestWrapper(HttpServletRequest request) {
      // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
      super(request);
      //将参数表，赋予给当前的Map以便于持有request中的参数
      Map<String, String[]> requestMap=request.getParameterMap();
      this.params.putAll(requestMap);
      this.modifyParameterValues();
    }

    /**
     * 将parameter的值去除空格后重写回去
     */
    void modifyParameterValues(){
      Set<String> set =params.keySet();
      for (String key : set) {
        // 只处理 page 参数
        if ("page".equals(key)) {
          String[] values = params.get(key);
          int page = ObjectUtils.isEmpty(values) ? 1 : Integer.parseInt(values[0]);
          // 如果 page 大于零就 减一
          values[0] = page > 0 ? String.valueOf(page - 1) : String.valueOf(page);
          params.put(key, values);
        }
      }
    }

    /** 重写getParameter 参数从当前类中的map获取 */
    @Override
    public String getParameter(String name) {
      String[] values = params.get(name);
      if (values == null || values.length == 0) {
        return null;
      }
      return values[0];
    }


    /**
     * 重写getParameterValues
     */
    @Override
    public String[] getParameterValues(String name) {
      return params.get(name);
    }
  }
}
