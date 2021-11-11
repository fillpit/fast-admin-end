package com.kenfei.admin.user.service;

import com.kenfei.admin.core.common.base.InterfaceService;
import com.kenfei.admin.user.entity.SysMenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表
 * @author fei
 * @since 2019-01-10 23:27
 */
public interface SysMenuService extends InterfaceService<SysMenuEntity, Long> {

  /**
   * 根据ID获取同级与上级数据
   * @param sysMenuEntity /
   * @param objects /
   * @return /
   */
  List<SysMenuEntity> getSuperior(SysMenuEntity sysMenuEntity, List<SysMenuEntity> objects);

  /**
   * 获取该菜单所有的子菜单（铺平）
   * @param menu 当前菜单
   * @param menuSet 递归用，首次调用可传入一个空集合
   * @return 菜单集合
   */
  Set<SysMenuEntity> getChildMenus(SysMenuEntity menu, Set<SysMenuEntity> menuSet);

  /**
   * 权限维护主页的列表数据
   * @param menuName 权限名称
   * @param pageable 分页参数
   * @return 权限集合
   */
  Page<SysMenuEntity> indexList(String menuName, Pageable pageable);


  /**
   * 获取子菜单
   * @param parentId 菜单id
   * @return 子菜单集合（不分页）
   */
  List<SysMenuEntity> findChildrenList(Long parentId);

  /**
   * 获取菜单树
   *
   * @return 菜单集合
   */
  List<SysMenuEntity> findMenuTree();

  /**
   * 获取所有的菜单
   * @param menuName 菜单名称
   * @param pageable 分页参数
   * @return 菜单集合
   */
  Page<SysMenuEntity> findAllForMenu(String menuName, Pageable pageable);

  /**
   * 获取所有的权限
   * @return
   */
  List<SysMenuEntity> findAllForPermission();

  /**
   * 根据角色ID 和 菜单类型查找菜单集合
   * @param roleIds 角色IDs
   * @param type 菜单类型
   * @return 菜单集合
   */
  List<SysMenuEntity> findByRoleIdsAndType(Long[] roleIds, Integer type);

  /**
   * 根据角色ID查找菜单集合
   * @param roleIds 角色IDs
   * @return 菜单集合
   */
  List<SysMenuEntity> findByRoleIds(Long[] roleIds);

  /**
   * 把菜单列表转换为菜单树
   * @param menus 菜单集合
   * @return 菜单树
   */
  List<SysMenuEntity> buildTree(List<SysMenuEntity> menus);
}
