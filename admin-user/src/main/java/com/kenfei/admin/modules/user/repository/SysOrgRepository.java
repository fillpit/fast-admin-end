package com.kenfei.admin.modules.user.repository;

import com.kenfei.admin.modules.user.entity.SysOrgEntity;
import com.kenfei.admin.core.common.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.lang.Long;
import java.util.List;

/**
 * (SysOrg)表数据库访问层
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
public interface SysOrgRepository extends BaseRepository<SysOrgEntity, Long> {

  /**
   * 查询所有的顶级节点
   * @return /
   */
  List<SysOrgEntity> findAllByParentIdIsNull();

  /**
   * 查询下一级子节点
   * @param parentId ID
   * @return /
   */
  List<SysOrgEntity> findAllByParentId(Long parentId);

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
  @Query(value = " update sys_org set sub_count = ?1 where id = ?2 ",nativeQuery = true)
  void updateSubCntById(Integer count, Long id);
}

