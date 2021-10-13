package com.admin.user.controller;

import com.admin.core.basic.AbstractController;
import com.admin.user.entity.SysDeptEntity;
import com.admin.user.entity.SysDictEntity;
import com.admin.user.dto.SysDictDto;
import com.admin.user.service.SysDictService;
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
 * 数据字典(SysDict)表控制层
 *
 * @author kenfei
 * @since 2021-08-16 22:39:33
 */
@Slf4j
@RestController
@RequestMapping("dict")
public class SysDictController extends AbstractController<SysDictEntity, Long> {
  private final SysDictService sysDictService;

  @Autowired
  public SysDictController(SysDictService sysDictService) {
    super(sysDictService);
    this.sysDictService = sysDictService;
  }

  /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<SysDictEntity> index(@PageableDefault Pageable pageable) {
    return sysDictService.index(pageable);
  }


  /**
   * 获取数据列表
   * @return 数据集合
   */
  @GetMapping("list")
  public List<SysDictEntity> list() {
    return sysDictService.findAll();
  }

  /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public SysDictEntity save(@Valid @RequestBody SysDictDto dto) {

    SysDictEntity sysDict = dto.convert(SysDictEntity.class);

    return sysDictService.save(sysDict);
  }

  /**
   * 更新.
   *
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public SysDictEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysDictDto dto) {
    SysDictEntity sysDict = sysDictService.findById(id);

    BeanUtils.copyProperties(dto, sysDict);

    return sysDictService.update(sysDict);
  }

  /**
   * 删除
   *
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    List<SysDictEntity> sysDictSet = new LinkedList<>();
    for (Long id : ids) {
      SysDictEntity sysDict = sysDictService.findById(id);
      sysDictSet.add(sysDict);
    }
    sysDictService.delete(sysDictSet);
  }
}
