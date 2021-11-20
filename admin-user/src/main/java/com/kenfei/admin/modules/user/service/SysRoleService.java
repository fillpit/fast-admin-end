package com.kenfei.admin.modules.user.service;

import com.kenfei.admin.core.common.base.InterfaceService;
import com.kenfei.admin.modules.user.entity.SysRoleEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 角色业务
 * @author fei
 * @date 2017/9/19
 */
public interface SysRoleService extends InterfaceService<SysRoleEntity, Long> {

  /**
   * 取消菜单关联
   * @param mid 菜单ID
   */
  void untiedMenu(Long mid);

  /**
   * 获取当前角色的等级
   *
   * @return 等级
   */
  Integer getLevels();

  /**
   * 根据菜单ID获取角色
   * @param menuIds 菜单ID
   * @return /
   */
  List<SysRoleEntity> findInMenuId(List<Long> menuIds);

  /**
   * 根据角色ID集合获取角色信息.
   * @param ids 角色ID集合
   * @return 角色集合
   */
  List<SysRoleEntity> findAllByIds(Long... ids);

  /**
   * 获取角色权限
   * @param id 角色ID
   * @return 权限集合
   */
  Set<Long> getRolePermission(@NotNull Long id);

  /**
   * 获取所有有效的角色对象
   * @return 角色集合
   */
  @NotNull
  List<SysRoleEntity> findAllValid();
}
