package com.kenfei.admin.user.repository;

import com.kenfei.admin.core.common.base.BaseRepository;
import com.kenfei.admin.user.entity.SysMenuEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 菜单权限表
 * @author fei
 * @since 2017/9/19
 */
public interface SysMenuRepository extends BaseRepository<SysMenuEntity, Long> {

  /**
   * 获取菜单集合.
   *
   * @param ids 菜单ID数组
   * @param enabled 状态
   * @return 菜单集合
   */
  List<SysMenuEntity> findAllByIdInAndEnabled(Long[] ids, Integer enabled);

  /**
   * 获取所有的子菜单
   *
   * @param parentId 父级菜单ID
   * @return 菜单集合
   */
  List<SysMenuEntity> findAllByParentId(Long parentId);

  /**
   * 查询没有上级菜单ID 的 数据
   * @return 菜单集合
   */
  List<SysMenuEntity> findAllByParentIdIsNull();


  /**
   * 根据角色ID与菜单类型查询菜单
   * @param roleIds roleIDs
   * @param type 类型
   * @return /
   */
  @Query(value = "SELECT m.* FROM sys_menu m, sys_roles_menus r WHERE " +
    "m.id = r.menu_id AND r.role_id IN ?1 AND m.type = ?2 order by m.menu_sort asc",nativeQuery = true)
  List<SysMenuEntity> findByRoleIdsAndType(Long[] roleIds, Integer type);


  /**
   * 根据角色ID与菜单类型查询菜单
   * @param roleIds roleIDs
   * @return /
   */
  @Query(value = "SELECT m.* FROM sys_menu m, sys_roles_menus r WHERE " +
    "m.id = r.menu_id AND r.role_id IN ?1 order by m.menu_sort asc",nativeQuery = true)
  List<SysMenuEntity> findByRoleIds(Long[] roleIds);

}
