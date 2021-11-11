package com.admin.bpmn.controller.api;

import cn.hutool.core.bean.BeanUtil;
import com.admin.bpmn.flowable.handler.ProcessHandler;
import com.admin.bpmn.model.DeploymentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;

/**
 * 部署流程定义
 * @author fei
 * @date 2021/10/30 15:32
 */
@RestController
@RequestMapping("api/flow/process")
@Api(value = "Process", tags = {"流程模板"}, description = "注意：如果部署流程定义时指定了tenantId,那么在启动流程实例的时候，也需要传递tenantId，否则报错")
public class ProcessController {
  @Autowired private ProcessHandler processHandler;

  @PostMapping(value = "deploy/files/zip", headers = "content-type=multipart/form-data")
  @ApiOperation(value = "部署压缩包形式的模板(.zip .bar)，主子流程定义部署", notes = "可用于一次性部署多个资源文件（.bpmn .drl .form等）")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "主模板名称（模板ID）", required = true, dataType = "String"),
    @ApiImplicitParam(name = "category", value = "模板类别", required = false, dataType = "String"),
    @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
    @ApiImplicitParam(name = "file", value = "模板文件", required = true, dataType = "__file")
  })
  public DeploymentVO deployByZip(String name, String category, String tenantId, MultipartFile file) {
    Deployment deployment = null;
    try (ZipInputStream zipIn = new ZipInputStream(file.getInputStream(), StandardCharsets.UTF_8)) {
      deployment = processHandler.deploy(name, category, tenantId, zipIn);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //忽略二进制文件（模板文件、模板图片）返回
    DeploymentVO deploymentVO =
      BeanUtil.copyProperties(deployment, DeploymentVO.class, "resources");
    return deploymentVO;
  }


  /**
   * 部署流程定义
   */
  @ApiOperation(value = "部署流程模板文件", notes = "模板与工作流微服务解耦，模板文件可来自网络中某个位置，也可以来自业务项目。" +
    "tenantId用于记录流程定义归属于哪个业务系统，实际使用中，可以为每个系统设置一个固定标识")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "模板名称（模板ID）", required = true, dataType = "String"),
    @ApiImplicitParam(name = "category", value = "模板类别", required = false, dataType = "String"),
    @ApiImplicitParam(name = "tenantId", value = "系统标识", required = false, dataType = "String"),
    @ApiImplicitParam(name = "file", value = "模板文件", required = true, dataType = "__file")
  })
  @PostMapping(value = "deploy/file", headers = "content-type=multipart/form-data")
  public DeploymentVO deployByInputStream(String name, String tenantId, String category, MultipartFile file) {
    Deployment deploy = null;
    if (processHandler.exist(name)) {
      deploy = processHandler.deployName(name);
    } else {
      try (InputStream in = file.getInputStream()) {
        deploy = processHandler.deploy(name, tenantId, category, in);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //忽略二进制文件（模板文件、模板图片）返回
    DeploymentVO deploymentVO =
      BeanUtil.copyProperties(deploy, DeploymentVO.class, "resources");
    return deploymentVO;
  }
}
