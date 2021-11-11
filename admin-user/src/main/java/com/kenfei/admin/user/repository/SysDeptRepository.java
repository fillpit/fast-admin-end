package com.kenfei.admin.user.repository;

import com.kenfei.admin.core.common.base.BaseRepository;
import com.kenfei.admin.user.entity.SysDeptEntity;

import java.lang.Long;
import java.util.List;

/**
 * (SysDept)表数据库访问层
 *
 * @author kenfei
 * @since 2021-08-16 16:23:38
 */
public interface SysDeptRepository extends BaseRepository<SysDeptEntity, Long> {

  /**
   * 查询所有的顶级部门
   * @return /
   */
  List<SysDeptEntity> findAllByParentIdIsNull();

  /**
   * 查询下一级子部门
   * @param parentId ID
   * @return /
   */
  List<SysDeptEntity> findAllByParentId(Long parentId);
}