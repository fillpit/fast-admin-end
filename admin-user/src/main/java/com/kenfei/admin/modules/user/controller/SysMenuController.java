package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.dto.SysMenuButtonDto;
import com.kenfei.admin.modules.user.dto.SysMenuDto;
import com.kenfei.admin.modules.user.entity.SysMenuEntity;
import com.kenfei.admin.modules.user.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单资源接口.
 *
 * @author fei
 * @date 2017/9/19
 */
@Slf4j
@RestController
@RequestMapping("menu")
public class SysMenuController extends AbstractController<SysMenuEntity, Long> {

  private final SysMenuService sysMenuService;

  @Autowired
  public SysMenuController(SysMenuService sysMenuService) {
    super(sysMenuService);
    this.sysMenuService = sysMenuService;
  }

  /**
   * 获取菜单树.
   *
   * @return 菜单集合
   */
  @GetMapping("menu_tree")
  public List<SysMenuEntity> findMenuTree() {
    return sysMenuService.findMenuTree();
  }

  /**
   * 获取所有的菜单.
   *
   * @param name 菜单名称
   * @param pageable 分页参数
   * @return 菜单集合
   */
  @GetMapping("index")
  public Page<SysMenuEntity> index(String name, @PageableDefault Pageable pageable) {
    return sysMenuService.indexList(name, pageable);
  }

  /**
   * 获取子菜单集合
   * @param parentId 菜单ID
   * @return 子菜单集合(不分页)
   */
  @GetMapping("children_list")
  public List<SysMenuEntity> findChildrenList(Long parentId) {
    return sysMenuService.findChildrenList(parentId);
  }

  /**
   * 查询菜单:根据ID获取同级与上级数据（编辑时懒加载用于显示默认值）
   * @param id id
   * @return 菜单集合
   */
  @GetMapping("/superior")
  public List<SysMenuEntity> getSuperior(Long id) {
    if (!Objects.isNull(id)) {
      SysMenuEntity sysMenuEntity = sysMenuService.findById(id);
      List<SysMenuEntity> menus = sysMenuService.getSuperior(sysMenuEntity, new ArrayList<>());
      return sysMenuService.buildTree(menus);
    }
    return sysMenuService.findChildrenList(null);
  }

  /**
   * 获取所有子菜单id集合(深度递归)
   * @param parentId 菜单ID
   * @return 子菜单集合(不分页)
   */
  @GetMapping("children")
  public Set<Long> findChildrenIds(Long parentId) {
    Set<SysMenuEntity> menuSet = new HashSet<>();
    SysMenuEntity sysMenu = sysMenuService.findById(parentId);
    menuSet.add(sysMenu);
    menuSet = sysMenuService.getChildMenus(sysMenu, menuSet);
    return  menuSet.stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
  }


  /**
   * 保存菜单.
   *
   * @param dto 菜单表单
   * @return 保存后的菜单对象
   */
  @PostMapping
  public SysMenuEntity save(@Valid @RequestBody SysMenuDto dto) {

    SysMenuEntity sysMenu = dto.convert(SysMenuEntity.class);

    return sysMenuService.save(sysMenu);
  }

  /**
   * 更新菜单信息.
   * @param id 菜单ID
   * @param dto 菜单信息
   * @return 更新后的菜单信息
   */
  @PutMapping("{id}")
  public SysMenuEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysMenuDto dto) {
    SysMenuEntity sysMenu = sysMenuService.findById(id);

    BeanUtils.copyProperties(dto, sysMenu);

    return sysMenuService.update(sysMenu);
  }

  /**
   * 保存菜单按钮.
   *
   * @param dto 菜单表单
   * @return 保存后的菜单对象
   */
  @PostMapping(value = "button")
  public SysMenuEntity saveButton(@Valid @RequestBody SysMenuButtonDto dto) {

    SysMenuEntity sysMenu = dto.convert(SysMenuEntity.class);

    return sysMenuService.save(sysMenu);
  }

  /**
   * 更新菜单信息按钮.
   * @param id 菜单ID
   * @param dto 菜单信息
   * @return 更新后的菜单信息
   */
  @PutMapping("button/{id}")
  public SysMenuEntity updateButton(@NotNull @PathVariable Long id, @Valid @RequestBody SysMenuButtonDto dto) {
    SysMenuEntity sysMenu = sysMenuService.findById(id);

    BeanUtils.copyProperties(dto, sysMenu);

    return sysMenuService.update(sysMenu);
  }

  /**
   * 删除菜单
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    Set<SysMenuEntity> menuSet = new HashSet<>();
    for (Long id : ids) {
      SysMenuEntity menu = sysMenuService.findById(id);
      menuSet.add(menu);
      menuSet = sysMenuService.getChildMenus(menu, menuSet);
    }
    sysMenuService.delete(menuSet);
  }

}


