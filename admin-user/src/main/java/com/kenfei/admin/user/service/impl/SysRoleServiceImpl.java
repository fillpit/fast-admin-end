package com.kenfei.admin.user.service.impl;

import com.kenfei.admin.auth.model.CurrentUser;
import com.kenfei.admin.core.common.EnabledEnum;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.user.entity.*;
import com.kenfei.admin.user.repository.SysRoleRepository;
import com.kenfei.admin.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色业务
 *
 * @author fei
 * @date 2017/9/19
 */
@Slf4j
@Service
@Validated
public class SysRoleServiceImpl extends AbstractServiceImpl<SysRoleEntity, Long> implements SysRoleService {

  private final SysRoleRepository sysRoleRepository;

  @Autowired
  public SysRoleServiceImpl(SysRoleRepository sysRoleRepository) {
    super(sysRoleRepository);
    this.sysRoleRepository = sysRoleRepository;
  }

  @Override
  public void untiedMenu(Long mid) {
    // 取消菜单关联
    sysRoleRepository.untiedMenu(mid);
  }

  @Override
  public Integer getLevels() {
    List<Integer> levels = sysRoleRepository.findByUserId(CurrentUser.userId()).stream().map(SysRoleEntity::getLevel).collect(Collectors.toList());

    return Collections.min(levels);
  }

  @Override
  public List<SysRoleEntity> findInMenuId(List<Long> menuIds) {
    return sysRoleRepository.findInMenuId(menuIds);
  }

  @Override
  public List<SysRoleEntity> findAllByIds(Long... ids) {
    return sysRoleRepository.findAllByIdIn(ids);
  }

  @Override
  public SysRoleEntity update(@Valid SysRoleEntity entity) {
    return super.update(entity);
  }

  @Override
  public Set<Long> getRolePermission(@NotNull Long id) {
    SysRoleEntity sysRole = this.findById(id);

    return sysRole.getMenuIds();
  }

  @Override
  public @NotNull List<SysRoleEntity> findAllValid() {
    return sysRoleRepository.findAllByEnabled(EnabledEnum.ON.value());
  }

  @Override
  public SysRoleEntity save(SysRoleEntity entity) {
    // 验证权限等级
    checkLevels(entity.getLevel());
    // 保存
    return super.save(entity);
  }

  @Override
  public void delete(SysRoleEntity entity) {
    // 验证权限等级
    checkLevels(entity.getLevel());
    // 删除
    super.delete(entity);
  }

  /**
   * 检查当前用户的角色级别
   */
  private void checkLevels(Integer level){
    int min = getLevels();
    if(level != null && level < min){
      throw new AppException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
    }
  }
}
