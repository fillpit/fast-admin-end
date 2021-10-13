package com.admin.core.converter;

import com.admin.core.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 枚举参数转换器
 * @author fei
 * @date 2018/6/14
 */
public class EnumConverter implements ConverterFactory<String, BaseEnum> {

  private static final Map<Class, Converter> CONVERTER_MAP = new WeakHashMap<>();

  @Override
  public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> aClass) {
    Converter result = CONVERTER_MAP.get(aClass);
    if(result == null) {
      result = new StrToEnum<>(aClass);
      CONVERTER_MAP.put(aClass, result);
    }
    return result;
  }

  class StrToEnum<T extends BaseEnum> implements Converter<String, T> {

    private final Class<T> enumType;
    private Map<String, T> enumMap = new HashMap<>();

    public StrToEnum(Class<T> enumType) {
      this.enumType = enumType;
      T[] enums = enumType.getEnumConstants();
      for(T e : enums) {
        enumMap.put(e.value() + "", e);
      }
    }

    @Override
    public T convert(String s) {
      T result = enumMap.get(s);
      if(result == null) {
        throw new IllegalArgumentException("No element matches [" + s + "]");
      }
      return result;
    }
  }
}
