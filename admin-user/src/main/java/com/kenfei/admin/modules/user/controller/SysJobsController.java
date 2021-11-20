package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.entity.SysJobsEntity;
import com.kenfei.admin.modules.user.dto.SysJobsDto;
import com.kenfei.admin.modules.user.service.SysJobsService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;
/**
 * (SysJob) 岗位 表控制层
 *
 * @author kenfei
 * @since 2021-08-17 20:08:08
 */
@Slf4j
@RestController
@RequestMapping("jobs")
public class SysJobsController extends AbstractController<SysJobsEntity, Long> {
  private final SysJobsService sysJobsService;
  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER = ExampleMatcher
      .matchingAll()
      .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
      ;
  }

  @Autowired
  public SysJobsController(SysJobsService sysJobsService) {
    super(sysJobsService);
    this.sysJobsService = sysJobsService;
  }

  /**
   * 页面表格获取数据.
   *
   * @param enabled 数据状态
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<SysJobsEntity> index(String name, Boolean enabled, @PageableDefault Pageable pageable) {
    return sysJobsService.index(name, enabled, pageable);
  }

  /**
   * 获取数据列表
   * @return 岗位集合
   */
  @GetMapping(value = "list")
  public List<SysJobsEntity> index(@RequestParam(required = false) String name) {
    SysJobsEntity entity = new SysJobsEntity();
    entity.setName(name);
    entity.setEnabled(true);

    return sysJobsService.findAll(Example.of(entity, EXAMPLE_MATCHER));
  }

  /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public SysJobsEntity save(@Valid @RequestBody SysJobsDto dto) {

    SysJobsEntity sysJob = dto.convert(SysJobsEntity.class);

    return sysJobsService.save(sysJob);
  }

  /**
   * 更新.
   *
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public SysJobsEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysJobsDto dto) {
    SysJobsEntity sysJob = sysJobsService.findById(id);

    BeanUtils.copyProperties(dto, sysJob);

    return sysJobsService.update(sysJob);
  }

  /**
   * 删除
   *
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    List<SysJobsEntity> sysJobList = new LinkedList<>();
    for (Long id : ids) {
      SysJobsEntity sysJob = sysJobsService.findById(id);
      sysJobList.add(sysJob);
    }
    sysJobsService.delete(sysJobList);
  }
}
