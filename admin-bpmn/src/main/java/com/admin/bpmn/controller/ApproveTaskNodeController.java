package com.admin.bpmn.controller;

import com.admin.bpmn.entity.ApproveTaskEntity;
import com.admin.core.basic.AbstractController;
import com.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.admin.bpmn.dto.ApproveTaskNodeDto;
import com.admin.bpmn.service.ApproveTaskNodeService;
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
 * 执行的流程节点(ApproveTaskNode)表控制层
 *
 * @author kenfei
 * @since 2021-11-03 13:56:15
 */
@Slf4j
@RestController
@RequestMapping("/flow/task/node")
public class ApproveTaskNodeController extends AbstractController<ApproveTaskNodeEntity, Long> {
  private static final ExampleMatcher EXAMPLE_MATCHER;

  static {
    EXAMPLE_MATCHER =
        ExampleMatcher.matchingAll();
            //.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withMatcher("departmentCode", ExampleMatcher.GenericPropertyMatchers.contains())
            //.withIgnorePaths("permissionIds");
  }

    private final ApproveTaskNodeService approveTaskNodeService;

    @Autowired
    public ApproveTaskNodeController(ApproveTaskNodeService approveTaskNodeService) {
        super(approveTaskNodeService);
        this.approveTaskNodeService = approveTaskNodeService;
    }

  /**
   * 获取todo 列表
   * @return todo 列表
   */
  @GetMapping(value = "todo_list")
  public Page<ApproveTaskNodeEntity> todoList(@PageableDefault Pageable pageable) {
    return approveTaskNodeService.todoList(pageable);
  }

  /**
   * 统计 todo 列表
   * @return todo 数量
   */
  @GetMapping(value = "count_todo_list")
  public Long countTodoList() {
    return approveTaskNodeService.countTodoList();
  }

  /**
   * 获取已办列表
   * @param pageable 分页
   * @return /
   */
  @GetMapping("done")
  public Page<ApproveTaskNodeEntity> done(@PageableDefault Pageable pageable) {
    return approveTaskNodeService.done(pageable);
  }

}



