package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.entity.SysLoginInfoEntity;
import com.kenfei.admin.modules.user.service.SysLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登陆日志(SysLoginInfo)表控制层
 *
 * @author fei
 * @since 2020-10-22 15:25:20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("logs")
public class   SysLoginInfoController extends AbstractController<SysLoginInfoEntity, Long> {
  /** 服务对象 */
  private final SysLoginInfoService sysLoginInfoService;

  public SysLoginInfoController(SysLoginInfoService sysLoginInfoService) {
    super(sysLoginInfoService);
    this.sysLoginInfoService = sysLoginInfoService;
  }

  /**
   * 获取首页数据（分页）
   *
   * @param entity 实体对象
   * @param pageable 分页参数
   * @return 用户集合
   */
  @GetMapping(
      value = "index",
      params = {"page"})
  public Page<SysLoginInfoEntity> findAll(
      SysLoginInfoEntity entity, @PageableDefault Pageable pageable) {
    return this.sysLoginInfoService.findAll(Example.of(entity, getMatcher()), pageable);
  }

}
