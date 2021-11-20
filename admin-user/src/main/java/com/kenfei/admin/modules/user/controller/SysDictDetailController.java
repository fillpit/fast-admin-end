package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.entity.SysDictDetailEntity;
import com.kenfei.admin.modules.user.dto.SysDictDetailDto;
import com.kenfei.admin.modules.user.service.SysDictDetailService;
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
/**
 * 数据字典详情(SysDictDetail)表控制层
 *
 * @author kenfei
 * @since 2021-08-16 22:39:33
 */
@Slf4j
@RestController
@RequestMapping("dict_detail")
public class SysDictDetailController extends AbstractController<SysDictDetailEntity, Long> {
  private final SysDictDetailService sysDictDetailService;

  @Autowired
  public SysDictDetailController(SysDictDetailService sysDictDetailService) {
    super(sysDictDetailService);
    this.sysDictDetailService = sysDictDetailService;
  }

  /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<SysDictDetailEntity> index(String dictName, @PageableDefault Pageable pageable) {
    return sysDictDetailService.index(dictName, pageable);
  }

  /**
   * 获取数据列表.
   *
   * @return 数据分页
   */
  @GetMapping("list")
  public List<SysDictDetailEntity> list(String dictName) {
    return sysDictDetailService.list(dictName);
  }

  /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public SysDictDetailEntity save(@Valid @RequestBody SysDictDetailDto dto) {

    SysDictDetailEntity sysDictDetail = dto.convert(SysDictDetailEntity.class);

    return sysDictDetailService.save(sysDictDetail);
  }

  /**
   * 更新.
   *
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public SysDictDetailEntity update(
      @NotNull @PathVariable Long id, @Valid @RequestBody SysDictDetailDto dto) {
    SysDictDetailEntity sysDictDetail = sysDictDetailService.findById(id);

    BeanUtils.copyProperties(dto, sysDictDetail);

    return sysDictDetailService.update(sysDictDetail);
  }

  /**
   * 删除
   *
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    List<SysDictDetailEntity> sysDictDetailSet = new LinkedList<>();
    for (Long id : ids) {
      SysDictDetailEntity sysDictDetail = sysDictDetailService.findById(id);
      sysDictDetailSet.add(sysDictDetail);
    }
    sysDictDetailService.delete(sysDictDetailSet);
  }
}
