package com.admin.user.service;

import com.admin.user.entity.SysDictDetailEntity;
import com.admin.core.basic.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 数据字典详情(SysDictDetail)表服务接口
 *
 * @author kenfei
 * @since 2021-08-16 22:18:42
 */
public interface SysDictDetailService extends InterfaceService<SysDictDetailEntity, Long> {
  /**
   * 主页的列表数据
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<SysDictDetailEntity> index(String dictName, Pageable pageable);

  /**
   * 获取数据列表
   * @param dictName 字典名称
   * @return /
   */
  List<SysDictDetailEntity> list(String dictName);
}
