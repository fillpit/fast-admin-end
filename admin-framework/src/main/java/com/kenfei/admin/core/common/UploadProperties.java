package com.kenfei.admin.core.common;

import com.kenfei.admin.core.config.MvcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 文件上传的配置
 *
 * @author fei
 * @date 2021/11/22 12:02
 */
public class UploadProperties {
  Logger logger = LoggerFactory.getLogger(UploadProperties.class);

  /** 文件保存的路径 */
  @Value("${spring.servlet.multipart.location}")
  private String filePath;
  /** 文件访问的路径 */
  @Value("${app.location.source-url}")
  private String localSourceMapping;

  public String getRequestPath() {
    if (!StringUtils.hasLength(this.localSourceMapping)) {
      this.localSourceMapping = "/file";
    }

    logger.info("静态资源的 访问 路径映射为: {}", this.localSourceMapping);
    return this.localSourceMapping;
  }

  /**
   * 获取资源访问路径
   *
   * @param path 路径层级
   * @return 访问路径
   */
  public String getRequestPath(String path) {
    Objects.requireNonNull(path);
    String requestPath = this.localSourceMapping + path;

    logger.info("静态资源的 访问 路径映射为: {}", requestPath);
    return requestPath;
  }

  public String getSavePath() {

    logger.info("静态资源的 保存 路径映射为: {}", this.filePath);
    return this.filePath;
  }

  /**
   * 获取资源保存路径
   *
   * @param path 路径层级
   * @return 保存路径
   */
  public String getSavePath(String path) {
    Objects.requireNonNull(path);
    String savePath = this.filePath + path;

    logger.info("静态资源的 保存 路径映射为: {}", savePath);
    return savePath;
  }

  /**
   * 获取映射路径
   * @return /
   */
  public String getMappingPath() {
    String mapping = getRequestPath();

    mapping = mapping.replaceAll("\\*", "");
    if (mapping.endsWith("/")) {
      mapping = mapping.concat("**");
    } else {
      mapping = mapping.concat("/**");
    }

    logger.info("静态资源的 映射 路径映射为: {}", mapping);
    return mapping;
  }
}

