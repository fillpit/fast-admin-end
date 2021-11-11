package com.kenfei.admin.user.service;

import com.kenfei.admin.user.entity.SysDictEntity;
import com.kenfei.admin.core.common.base.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 数据字典(SysDict)表服务接口
 *
 * @author kenfei
 * @since 2021-08-16 22:18:11
 */
public interface SysDictService extends InterfaceService<SysDictEntity, Long> {
  /**
   * 主页的列表数据
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<SysDictEntity> index(Pageable pageable);
}
