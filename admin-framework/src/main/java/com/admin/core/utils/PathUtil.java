package com.admin.core.utils;

import org.springframework.web.util.UriUtils;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author fei
 * @date 2021/8/22 15:34
 */
public final class PathUtil {
  /**
   * 获取jar包运行时的当前目录
   * @return {String}
   */
  @Nullable
  public static String getJarPath() {
    try {
      URL url = PathUtil.class.getResource("/").toURI().toURL();
      return PathUtil.toFilePath(url);
    } catch (Exception e) {
      String path = PathUtil.class.getResource("").getPath();
      return new File(path).getParentFile().getParentFile().getAbsolutePath();
    }
  }

  @Nullable
  private static String toFilePath(@Nullable URL url) {
    if (url == null) { return null; }
    String protocol = url.getProtocol();
    String file = UriUtils.decode(url.getPath(), StandardCharsets.UTF_8);
    if (ResourceUtils.URL_PROTOCOL_FILE.equals(protocol)) {
      return new File(file).getParentFile().getParentFile().getAbsolutePath();
    } else if (ResourceUtils.URL_PROTOCOL_JAR.equals(protocol)
      || ResourceUtils.URL_PROTOCOL_ZIP.equals(protocol)) {
      int ipos = file.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
      if (ipos > 0) {
        file = file.substring(0, ipos);
      }
      if (file.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
        file = file.substring(ResourceUtils.FILE_URL_PREFIX.length());
      }
      return new File(file).getParentFile().getAbsolutePath();
    }
    return file;
  }
}
