package com.admin.bpmn.controller.api;

import com.admin.bpmn.flowable.handler.TaskHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author fei
 * @date 2021/10/30 15:47
 */
@ApiIgnore
@RestController
@RequestMapping("api/flow/user")
@Api(value = "User", tags = {"任务处理人"})
public class UserController {

  protected static Logger logger = LoggerFactory.getLogger(UserController.class);
  @Autowired
  private TaskHandler taskHandler;

  /**
   * 执行流程任务
   */
  @GetMapping(value = "transferTask")
  @ApiOperation(value = "任务移交", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "userId", value = "用户ID（接受移交的用户）", required = true, dataType = "String")
  })
  public void transferTask(String taskId, String userId) {
    taskHandler.setAssignee(taskId,userId);
  }
}
