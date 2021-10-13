package com.admin.core.converter;

import com.admin.core.exception.DateConverterException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接口参数转换器（String -> Date）.
 *
 * @author fei
 * @date 2018/2/27
 */
public class DateConverter implements Converter<String, Date> {
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public Date convert(String s) {
    if (!StringUtils.hasLength(s)) {
      return null;
    }

    // 去前后空格
    String[] v = s.trim().split(" ");
    if (v.length == 1) {
      s = s.concat(" 00:00:00");
    }

    try {
      return dateFormat.parse(s);
    } catch (ParseException e) {
      throw new DateConverterException("[" + s + "]时间格式有误, 合法格式 yyyy-MM-dd HH:mm:ss");
    }
  }
}
