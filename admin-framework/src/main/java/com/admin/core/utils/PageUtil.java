package com.admin.core.utils;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页返回值工具
 * https://gitee.com/elunez/eladmin/blob/master/eladmin-common/src/main/java/me/zhengjie/utils/PageUtil.java
 * @author fei
 * @date 2021/8/27 16:14
 */
public class PageUtil {
  /**
   * List 分页
   */
  public static List toPage(int page, int size , List list) {
    int fromIndex = page * size;
    int toIndex = page * size + size;
    if(fromIndex > list.size()){
      return new ArrayList();
    } else if(toIndex >= list.size()) {
      return list.subList(fromIndex,list.size());
    } else {
      return list.subList(fromIndex,toIndex);
    }
  }

  /**
   * Page 数据处理，预防redis反序列化报错
   */
  public static Map<String,Object> toPage(Page page) {
    Map<String,Object> map = new LinkedHashMap<>(2);
    map.put("content",page.getContent());
    map.put("totalElements",page.getTotalElements());
    return map;
  }

  /**
   * 自定义分页
   */
  public static Map<String,Object> toPage(Object object, Object totalElements) {
    Map<String,Object> map = new LinkedHashMap<>(2);
    map.put("content",object);
    map.put("totalElements",totalElements);
    return map;
  }

}
