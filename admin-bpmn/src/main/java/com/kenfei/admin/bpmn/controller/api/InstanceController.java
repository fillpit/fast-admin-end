package com.kenfei.admin.bpmn.controller.api;

import cn.hutool.core.bean.BeanUtil;
import com.kenfei.admin.bpmn.flowable.handler.InstanceHandler;
import com.kenfei.admin.bpmn.flowable.handler.TaskQueryHandler;
import com.kenfei.admin.bpmn.model.TaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程实例相关
 * @author fei
 * @date 2021/10/30 15:18
 */
@RestController
@RequestMapping("api/flow/instance")
@Api(value = "Instance", tags = {"流程实例"})
public class InstanceController {
  protected static Logger logger = LoggerFactory.getLogger(InstanceHandler.class);
  @Autowired
  private InstanceHandler instanceHandler;
  @Autowired
  private TaskQueryHandler taskQueryHandler;

  @PostMapping(value = "startByKey")
  @ApiOperation(value = "启动流程实例__通过流程定义name", notes = "实例启动成功，返回当前活动任务，如果部署流程模板时指定了tenantId，那么调用此方法也要指定", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "流程定义name", required = true, dataType = "String"),
    @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
  })
  public List<TaskVO> startByName(String name, String tenantId, @RequestBody Map<String, Object> variables) {
    ProcessInstance pi = instanceHandler.startProcessInstanceByKeyAndTenantId(name, tenantId, variables);
    List<Task> list = taskQueryHandler.processInstanceId4Multi(pi.getProcessInstanceId());
    List<TaskVO> taskVO = BeanUtil.copyToList(list, TaskVO.class);
    return taskVO;
  }


  @PostMapping(value = "startById")
  @ApiOperation(value = "启动流程实例--通过流程定义ID", notes = "实例启动成功，返回当前活动任务", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "流程定义ID", required = true, dataType = "String")})
  public TaskVO startById(String id, @RequestBody Map<String, Object> variables) {
    ProcessInstance pi = instanceHandler.startProcessInstanceById(id, variables);
    Task task = taskQueryHandler.processInstanceId(pi.getProcessInstanceId());
    TaskVO taskVO = BeanUtil.copyProperties(task, TaskVO.class);
    return taskVO;
  }


  @PostMapping(value = "startAndExecute")
  @ApiOperation(value = "启动流程实例并执行第一个流程任务", notes = "返回已执行、活动的任务map", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "流程定义KEY（模板ID）", required = true, dataType = "String"),
    @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "流程启动者ID", required = true, dataType = "String"),
  })
  public Map<String, TaskVO> startAndExecute(String name, String tenantId, String userId, @RequestBody Map<String, Object> variables) throws Exception {
    Map<String, TaskVO> map = instanceHandler.startInstanceAndExecuteFirstTask(name, tenantId, userId, variables);
    return map;
  }


  @PostMapping(value = "startExecuteAndSetActor")
  @ApiOperation(value = "启动流程实例并执行第一个流程任务,并设置下一任务处理人", notes = "{\"days\":\"6\",\"actorIds\":[\"zhangsan\",\"lisi\"]}", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "流程定义KEY（模板ID）", required = true, dataType = "String"),
    @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "流程启动者ID", required = true, dataType = "String"),
  })
  public Map<String, TaskVO> startAndExecuteAndSetActor(String name, String tenantId, String userId,
                                                 @RequestBody Map<String, Object> variables) {
    Map<String, TaskVO> map = instanceHandler.startInstanceAndExecuteFirstTask(name, tenantId, userId, variables);
    return map;
  }


  @GetMapping(value = "suspendById")
  @ApiOperation(value = "挂起流程实例", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String")})
  public String suspendById(String processInstanceId) throws Exception {
    instanceHandler.suspendProcessInstanceById(processInstanceId);
    return "流程实例挂起成功";
  }


  @GetMapping(value = "activateById")
  @ApiOperation(value = "激活流程实例", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String")})
  public String activateById(String processInstanceId) throws Exception {
    instanceHandler.activateProcessInstanceById(processInstanceId);
    return "激活流程实例";
  }


  @PostMapping(value = "addMultiInstanceExecution")
  @ApiOperation(value = "多实例加签", notes = "数据变化：act_ru_task、act_ru_identitylink各生成一条记录", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "activityDefId", value = "流程环节定义ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String")
  })
  public String addMultiInstanceExecution(String activityDefId, String instanceId, @RequestBody Map<String, Object> variables) {
    instanceHandler.addMultiInstanceExecution(activityDefId, instanceId, variables);
    return "加签成功";
  }

  @PostMapping(value = "deleteMultiInstanceExecution")
  @ApiOperation(value = "多实例减签", notes = "数据变化：act_ru_task减1、act_ru_identitylink不变", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "currentChildExecutionId", value = "子执行流ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "flag", value = "子执行流是否已执行", required = true, dataType = "boolean")
  })
  public String deleteMultiInstanceExecution(String currentChildExecutionId, boolean flag) {
    instanceHandler.deleteMultiInstanceExecution(currentChildExecutionId, flag);
    return "减签成功";
  }
}
