package com.admin.bpmn.flowable.handler;

import com.admin.bpmn.flowable.ActProcess;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程定义
 * @author fei
 * @date 2021/10/30 14:17
 */
@Component
public class ProcessHandler implements ActProcess {

  protected static Logger logger = LoggerFactory.getLogger(ProcessHandler.class);

  @Autowired private RepositoryService repositoryService;

  /**
   * 解决问题：https://blog.csdn.net/weixin_43607664/article/details/88664777
   */
  public static final String BPMN_FILE_SUFFIX = ".bpmn";

  @Override
  public DeploymentBuilder createDeployment() {
    BpmnModel bpmnModel =  repositoryService.getBpmnModel("");
    Process a = bpmnModel.getProcess("");
//    a.get
    return repositoryService.createDeployment();
  }

  @Override
  public DeploymentQuery createDeploymentQuery() {
    return repositoryService.createDeploymentQuery();
  }

  @Override
  public ProcessDefinitionQuery createProcessDefinitionQuery() {
    return repositoryService.createProcessDefinitionQuery();
  }

  @Override
  public Deployment deploy(String bpmnFileUrl) {
    return createDeployment().addClasspathResource(bpmnFileUrl).deploy();
  }

  @Override
  public Deployment deploy(String name, String key, InputStream xmlInputStream, InputStream pngInputStream) {
    String xmlFileName = key + ".bpmn20.xml";
    String pngFileName = key + ".png";

    return repositoryService.createDeployment().
      addInputStream(xmlFileName, xmlInputStream)
      .addInputStream(pngFileName, pngInputStream)
      .name(name)
      .key(key)
      .deploy();
  }

  @Override
  public Deployment deploy(String url, String pngUrl) {

    return createDeployment().addClasspathResource(url).addClasspathResource(pngUrl).deploy();
  }

  @Override
  public Deployment deploy(String name, String tenantId, String category, ZipInputStream zipInputStream) {
    return createDeployment().addZipInputStream(zipInputStream)
      .name(name).category(category).tenantId(tenantId).deploy();
  }

  @Override
  public Deployment deployBpmnAndDrl(String url, String drlUrl) {
    return createDeployment().addClasspathResource(url).addClasspathResource(drlUrl).deploy();
  }

  @Override
  public Deployment deploy(String url, String name, String category) {
    return createDeployment().addClasspathResource(url).name(name).category(category).deploy();
  }

  @Override
  public Deployment deploy(String url, String pngUrl, String name, String category) {
    return createDeployment().addClasspathResource(url).addClasspathResource(pngUrl)
      .name(name).category(category).deploy();
  }

  @Override
  public boolean exist(String processDefinitionKey) {
    ProcessDefinitionQuery processDefinitionQuery
      = createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);
    long count = processDefinitionQuery.count();
    return count > 0;
  }

  @Override
  public Deployment deploy(String name, String tenantId, String category, InputStream in) {
    return createDeployment().addInputStream(name + BPMN_FILE_SUFFIX, in)
      .name(name)
      .tenantId(tenantId)
      .category(category)
      .deploy();

  }

  @Override
  public ProcessDefinition queryByProcessDefinitionKey(String processDefinitionKey) {
    return createProcessDefinitionQuery()
    .processDefinitionKey(processDefinitionKey)
    .active().singleResult();
  }

  @Override
  public Deployment deployName(String deploymentName) {
    List<Deployment> list = repositoryService
      .createDeploymentQuery()
      .deploymentName(deploymentName).list();
    Assert.notNull(list, "list must not be null");
    return list.get(0);
  }

  @Override
  public void addCandidateStarterUser(String processDefinitionKey, String userId) {
    repositoryService.addCandidateStarterUser(processDefinitionKey, userId);
  }

}
