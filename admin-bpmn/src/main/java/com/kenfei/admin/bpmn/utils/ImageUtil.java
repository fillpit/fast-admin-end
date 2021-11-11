package com.kenfei.admin.bpmn.utils;

import lombok.experimental.UtilityClass;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.*;

/**
 * 图片转换工具
 * @author fei
 * @date 2021/10/28 17:14
 */
@UtilityClass
public class ImageUtil {

  public void convertToPng(String svgCode, String pngFilePath) {
    File file = new File(pngFilePath);
    FileOutputStream outputStream = null;
    try {
      file.createNewFile();
      convertToPng(svgCode, file);
    } catch (Exception e) {

    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public void convertToPng(String svgCode, File filePng) {
    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(filePng);
      byte[] bytes = svgCode.getBytes("utf-8");
      PNGTranscoder t = new PNGTranscoder();
      TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
      TranscoderOutput output = new TranscoderOutput(outputStream);
      t.transcode(input, output);
      outputStream.flush();
    } catch (Exception e) {
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
