package com.admin.bpmn.controller.api;

import com.admin.bpmn.flowable.handler.TaskHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流程回滚
 * @author fei
 * @date 2021/10/30 13:42
 */
@RestController
@RequestMapping("api/flow/rollback")
public class BackController {
  @Autowired private TaskHandler taskHandler;

  @PostMapping(value = "withdraw")
  @ApiOperation(value = "任务撤回",notes = "注意：当前与目标定义Key为设计模板时任务对应的ID,而非数据主键ID",produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "currentTaskKey", value = "当前任务定义Key", required = true, dataType = "String"),
    @ApiImplicitParam(name = "targetTaskKey", value = "目标任务定义Key", required = true, dataType = "String")
  })
  public String withdraw(String processInstanceId, String currentTaskKey, String targetTaskKey) {
    taskHandler.withdraw(processInstanceId, currentTaskKey, targetTaskKey);
    return "任务撤回成功";
  }
}
