package com.kenfei.admin.modules.user.service;

import com.kenfei.admin.modules.user.entity.SysOrgEntity;
import com.kenfei.admin.core.common.base.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (SysOrg)表服务接口
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
public interface SysOrgService extends InterfaceService<SysOrgEntity, Long> {
  /**
   * 权限维护主页的列表数据
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<SysOrgEntity> index(Long parentId, Pageable pageable);

  /**
   * 获取数据列表
   * @param enabled 状态
   * @return 数据列表
   */
  List<SysOrgEntity> list(Boolean enabled, Long parentId);

  /**
   * 根据ID获取同级与上级数据
   * @param entity /
   * @param objects /
   * @return /
   */
  List<SysOrgEntity> getSuperior(SysOrgEntity entity, List<SysOrgEntity> objects);

  /**
   * 查询下一级数据
   * @param parentId 父ID
   * @return /
   */
  List<SysOrgEntity> findByParentId(Long parentId);

  /**
   * 获取子数据 （递归）
   * @param parentId id
   * @return 子数据集合（不分页）
   */
  List<SysOrgEntity> findChildrenList(Long parentId);

  /**
   * 把列表转换为树
   * @param entitys /
   * @return /
   */
  List<SysOrgEntity> buildTree(List<SysOrgEntity> entitys);
}


