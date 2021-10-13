package com.admin.core.controller;

import com.admin.core.exception.AppException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author fei
 * @date 2017/11/17
 */
public class ControllerUtils {

  /**
   * 获取请求发起者的IP地址
   *
   * @param request 请求对象
   * @return IP地址字符串
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * 文件上传保存方法(保存单个文件)
   *
   * @param uploadDir 上传文件目录
   * @param file 上传对象
   * @throws IOException 流异常
   */
  public static File executeUpload(String uploadDir, MultipartFile file) throws IOException {
    Objects.requireNonNull(uploadDir, "保存路径不能为空");
    Objects.requireNonNull(file);

    List<File> files = executeUpload(uploadDir, new MultipartFile[] {file});
    return Optional.ofNullable(files).map(v -> v.get(0)).orElseThrow(() -> new AppException("文件处理失败"));
  }

  /**
   * 文件上传保存方法(保存多个文件)
   *
   * @param uploadDir 上传文件目录
   * @param files 上传对象
   * @throws IOException
   */
  public static List<File> executeUpload(String uploadDir, MultipartFile[] files) throws IOException {
    Objects.requireNonNull(uploadDir, "保存路径不能为空");

    // 如果目录不存在，自动创建文件夹
    File dir = new File(uploadDir);
    if (!dir.exists()) {
      dir.mkdir();
    }

    // 将上传的文件写入到服务器端文件内
    List<File> result = null;
    for (MultipartFile multipartFile : files) {
      if (null == result) {
        result = new LinkedList<>();
      }
      // 文件后缀名
      String suffix =
          multipartFile
              .getOriginalFilename()
              .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
      // 上传文件名
      String filename = UUID.randomUUID() + suffix;
      // 服务器端保存的文件对象
      File serverFile = new File(uploadDir + "/" + filename);

      multipartFile.transferTo(serverFile);

      result.add(serverFile);

    }
    return result;
  }

  /**
   * excel 导出工具
   * @param response 响应类
   * @param fileName 文件名
   * @return 输出流
   * @throws IOException
   */
  public static OutputStream getExportExcelStream(HttpServletResponse response, String fileName)
      throws IOException {
    Objects.requireNonNull(fileName, "文件名不能为空");
    // 清空response
    response.reset();
    // 设置response的Header
    String fileNameEndcode = new String(fileName.getBytes(), "ISO8859-1");
    response.addHeader("Content-Disposition", "attachment;filename=" + fileNameEndcode);
    response.setContentType("application/vnd.ms-excel;charset=utf-8");

    return response.getOutputStream();
  }
}
