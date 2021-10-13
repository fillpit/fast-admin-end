package com.admin.user.controller;

import com.admin.auth.model.CurrentUser;
import com.admin.auth.config.security.JwtUser;
import com.admin.core.annotations.JsonParam;
import com.admin.core.basic.AbstractController;
import com.admin.core.exception.AppException;
import com.admin.user.dto.SysUserDto;
import com.admin.user.entity.SysUserEntity;
import com.admin.user.service.SysUserService;
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
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * 用户接口层
 *
 * @author fei
 * @date 2017/9/19
 */
@RestController
@RequestMapping("users")
public class SysUserController extends AbstractController<SysUserEntity, Long> {

  private final SysUserService userService;
  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER = ExampleMatcher
      .matchingAll()
      .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
      .withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
      .withIgnorePaths("password");
  }

  @Autowired
  public SysUserController(SysUserService userService) {
    super(userService);
    this.userService = userService;
  }

  /**
   * 获取所有的用户信息
   * @param userName 用户名
   * @param pageable 分页参数
   * @return 用户集合
   */
  @GetMapping(value = "index", params = {"page"})
  public Page<SysUserEntity> index(String userName, Long deptId, @PageableDefault Pageable pageable) {
    return userService.index(userName, deptId, pageable);
  }

  /**
   * 检查用户名是否存在
   *
   * @param name 用户名
   * @return true:不存在，false 存在
   */
  @GetMapping("checkUserName")
  public boolean checkUserName(@NotEmpty(message = "用户名不能为空") String name) {
    return userService.checkUserName(name);
  }


  /**
   * 用户注册接口
   * @param dto 表单数据
   * @return token
   */
  @PostMapping
  public SysUserEntity save(@RequestBody @Valid SysUserDto dto) {

    SysUserEntity sysUser = dto.convert(SysUserEntity.class);
    return userService.save(sysUser);
  }

  /**
   * 更新用户信息
   * @param id 用户ID
   * @param dto 表单数据
   * @return 更新后的用户对象
   */
  @PutMapping("{id}")
  public SysUserEntity update(@NotNull @PathVariable Long id, @RequestBody SysUserDto dto) {
    SysUserEntity sysUser = userService.findById(id);
    BeanUtils.copyProperties(dto, sysUser);

    return userService.update(sysUser);
  }
}
