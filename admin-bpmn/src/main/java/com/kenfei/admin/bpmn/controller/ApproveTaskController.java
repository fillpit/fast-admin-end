package com.kenfei.admin.bpmn.controller;

import com.kenfei.admin.bpmn.dto.StartTaskDto;
import com.kenfei.admin.bpmn.flowable.ActInstanceQuery;
import com.kenfei.admin.bpmn.service.ImageService;
import com.kenfei.admin.core.common.base.AbstractController;
import com.kenfei.admin.bpmn.entity.ApproveTaskEntity;
import com.kenfei.admin.bpmn.dto.ApproveTaskDto;
import com.kenfei.admin.bpmn.service.ApproveTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
/**
 * 激活的任务(ApproveTask)表控制层
 *
 * @author kenfei
 * @since 2021-11-01 13:58:32
 */
@Slf4j
@RestController
@RequestMapping("flow/task")
public class ApproveTaskController extends AbstractController<ApproveTaskEntity, Long> {
  private static final ExampleMatcher EXAMPLE_MATCHER;

  @Autowired private ActInstanceQuery actInstanceQuery;
  @Autowired private ImageService imageService;

  static {
    EXAMPLE_MATCHER =
        ExampleMatcher.matchingAll();
            //.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withIgnorePaths("permissionIds");
  }

    private final ApproveTaskService approveTaskService;

    @Autowired
    public ApproveTaskController(ApproveTaskService approveTaskService) {
        super(approveTaskService);
        this.approveTaskService = approveTaskService;
    }

   /**
   * 页面表格获取数据.
   *
   * @param pageable 分页参数
   * @return 数据分页
   */
  @GetMapping("index")
  public Page<ApproveTaskEntity> index(@PageableDefault Pageable pageable) {
    return approveTaskService.index(pageable);
  }

   /**
   * 获取数据列表
   * @return 数据集合
   */
  @GetMapping(value = "list")
  public List<ApproveTaskEntity> index() {
    return approveTaskService.findAll();
  }


  /**
   * 获取发起人的审批列表
   * @param pageable 分页
   * @return /
   */
  @GetMapping("originator")
  public Page<ApproveTaskEntity> findOriginator(@PageableDefault Pageable pageable) {
    return approveTaskService.findOriginator(pageable);
  }

   /**
   * 启动一个流程.
   *
   * @param dto 表单
   * @return 保存后的实体对象
   */
  @PostMapping("start")
  public ApproveTaskEntity start(@Valid @RequestBody StartTaskDto dto) throws Exception {

    return approveTaskService.start(
        dto.getProcessDefinitionKey(), dto.getTaskName(), dto.getFormData());
  }

  /**
   * 执行任务
   * @param dto 表单
   * @return /
   * @throws Exception
   */
  @PostMapping("execute")
  public String execute(@Valid @RequestBody ApproveTaskDto dto) throws Exception {
    approveTaskService.execute(
        dto.getId(), dto.getNodeId(), dto.getTaskId(), dto.getFormData(), dto.getMessage());
    return "执行成功";
  }


  @GetMapping("proc_image")
  public void getProcessImage(String processDefinitionKey, HttpServletResponse response) throws Exception{
    ProcessInstance pi = actInstanceQuery.processInstanceBusinessKey(processDefinitionKey);
    byte[] bytes = imageService.generateImageByProcInstId(pi.getId());
    response.setContentType("image/png");
    ServletOutputStream outputStream = response.getOutputStream();
    FileCopyUtils.copy(bytes,outputStream);
    System.out.println(new String(bytes));
  }

  /**
  * 删除
  * @param ids IDs
  */
  @DeleteMapping
  public void delete(@NotEmpty @RequestBody List<Long> ids) {
    List<ApproveTaskEntity> approveTaskList = new LinkedList<>();
    for (Long id : ids) {
       ApproveTaskEntity approveTask = approveTaskService.findById(id);
       approveTaskList.add(approveTask);
    }
    approveTaskService.delete(approveTaskList);
  }
}



