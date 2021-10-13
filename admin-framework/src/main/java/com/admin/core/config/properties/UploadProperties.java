package com.admin.core.config.properties;

import com.admin.core.utils.PathUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

/**
 * 文件上传配置
 *
 * @author fei
 * @date 2021/8/22 15:28
 */
@ConfigurationProperties(UploadProperties.PREFIX)
public class UploadProperties {
  public static final String PREFIX = "app.upload";

  /** 是否启用本地的文件上传，默认：开启 */
  private boolean enable = true;

  /** 上传的文件 路径匹配 */
  private String uploadPathPattern = "/upload/**";

  /** 文件保存目录，默认：jar 包同级目录 */
  @Nullable private String savePath = PathUtil.getJarPath() + "/upload/";

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getUploadPathPattern() {
    return uploadPathPattern;
  }

  public void setUploadPathPattern(String uploadPathPattern) {
    this.uploadPathPattern = uploadPathPattern;
  }

  @Nullable
  public String getSavePath() {
    return savePath;
  }

  public void setSavePath(@Nullable String savePath) {
    this.savePath = savePath;
  }
}
