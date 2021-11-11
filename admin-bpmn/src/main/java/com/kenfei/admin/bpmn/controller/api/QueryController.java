package com.kenfei.admin.bpmn.controller.api;

import cn.hutool.core.bean.BeanUtil;
import com.kenfei.admin.bpmn.flowable.handler.HistTaskQueryHandler;
import com.kenfei.admin.bpmn.flowable.handler.TaskHandler;
import com.kenfei.admin.bpmn.flowable.handler.TaskQueryHandler;
import com.kenfei.admin.bpmn.model.HistTaskVO;
import com.kenfei.admin.bpmn.model.TaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.HistoryService;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程任务相关
 * @author fei
 * @date 2021/10/30 15:36
 */
@RestController
@RequestMapping("api/flow/query")
@Api(value = "Query", tags = {"查询相关"})
public class QueryController {

  protected static Logger logger = LoggerFactory.getLogger(QueryController.class);
  @Autowired
  private TaskHandler taskHandler;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private TaskQueryHandler taskQueryHandler;
  @Autowired
  private HistTaskQueryHandler histTaskQueryHandler;

  @GetMapping(value = "task/taskId")
  @ApiOperation(value = "任务查询", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
  public TaskVO queryTask(String taskId) {
    Task task = taskQueryHandler.taskId(taskId);
    TaskVO taskVO = BeanUtil.copyProperties(task, TaskVO.class);
    return taskVO;
  }

  @GetMapping(value = "task/list/userId")
  @ApiOperation(value = "根据用户ID，查询该用户参与的活动任务列表", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")})
  public List<TaskVO> queryUserList(String userId) {
    List<Task> tasks = taskQueryHandler.taskCandidateOrAssigned(userId);
    List<TaskVO> list = BeanUtil.copyToList(tasks,TaskVO.class);
    return list;
  }


  @GetMapping(value = "comment")
  @ApiOperation(value = "查询批注信息", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")})
  public List<Comment> getTaskComments(String taskId) throws Exception {
    List<Comment> taskComments = taskHandler.getTaskComments(taskId);
    return taskComments;
  }


  @GetMapping(value = "task/list/instanceId")
  @ApiOperation(value = "查询任务列表（所有）", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String")})
  public List<HistTaskVO> listByInstanceId(String instanceId) {
    List<HistoricTaskInstance> list = histTaskQueryHandler.listByInstanceId(instanceId);
    List<HistTaskVO> copyList = BeanUtil.copyToList(list, HistTaskVO.class);
    return copyList;
  }

  @GetMapping(value = "task/pageList/instanceId")
  @ApiOperation(value = "分页查询任务列表（所有）", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "page", value = "页码", required = false, defaultValue = "1", dataType = "int"),
    @ApiImplicitParam(name = "step", value = "数量", required = false, defaultValue = "20", dataType = "int"),
  })
  public List<HistoricTaskInstance> pageListByInstanceId(String instanceId, Integer page, Integer step) {
    List<HistoricTaskInstance> list = histTaskQueryHandler.pageListByInstanceId(instanceId, page, step);
    return list;
  }


  @GetMapping(value = "/task/list/unclaim")
  @ApiOperation(value = "查询未签收任务列表", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
    @ApiImplicitParam(name = "page", value = "页码", required = false, defaultValue = "1", dataType = "int"),
    @ApiImplicitParam(name = "step", value = "数量", required = false, defaultValue = "20", dataType = "int")})
  public List<TaskVO> unclaim(String userId, Integer page, Integer step) {
    List<Task> taskList = taskQueryHandler.taskCandidateUser(userId, page, step);
    return BeanUtil.copyToList(taskList, TaskVO.class);
  }

  @GetMapping(value = "task/list/claimed")
  @ApiOperation(value = "查询已签收任务列表", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")})
  public List<HistoricTaskInstance> claimed(String userId) {
    return historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
  }

  @GetMapping(value = "task/active")
  @ApiOperation(value = "根据实例ID，查询当前活动任务", produces = "application/json")
  @ApiImplicitParams({@ApiImplicitParam(name = "instanceId", value = "实例ID", required = true, dataType = "String")})
  public HistoricTaskInstance query(String instanceId) throws Exception {
    //单实例任务--单个活动任务
    //方式一：需要处理懒加载问题
    //Task task = taskQueryHandler.processInstanceId(instanceId);
    //TaskVO taskVO = new TaskVO();
    //BeanUtils.copyProperties(task, taskVO);

    //方式二：正常使用，通过historyService查询当前活动任务，无需进行转换
    HistoricTaskInstance historicTaskInstance =
      historyService
        .createHistoricTaskInstanceQuery()
        .processInstanceId(instanceId)
        .unfinished().singleResult();

    //多实例并行任务--多个活动任务
    //List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).active().list();

    return historicTaskInstance;
  }


}
