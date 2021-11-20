package com.kenfei.admin.modules.user.repository;

import com.kenfei.admin.core.common.base.BaseRepository;
import com.kenfei.admin.modules.user.entity.SysRoleEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 角色数据访问接口
 *
 * @author fei
 * @date 2017/9/19
 */
public interface SysRoleRepository extends BaseRepository<SysRoleEntity, Long> {

  /**
   * 获取角色集合
   *
   * @param ids 角色ID
   * @return 角色集合
   */
  List<SysRoleEntity> findAllByIdIn(Long... ids);

  /**
   * 根据有效状态获取所有的角色信息
   * @param enabled 有效状态
   * @return 角色集合
   */
  List<SysRoleEntity> findAllByEnabled(Integer enabled);


  /**
   * 根据用户ID查询
   * @param id 用户ID
   * @return /
   */
  @Query(value = "SELECT r.* FROM sys_role r, sys_users_roles u WHERE " +
    "r.id = u.role_id AND u.user_id = ?1",nativeQuery = true)
  Set<SysRoleEntity> findByUserId(Long id);

  /**
   * 解绑角色菜单
   * @param id 菜单ID
   */
  @Modifying
  @Query(value = "delete from sys_roles_menus where menu_id = ?1",nativeQuery = true)
  void untiedMenu(Long id);

  /**
   * 根据部门查询
   * @param deptIds 部门ID集合
   * @return /
   */
  @Query(value = "select count(1) from sys_role r, sys_roles_depts d where " +
    "r.role_id = d.role_id and d.dept_id in ?1",nativeQuery = true)
  int countByDepts(Set<Long> deptIds);

  /**
   * 根据菜单Id查询
   * @param menuIds /
   * @return /
   */
  @Query(value = "SELECT r.* FROM sys_role r, sys_roles_menus m WHERE " +
    "r.id = m.role_id AND m.menu_id in ?1",nativeQuery = true)
  List<SysRoleEntity> findInMenuId(List<Long> menuIds);
}
