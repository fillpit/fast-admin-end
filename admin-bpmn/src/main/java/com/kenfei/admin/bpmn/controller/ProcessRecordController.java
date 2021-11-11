package com.kenfei.admin.bpmn.controller;

import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.bpmn.entity.ProcessRecordEntity;
import com.kenfei.admin.bpmn.dto.ProcessRecordDto;
import com.kenfei.admin.bpmn.service.ProcessRecordService;
import com.kenfei.admin.core.common.exception.AppException;
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
 * (TbProcessRecord)表控制层
 *
 * @author kenfei
 * @since 2021-10-28 16:34:39
 */
@Slf4j
@RestController
@RequestMapping("process")
public class ProcessRecordController extends AbstractController<ProcessRecordEntity, Integer> {
  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER =
        ExampleMatcher.matchingAll();
            //.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withIgnorePaths("permissionIds");
  }

    private final ProcessRecordService tbProcessRecordService;

    @Autowired
    public ProcessRecordController(ProcessRecordService tbProcessRecordService) {
        super(tbProcessRecordService);
        this.tbProcessRecordService = tbProcessRecordService;
    }


  /**
   * 流程发布
   *
   * @param id 流程ID
   * @return
   */
  @PutMapping("/{id}/deploy")
  public ProcessRecordEntity deploy(@PathVariable("id") @NotNull Integer id) {
    return tbProcessRecordService.deploy(id);
  }

  /**
   * 流程发布
   *
   * @param dto 流程ID
   * @return
   */
  @PutMapping("deploy")
  public ProcessRecordEntity saveAndDeploy(@RequestBody @Valid ProcessRecordDto dto) {
    ProcessRecordEntity entity = dto.convert(ProcessRecordEntity.class);
    entity = tbProcessRecordService.save(entity);

    try{
      entity = tbProcessRecordService.deploy(entity.getId());
    } catch (Exception e){
      log.error("流程部署失败, 但数据已暂存，你可以稍后手动部署流程");
      throw new AppException("流程部署失败, 但数据已暂存，你可以稍后手动部署流程", e);
    }

    return entity;
  }


   /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<ProcessRecordEntity> index(@PageableDefault Pageable pageable) {
    return tbProcessRecordService.index(pageable);
  }

   /**
   * 获取数据列表
   * @return 数据集合
   */
  @GetMapping(value = "list")
  public List<ProcessRecordEntity> index() {
    return tbProcessRecordService.findAll();
  }

   /**
   * 保存.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping
  public ProcessRecordEntity save(@Valid @RequestBody ProcessRecordDto dto) {

    ProcessRecordEntity tbProcessRecord = dto.convert(ProcessRecordEntity.class);

    return tbProcessRecordService.save(tbProcessRecord);
  }

   /**
   * 更新.
   * @param id ID
   * @param dto 表单信息
   * @return 更新后的对象
   */
  @PutMapping("{id}")
  public ProcessRecordEntity update(@NotNull @PathVariable Integer id, @Valid @RequestBody ProcessRecordDto dto) {
    ProcessRecordEntity tbProcessRecord = tbProcessRecordService.findById(id);

    BeanUtils.copyProperties(dto, tbProcessRecord);

    return tbProcessRecordService.update(tbProcessRecord);
  }

  /**
  * 删除
  * @param ids IDs
  */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Integer> ids) {
    List<ProcessRecordEntity> tbProcessRecordList = new LinkedList<>();
    for (Integer id : ids) {
       ProcessRecordEntity tbProcessRecord = tbProcessRecordService.findById(id);
       tbProcessRecordList.add(tbProcessRecord);
    }
    tbProcessRecordService.delete(tbProcessRecordList);
  }
}



