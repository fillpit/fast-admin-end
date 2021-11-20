package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.entity.SysDeptEntity;
import com.kenfei.admin.modules.user.dto.SysDeptDto;
import com.kenfei.admin.modules.user.service.SysDeptService;
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
 * (SysDept)表控制层
 *
 * @author kenfei
 * @since 2021-08-16 16:23:38
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class SysDeptController extends AbstractController<SysDeptEntity, Long> {
    private final SysDeptService sysDeptService;

    @Autowired
    public SysDeptController(SysDeptService sysDeptService) {
        super(sysDeptService);
        this.sysDeptService = sysDeptService;
    }

   /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<SysDeptEntity> index(Long parentId, @PageableDefault Pageable pageable) {
    return sysDeptService.index(parentId, pageable);
  }

  /**
   * 获取数据列表
   * @param enabled 状态
   * @return 数据集合
   */
  @GetMapping("list")
  public List<SysDeptEntity> list(Boolean enabled, Long parentId) {
    return sysDeptService.list(enabled, parentId);
  }


  /**
   * 查询菜单:根据ID获取同级与上级数据（编辑时懒加载用于显示默认值）
   * @param id id
   * @return 菜单集合
   */
  @GetMapping("/superior")
  public List<SysDeptEntity> getSuperior(Long id) {
    if (!Objects.isNull(id)) {
      SysDeptEntity sysMenuEntity = sysDeptService.findById(id);
      List<SysDeptEntity> menus = sysDeptService.getSuperior(sysMenuEntity, new ArrayList<>());
      return sysDeptService.buildTree(menus);
    }
    return sysDeptService.findByParentId(null);
  }
   /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public SysDeptEntity save(@Valid @RequestBody SysDeptDto dto) {

    SysDeptEntity sysDept = dto.convert(SysDeptEntity.class);

    return sysDeptService.save(sysDept);
  }

   /**
   * 更新.
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public SysDeptEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysDeptDto dto) {
    SysDeptEntity sysDept = sysDeptService.findById(id);

    BeanUtils.copyProperties(dto, sysDept);

    return sysDeptService.update(sysDept);
  }

   /**
   * 删除
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    Set<SysDeptEntity> SysDeptSet = new HashSet<>();
    for (Long id : ids) {
       SysDeptEntity sysDept = sysDeptService.findById(id);
       SysDeptSet.add(sysDept);
    }
    sysDeptService.delete(SysDeptSet);
  }
}



