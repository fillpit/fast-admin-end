package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.user.entity.SysDeptEntity;
import com.admin.user.entity.SysMenuEntity;
import com.admin.core.enums.EnabledEnum;
import com.admin.user.enums.MenuTypeEnum;
import com.admin.user.repository.SysMenuRepository;
import com.admin.user.service.SysMenuService;
import com.admin.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限表
 *
 * @author fei
 * @since 2019-01-10 23:28
 */
@Slf4j
@Service
@Validated
public class SysMenuServiceImpl extends AbstractServiceImpl<SysMenuEntity, Long>
    implements SysMenuService {
  private final SysMenuRepository sysMenuRepository;
  private final SysRoleService sysRoleService;

  @Autowired
  public SysMenuServiceImpl(SysMenuRepository sysMenuRepository, SysRoleService sysRoleService) {
    super(sysMenuRepository);
    this.sysMenuRepository = sysMenuRepository;
    this.sysRoleService = sysRoleService;
  }

  @Override
  public List<SysMenuEntity> getSuperior(SysMenuEntity sysMenuEntity, List<SysMenuEntity> menus) {
    if(sysMenuEntity.getParentId() == null){
      menus.addAll(sysMenuRepository.findAllByParentIdIsNull());
      return menus;
    }
    menus.addAll(sysMenuRepository.findAllByParentId(sysMenuEntity.getParentId()));
    return getSuperior(findById(sysMenuEntity.getParentId()), menus);
  }

  @Override
  public Set<SysMenuEntity> getChildMenus(SysMenuEntity menu, Set<SysMenuEntity> menuSet) {
    List<SysMenuEntity> childs = this.findChildrenList(menu.getId());
    for (SysMenuEntity child : childs) {
      menuSet.add(child);
      getChildMenus(child, menuSet);
    }
    return menuSet;
  }

  @Override
  public Page<SysMenuEntity> indexList(String menuName, Pageable pageable) {
    return this.findAllForMenu(menuName, pageable);
  }

  @Override
  public List<SysMenuEntity> findChildrenList(Long parentId) {
    return sysMenuRepository.findAll(
      (root, criteriaQuery, criteriaBuilder) -> {
        List<Predicate> list = new LinkedList<>();
        if (Objects.isNull(parentId)) {
          list.add(criteriaBuilder.isNull(root.get("parentId")));
        } else {
          list.add(criteriaBuilder.equal(root.get("parentId"), parentId));
        }

        return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
      });
  }

  /**
   * 查询子菜单的公共条件
   * @param menuName 菜单名称
   * @return 条件构造对象
   */
  private Specification<SysMenuEntity> findChildrenWhere(String menuName) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();
      if (StringUtils.hasLength(menuName)) {
        list.add(criteriaBuilder.equal(root.get("menuName"), menuName));
      }

      list.add(criteriaBuilder.equal(root.get("enabled"), true));
      list.add(criteriaBuilder.isNotNull(root.get("parentId")));

      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }

  @Override
  public List<SysMenuEntity> findMenuTree() {
    return this.tree(null, this.findMenu(null));
  }

  @Override
  public Page<SysMenuEntity> findAllForMenu(String menuName, Pageable pageable) {
    return sysMenuRepository.findAll(
        (root, criteriaQuery, criteriaBuilder) -> {
          List<Predicate> list = new LinkedList<>();
          if (StringUtils.hasLength(menuName)) {
            list.add(criteriaBuilder.equal(root.get("menuName"), menuName));
          }

          list.add(criteriaBuilder.isNull(root.get("parentId")));

          return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
        },
        pageable);
  }

  @Override
  public List<SysMenuEntity> findAllForPermission() {
    return sysMenuRepository.findAll(findWhere(MenuTypeEnum.BUTTON.value(), true));
  }

  @Override
  public List<SysMenuEntity> findByRoleIds(Long[] roleIds) {
    return sysMenuRepository.findByRoleIds(roleIds);
  }

  @Override
  public List<SysMenuEntity> findByRoleIdsAndType(Long[] roleIds, Integer type) {
    return sysMenuRepository.findByRoleIdsAndType(roleIds, type);
  }

  @Override
  public List<SysMenuEntity> buildTree(List<SysMenuEntity> menus) {
    return this.tree(null, menus);
  }

  @Override
  public void delete(Iterable<? extends SysMenuEntity> entities) {
    for (SysMenuEntity menu : entities) {
      sysRoleService.untiedMenu(menu.getId());
      this.delete(menu.getId());
    }
  }

  /**
   * 查询公共条件
   * @param type 菜单类型
   * @param enabled 菜单名称
   * @return 条件构造对象
   */
  private Specification<SysMenuEntity> findWhere(Integer type, Boolean enabled) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();
      if (!Objects.isNull(enabled)) {
        list.add(criteriaBuilder.equal(root.get("enabled"), enabled));
      }

      if (Objects.isNull(type)) {
        list.add(criteriaBuilder.isNull(root.get("type")));
      } else {
        list.add(criteriaBuilder.equal(root.get("type"), type));
      }

      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }

  private List<SysMenuEntity> findMenu(@NotEmpty List<Integer> containId) {
    return sysMenuRepository.findAll(
      (root, criteriaQuery, criteriaBuilder) -> {
        List<Predicate> list = new LinkedList<>();
        list.add(criteriaBuilder.equal(root.get("enabled"), EnabledEnum.ON.value()));

        // 判断是否有过滤条件
        if (!ObjectUtils.isEmpty(containId)) {
          list.add(root.get("id").in(containId));
        }

        return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
      });
  }

  private List<SysMenuEntity> tree(Long parentId, List<SysMenuEntity> menus) {
    List<SysMenuEntity> current =
      menus.stream()
        .parallel()
        .filter(v -> Objects.equals(parentId, v.getParentId()))
        .distinct()
        .collect(Collectors.toList());
    for (SysMenuEntity menu : current) {
      List<SysMenuEntity> childrenMenu = this.tree(menu.getId(), menus);
      // 把按钮和菜单分开
      Map<Integer, List<SysMenuEntity>> map =
          childrenMenu.parallelStream().collect(Collectors.groupingBy(SysMenuEntity::getType));
      menu.setPermissions(map.get(MenuTypeEnum.BUTTON.value()));
      menu.setChildren(map.get(MenuTypeEnum.MENU.value()));
    }

    return current;
  }

}
