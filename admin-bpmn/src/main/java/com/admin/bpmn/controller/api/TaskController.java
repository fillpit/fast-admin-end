package com.admin.bpmn.controller.api;

import com.admin.bpmn.flowable.handler.InstanceHandler;
import com.admin.bpmn.flowable.handler.TaskHandler;
import com.admin.bpmn.flowable.handler.TaskQueryHandler;
import com.admin.bpmn.model.TaskVO;
import com.admin.core.annotations.JsonParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.task.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程任务相关
 * @author fei
 * @date 2021/10/30 15:43
 */
@RestController
@RequestMapping("api/flow/task")
@Api(value = "Task", tags = {"流程任务"})
public class TaskController {

  protected static Logger logger = LoggerFactory.getLogger(InstanceHandler.class);
  @Autowired
  private TaskHandler taskHandler;
  @Autowired
  private TaskQueryHandler taskQueryHandler;


  @PostMapping(value = "execute")
  @ApiOperation(value = "执行任务", notes = "部门领导审批：{\"days\":\"6\",\"check\":\"1\",\"actorIds\":[\"wangwu\",\"lifei\"]}                                             \n" +
    "分管领导审批：{\"check\":\"1\",\"actorIds\":[\"hr_li\",\"hr_zhang\"]}", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
  public Map<String, TaskVO> executeTask(String taskId, @RequestBody Map<String, Object> variables) throws Exception {
    return taskHandler.complete(taskId, variables, false);
  }


  @PostMapping(value = "claim")
  @ApiOperation(value = "任务签收", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
  })
  public String claim(@JsonParam String taskId, @JsonParam String userId) throws Exception {
    taskHandler.claim(taskId, userId);
    return "任务签收成功";
  }


  @PostMapping(value = "unclaim")
  @ApiOperation(value = "任务反签收", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
  public String unclaim(String taskId) throws Exception {
    taskHandler.unclaim(taskId);
    return "任务反签收成功";
  }


  @PostMapping(value = "delegate")
  @ApiOperation(value = "任务委派", notes = "【act_ru_task】委派人：owner，被委派人：assignee，委派任务：delegateTask，任务回到委派人：resolveTask", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
  })
  public String delegate(String taskId, String userId) throws Exception {
    taskHandler.delegate(taskId, userId);
    return "任务委派成功";
  }


  @PostMapping(value = "resolve")
  @ApiOperation(value = "任务归还：被委派人完成任务之后，将任务归还委派人", notes = "", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),})
  public void reslove(String taskId) throws Exception {
    taskHandler.resolveTask(taskId);
  }


  @PostMapping(value = "assignee")
  @ApiOperation(value = "任务转办", notes = "【act_ru_task】设置assignee为转办人", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
  })
  public String assignee(String taskId, String userId) throws Exception {
    taskHandler.setAssignee(taskId, userId);
    return "任务转办成功";
  }


  @PostMapping(value = "comment/add")
  @ApiOperation(value = "添加批注信息", notes = "批注信息：act_hi_comment", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "instanceId", value = "实例ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "message", value = "批注内容", required = true, dataType = "String")
  })
  public Comment addComment(String taskId, String instanceId, String message) throws Exception {
    return taskHandler.addComment(taskId, instanceId, message);
  }
}
