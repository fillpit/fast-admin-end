package com.admin.user.controller;

import com.admin.core.basic.AbstractController;
import com.admin.user.dto.SysRoleDto;
import com.admin.user.entity.SysRoleEntity;
import com.admin.user.service.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色接口
 *
 * @author fei
 * @date 2017/9/19
 */
@RestController
@RequestMapping("roles")
public class SysRoleController extends AbstractController<SysRoleEntity, Long> {

  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER =
        ExampleMatcher.matchingAll()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            .withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
            .withIgnorePaths("permissionIds");
  }

  private final SysRoleService roleService;

  @Autowired
  public SysRoleController(SysRoleService roleService) {
    super(roleService);
    this.roleService = roleService;
  }

  /**
   * 获取所有有效的角色集合
   *
   * @return 角色集合
   */
  @GetMapping("all_valid")
  public List<SysRoleEntity> findAllValid() {
    SysRoleEntity sysRole = new SysRoleEntity();

    Example<SysRoleEntity> example = Example.of(sysRole, EXAMPLE_MATCHER);

    return roleService.findAll(example);
  }

  /**
   * 获取所有的角色信息 (分页)
   *
   * @param roleName 角色名称
   * @param pageable 分页参数
   * @return 角色集合
   */
  @GetMapping(value = "index", params = {"page"})
  public Page<SysRoleEntity> indexPage(String roleName, @PageableDefault Pageable pageable) {
    SysRoleEntity sysRole = new SysRoleEntity();
    sysRole.setName(roleName);

    return roleService.findAll(Example.of(sysRole, EXAMPLE_MATCHER), pageable);
  }

  /**
   * 获取所有的角色信息(不分页)
   * @param roleName 角色名称
   * @return 角色集合
   */
  @GetMapping(value = "list")
  public List<SysRoleEntity> index(String roleName) {
    SysRoleEntity sysRole = new SysRoleEntity();
    sysRole.setName(roleName);

    return roleService.findAll(Example.of(sysRole, EXAMPLE_MATCHER));
  }

  /**
   * 保存角色信息
   *
   * @param dto 角色表单
   * @return 保存后的角色信息
   */
  @PostMapping
  public SysRoleEntity save(@Valid @RequestBody SysRoleDto dto) {
    SysRoleEntity sysRole = dto.convert(SysRoleEntity.class);
    return roleService.save(sysRole);
  }

  /**
   * 更新角色信息
   *
   * @param id 角色ID
   * @param dto 角色表单
   * @return 更新后的角色信息
   */
  @PutMapping("/{id}")
  public SysRoleEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysRoleDto dto) {
    SysRoleEntity sysRole = roleService.findById(id);
    BeanUtils.copyProperties(dto, sysRole);
    return roleService.update(sysRole);
  }

  /**
   * 删除
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    Set<SysRoleEntity> SysDeptSet = new HashSet<>();
    for (Long id : ids) {
      SysRoleEntity sysDept = roleService.findById(id);
      SysDeptSet.add(sysDept);
    }
    roleService.delete(SysDeptSet);
  }


  /**
   * 更新角色权限
   *
   * @param id 角色ID
   * @param ids 权限ID集合
   * @return 更新后的角色信息
   */
  @PutMapping("/{id}/menu")
  public SysRoleEntity authorization(@PathVariable Long id, @RequestBody Set<Long> ids) {
    SysRoleEntity sysRole = roleService.findById(id);
    sysRole.setMenuIds(ids);

    return roleService.save(sysRole);
  }

  /**
   * 获取当前用户的最小级别
   * @return 级别编号
   */
  @GetMapping(value = "level")
  public Integer getLevel(){
    return roleService.getLevels();
  }
}

