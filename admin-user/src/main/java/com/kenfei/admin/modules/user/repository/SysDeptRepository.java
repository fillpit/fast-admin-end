package com.kenfei.admin.modules.user.repository;

import com.kenfei.admin.core.common.base.BaseRepository;
import com.kenfei.admin.modules.user.entity.SysDeptEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

  /**
   * 统计子节点的个数
   * @param parentId 父ID
   * @return /
   */
  Integer countByParentId(Long parentId);

  /**
   * 根据ID更新sub_count
   * @param count /
   * @param id /
   */
  @Modifying
  @Query(value = " update sys_dept set sub_count = ?1 where id = ?2 ",nativeQuery = true)
  void updateSubCntById(Integer count, Long id);
}
