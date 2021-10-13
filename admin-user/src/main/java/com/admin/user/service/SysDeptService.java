package com.admin.user.service;

import com.admin.user.entity.SysDeptEntity;
import com.admin.core.basic.InterfaceService;
import com.admin.user.entity.SysMenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (SysDept)表服务接口
 *
 * @author kenfei
 * @since 2021-08-16 16:23:38
 */
public interface SysDeptService extends InterfaceService<SysDeptEntity, Long> {

  /**
   * 权限维护主页的列表数据
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<SysDeptEntity> index(Long parentId, Pageable pageable);

  /**
   * 获取数据列表
   * @param enabled 状态
   * @return 数据列表
   */
  List<SysDeptEntity> list(Boolean enabled, Long parentId);

  /**
   * 根据ID获取同级与上级数据
   * @param entity /
   * @param objects /
   * @return /
   */
  List<SysDeptEntity> getSuperior(SysDeptEntity entity, List<SysDeptEntity> objects);

  /**
   * 查询下一级数据
   * @param parentId 父ID
   * @return /
   */
  List<SysDeptEntity> findByParentId(Long parentId);

  /**
   * 获取子数据 （递归）
   * @param parentId id
   * @return 子数据集合（不分页）
   */
  List<SysDeptEntity> findChildrenList(Long parentId);

  /**
   * 把列表转换为树
   * @param entitys /
   * @return /
   */
  List<SysDeptEntity> buildTree(List<SysDeptEntity> entitys);
}
