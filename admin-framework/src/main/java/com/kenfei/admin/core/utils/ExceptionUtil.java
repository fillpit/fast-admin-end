package com.kenfei.admin.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * 异常信息处理工具
 * @author fei
 * @since 2020/10/28 10:40 上午
 */
public class ExceptionUtil {
  /**
   * 获取异常堆栈信息.
   *
   * @param throwable 异常对象
   * @return 堆栈信息
   */
  public static String getStackTrace(Throwable throwable) {
    // 取完整的信息
    return getStackTrace(throwable, null);
  }

  /**
   * 获取异常堆栈信息.
   *
   * @param throwable 异常对象
   * @param row 获取几行文字
   * @return 堆栈信息
   */
  public static String getStackTrace(Throwable throwable, Integer row) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw, true)) {
      throwable.printStackTrace(pw);
      // 没传获取几行就输出全部
      if (Objects.isNull(row) || row == 0) {
        return sw.toString();
      }

      String[] str = sw.toString().split("\\n\\t");
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < str.length && i < row; i++) {
        result.append(str[i]).append("\n\t");
      }
      return result.toString();
    }
  }
}
