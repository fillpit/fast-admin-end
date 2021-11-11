package com.kenfei.admin.bpmn.service;

import com.kenfei.admin.bpmn.entity.ProcessRecordEntity;
import com.kenfei.admin.core.common.base.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * (TbProcessRecord)表服务接口
 *
 * @author kenfei
 * @since 2021-10-28 16:34:39
 */
public interface ProcessRecordService extends InterfaceService<ProcessRecordEntity, Integer> {
  /**
   * 主页的列表数据
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<ProcessRecordEntity> index(Pageable pageable);

  /**
   * 部署流程
   * @param id 流程ID
   * @return 更新后的流程对象
   */
  ProcessRecordEntity deploy(Integer id);
}


