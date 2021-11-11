package com.admin.bpmn.service;

/**
 * 流程追踪图生成类
 * @author fei
 * @date 2021/10/30 14:57
 */
public interface ImageService {
  /**
   * 根据流程实例标识，生成流程跟踪图示（高亮）
   *
   * @param procInstId 流程实例标识
   * @return
   * @throws Exception
   */
  byte[] generateImageByProcInstId(String procInstId) throws Exception;

}
