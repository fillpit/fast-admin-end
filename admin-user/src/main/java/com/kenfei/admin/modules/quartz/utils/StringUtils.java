package com.kenfei.admin.modules.quartz.utils;

import com.kenfei.admin.core.utils.TypeConvertUtils;

/**
 * @author fei
 * @since 2020/10/29 9:47 上午
 */
public class StringUtils extends org.springframework.util.StringUtils {
  public static final String EMPTY_JSON = "{}";
  public static final char C_BACKSLASH = '\\';
  public static final char C_DELIM_START = '{';
  public static final char C_DELIM_END = '}';
  /** 空字符串 */
  private static final String NULLSTR = "";
  /** 下划线 */
  private static final char SEPARATOR = '_';

  /**
   * 获取参数不为空值
   *
   * @param value defaultValue 要判断的value
   * @return value 返回值
   */
  public static <T> T nvl(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }

  /**
   * * 判断一个字符串是否为非空串
   *
   * @param str String
   * @return true：非空串 false：空串
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * * 判断一个对象是否为空
   *
   * @param object Object
   * @return true：为空 false：非空
   */
  public static boolean isNull(Object object) {
    return object == null;
  }

  /**
   * * 判断一个对象是否非空
   *
   * @param object Object
   * @return true：非空 false：空
   */
  public static boolean isNotNull(Object object) {
    return !isNull(object);
  }

  /** 去空格 */
  public static String trim(String str) {
    return (str == null ? "" : str.trim());
  }

  /**
   * 截取字符串
   *
   * @param str 字符串
   * @param start 开始
   * @return 结果
   */
  public static String substring(final String str, int start) {
    if (str == null) {
      return NULLSTR;
    }

    if (start < 0) {
      start = str.length() + start;
    }

    if (start < 0) {
      start = 0;
    }
    if (start > str.length()) {
      return NULLSTR;
    }

    return str.substring(start);
  }

  /**
   * 截取字符串
   *
   * @param str 字符串
   * @param start 开始
   * @param end 结束
   * @return 结果
   */
  public static String substring(final String str, int start, int end) {
    if (str == null) {
      return NULLSTR;
    }

    if (end < 0) {
      end = str.length() + end;
    }
    if (start < 0) {
      start = str.length() + start;
    }

    if (end > str.length()) {
      end = str.length();
    }

    if (start > end) {
      return NULLSTR;
    }

    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = 0;
    }

