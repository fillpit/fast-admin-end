package com.kenfei.admin.modules.user.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.modules.user.entity.SysOrgEntity;
import com.kenfei.admin.modules.user.dto.SysOrgDto;
import com.kenfei.admin.modules.user.service.SysOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * (SysOrg)表控制层
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
@Slf4j
@RestController
@RequestMapping("sys/org")
public class SysOrgController extends AbstractController<SysOrgEntity, Long> {
  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER =
        ExampleMatcher.matchingAll();
            //.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withIgnorePaths("permissionIds");
  }

    private final SysOrgService sysOrgService;

    @Autowired
    public SysOrgController(SysOrgService sysOrgService) {
        super(sysOrgService);
        this.sysOrgService = sysOrgService;
    }

  /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<SysOrgEntity> index(Long parentId, @PageableDefault Pageable pageable) {
    return sysOrgService.index(parentId, pageable);
  }

  /**
   * 获取数据列表
   * @param enabled 状态
   * @return 数据集合
   */
  @GetMapping("list")
  public List<SysOrgEntity> list(Boolean enabled, Long parentId) {
    return sysOrgService.list(enabled, parentId);
  }


  /**
   * 查询菜单:根据ID获取同级与上级数据（编辑时懒加载用于显示默认值）
   * @param id id
   * @return 菜单集合
   */
  @GetMapping("/superior")
  public List<SysOrgEntity> getSuperior(Long id) {
    if (!Objects.isNull(id)) {
      SysOrgEntity sysMenuEntity = sysOrgService.findById(id);
      List<SysOrgEntity> menus = sysOrgService.getSuperior(sysMenuEntity, new ArrayList<>());
      return sysOrgService.buildTree(menus);
    }
    return sysOrgService.findByParentId(null);
  }
  /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public SysOrgEntity save(@Valid @RequestBody SysOrgDto dto) {

    SysOrgEntity sysDept = dto.convert(SysOrgEntity.class);

    return sysOrgService.save(sysDept);
  }

  /**
   * 更新.
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public SysOrgEntity update(@NotNull @PathVariable Long id, @Valid @RequestBody SysOrgDto dto) {
    SysOrgEntity sysDept = sysOrgService.findById(id);

    BeanUtils.copyProperties(dto, sysDept);

    return sysOrgService.update(sysDept);
  }

  /**
   * 删除
   * @param ids IDs
   */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    Set<SysOrgEntity> sysDeptSet = new HashSet<>();
    for (Long id : ids) {
      SysOrgEntity sysDept = sysOrgService.findById(id);
      sysDeptSet.add(sysDept);
    }
    sysOrgService.delete(sysDeptSet);
  }
}



