package com.kenfei.admin.core.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * JSON解析处理
 *
 * @author fei
 * @since 2020/10/26 9:17 上午
 */
public class JSON {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

  public static void reade(File file, Object value) throws RuntimeException {
    try {
      OBJECT_WRITER.writeValue(file, value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void reade(OutputStream os, Object value) throws RuntimeException {
    try {
      OBJECT_WRITER.writeValue(os, value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toString(Object value) throws RuntimeException {
    try {
      return OBJECT_WRITER.writeValueAsString(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] toByte(Object value) throws RuntimeException {
    try {
      return OBJECT_WRITER.writeValueAsBytes(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T parseObject(File file, Class<T> valueType) throws RuntimeException {
    try {
      return OBJECT_MAPPER.readValue(file, valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T parseObject(InputStream is, Class<T> valueType) throws RuntimeException {
    try {
      return OBJECT_MAPPER.readValue(is, valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T parseObject(String str, Class<T> valueType) throws RuntimeException {
    try {
      return OBJECT_MAPPER.readValue(str, valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T parseObject(byte[] bytes, Class<T> valueType) throws RuntimeException {
    try {
      if (bytes == null) {
        bytes = new byte[0];
      }
      return OBJECT_MAPPER.readValue(bytes, 0, bytes.length, valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
