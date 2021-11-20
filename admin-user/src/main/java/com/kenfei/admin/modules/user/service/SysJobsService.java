package com.kenfei.admin.modules.user.service;

import com.kenfei.admin.modules.user.entity.SysJobsEntity;
import com.kenfei.admin.core.common.base.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (SysJob)表服务接口
 *
 * @author kenfei
 * @since 2021-08-17 20:08:07
 */
public interface SysJobsService extends InterfaceService<SysJobsEntity, Long> {
  /**
   * 主页的列表数据
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<SysJobsEntity> index(String name, Boolean enabled, Pageable pageable);


  List<SysJobsEntity> list(Boolean enabled);
}