    return str.substring(start, end);
  }

  /**
   * 格式化文本, {} 表示占位符<br>
   * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
   * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
   * 例：<br>
   * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
   * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
   * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
   *
   * @param template 文本模板，被替换的部分用 {} 表示
   * @param params 参数值
   * @return 格式化后的文本
   */
  public static String format(String template, Object... params) {
    if (isEmpty(params) || isEmpty(template)) {
      return template;
    }
    if (StringUtils.isEmpty(template) || StringUtils.isEmpty(params)) {
      return template;
    }
    final int strPatternLength = template.length();

    // 初始化定义好的长度以获得更好的性能
    StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

    int handledPosition = 0;
    int delimIndex; // 占位符所在位置
    for (int argIndex = 0; argIndex < params.length; argIndex++) {
      delimIndex = template.indexOf(EMPTY_JSON, handledPosition);
      if (delimIndex == -1) {
        if (handledPosition == 0) {
          return template;
        } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
          sbuf.append(template, handledPosition, strPatternLength);
          return sbuf.toString();
        }
      } else {
        if (delimIndex > 0 && template.charAt(delimIndex - 1) == C_BACKSLASH) {
          if (delimIndex > 1 && template.charAt(delimIndex - 2) == C_BACKSLASH) {
            // 转义符之前还有一个转义符，占位符依旧有效
            sbuf.append(template, handledPosition, delimIndex - 1);
            sbuf.append(TypeConvertUtils.utf8Str(params[argIndex]));
            handledPosition = delimIndex + 2;
          } else {
            // 占位符被转义
            argIndex--;
            sbuf.append(template, handledPosition, delimIndex - 1);
            sbuf.append(C_DELIM_START);
            handledPosition = delimIndex + 1;
          }
        } else {
          // 正常占位符
          sbuf.append(template, handledPosition, delimIndex);
          sbuf.append(TypeConvertUtils.utf8Str(params[argIndex]));
          handledPosition = delimIndex + 2;
        }
      }
    }
    // 加入最后一个占位符后所有的字符
    sbuf.append(template, handledPosition, template.length());

    return sbuf.toString();
  }

  /** 下划线转驼峰命名 */
  public static String toUnderScoreCase(String str) {
    if (str == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    // 前置字符是否大写
    boolean preCharIsUpperCase = true;
    // 当前字符是否大写
    boolean curreCharIsUpperCase = true;
    // 下一字符是否大写
    boolean nexteCharIsUpperCase = true;
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (i > 0) {
        preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
      } else {
        preCharIsUpperCase = false;
      }

      curreCharIsUpperCase = Character.isUpperCase(c);

      if (i < (str.length() - 1)) {
        nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
      }

      if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
        sb.append(SEPARATOR);
      } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
        sb.append(SEPARATOR);
      }
      sb.append(Character.toLowerCase(c));
    }
    return sb.toString();
  }


  public static boolean contains(String str, String... strs) {
    if (str != null && strs != null) {
      for (String s : strs) {
        if (str.equals(trim(s))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 是否包含字符串
   *
   * @param str 验证字符串
   * @param strs 字符串组
   * @return 包含返回true
   */
  public static boolean containsIgnoreCase(String str, String... strs) {
    if (str != null && strs != null) {
      for (String s : strs) {
        if (str.equalsIgnoreCase(trim(s))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 是否包含字符串
   *
   * @param str 验证字符串
   * @param target 目标值
   * @param replacement 替换元素
   * @return 包含返回true
   */
  public static String replaceIgnoreCase(String str, String target, String replacement) {
    if (str != null && target != null) {
      return str.replace(target.toLowerCase(), replacement).replace(target.toUpperCase(), replacement);
    }
    return str;
  }

  /**
   * 比较字符串
   *
   * @param str 验证字符串
   * @param str2 比较字符串
   * @return 包含返回true
   */
  public static boolean equalsIgnoreCase(String str, String str2) {
    if (str != null && str2 != null) {
      if (str.equalsIgnoreCase(trim(str2))) {
        return true;
      }
    }
    return false;
  }

  /**
   * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
   *
   * @param name 转换前的下划线大写方式命名的字符串
   * @return 转换后的驼峰式命名的字符串
   */
  public static String convertToCamelCase(String name) {
    StringBuilder result = new StringBuilder();
    // 快速检查
    if (name == null || name.isEmpty()) {
      // 没必要转换
      return "";
    } else if (!name.contains("_")) {
      // 不含下划线，仅将首字母大写
      return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    // 用下划线将原始字符串分割
    String[] camels = name.split("_");
    for (String camel : camels) {
      // 跳过原始字符串中开头、结尾的下换线或双重下划线
      if (camel.isEmpty()) {
        continue;
      }
      // 首字母大写
      result.append(camel.substring(0, 1).toUpperCase());
      result.append(camel.substring(1).toLowerCase());
    }
    return result.toString();
  }

  /** 驼峰式命名法 例如：user_name->userName */
  public static String toCamelCase(String s) {
    if (s == null) {
      return null;
    }
    if (s.indexOf(SEPARATOR) == -1) {
      return s;
    }
    s = s.toLowerCase();
    StringBuilder sb = new StringBuilder(s.length());
    boolean upperCase = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c == SEPARATOR) {
        upperCase = true;
      } else if (upperCase) {
        sb.append(Character.toUpperCase(c));
        upperCase = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  public static <T> T cast(Object obj) {
    return (T) obj;
  }


  public static String substringBefore(String str, String separator) {
    if (!org.springframework.util.StringUtils.isEmpty(str) && separator != null) {
      if (separator.isEmpty()) {
        return "";
      } else {
        int pos = str.indexOf(separator);
        return pos == -1 ? str : str.substring(0, pos);
      }
    } else {
      return str;
    }
  }

  public static String substringBeforeLast(String str, String separator) {
    if (!org.springframework.util.StringUtils.isEmpty(str) && !org.springframework.util.StringUtils.isEmpty(separator)) {
      int pos = str.lastIndexOf(separator);
      return pos == -1 ? str : str.substring(0, pos);
    } else {
      return str;
    }
  }

  public static String substringAfterLast(String str, String separator) {
    if (org.springframework.util.StringUtils.isEmpty(str)) {
      return str;
    } else if (org.springframework.util.StringUtils.isEmpty(separator)) {
      return "";
    } else {
      int pos = str.lastIndexOf(separator);
      return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : "";
    }
  }

  /**
   * 取 子字符 串（指前后字符串所包含的内容）
   * @param str 待处理字符
   * @param tag 开始字符&结束字符
   * @return 子字符
   */
  public static String substringBetween(String str, String tag) {
    return substringBetween(str, tag, tag);
  }

  /**
   * 取 子字符 串（指前后字符串所包含的内容）
   * @param str 待处理字符
   * @param open 开始字符
   * @param close 结束字符
   * @return 子字符
   */
  public static String substringBetween(String str, String open, String close) {
    if (str != null && open != null && close != null) {
      int start = str.indexOf(open);
      if (start != -1) {
        int end = str.indexOf(close, start + open.length());
        if (end != -1) {
          return str.substring(start + open.length(), end);
        }
      }

      return null;
    } else {
      return null;
    }
  }
  public static String trimToEmpty(String str) {
    return str == null ? "" : str.trim();
  }
}
