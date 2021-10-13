package com.admin.user.service;

import com.admin.user.entity.SysDictEntity;
import com.admin.core.basic.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
