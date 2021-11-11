package com.kenfei.admin.bpmn.config.converter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.*;
import org.flowable.editor.language.json.converter.BaseBpmnJsonConverter;
import org.flowable.editor.language.json.converter.UserTaskJsonConverter;
import org.flowable.editor.language.json.converter.util.JsonConverterUtil;

import java.util.Map;


/**
 * @author fei
 * @date 2021/11/6 10:41
 */
public class BruceUserTaskJsonConverter extends UserTaskJsonConverter {
  public static final String ASSIGNEE_TYPE = "assigneeType";
  public static final String IDM_ASSIGNEE = "idmAssignee";
  public static final String IDM_CANDIDATE_GROUPS = "idmCandidateGroups";
  public static final String IDM_CANDIDATE_USERS = "idmCandidateUsers";
  public static final String IS_EDITDATA = "isEditdata";
  public static final String NODE_TYPE = "nodeType";
  public static final String NEXT_SEQUENCE_FLOW_LABEL = "nextSequenceFlow";
  public static final String NEXT_USER_LABEL = "nextUser";


  static void customFillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap, Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    fillJsonTypes(convertersToBpmnMap);
    fillBpmnTypes(convertersToJsonMap);
  }

  public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
    convertersToBpmnMap.put(STENCIL_TASK_USER, BruceUserTaskJsonConverter.class);
  }

  public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    convertersToJsonMap.put(UserTask.class, BruceUserTaskJsonConverter.class);
  }

  @Override
  protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
    super.convertElementToJson(propertiesNode, baseElement);
    if (baseElement instanceof UserTask){
      final String[] text = new String[8];
      baseElement.getExtensionElements().forEach((s, elements) -> elements.forEach(extensionElement -> {
        if (ASSIGNEE_TYPE.equals(extensionElement.getName())){
          text[0] = extensionElement.getElementText();
        }
        if (IDM_ASSIGNEE.equals(extensionElement.getName())){
          text[1] = extensionElement.getElementText();
        }
        if (IDM_CANDIDATE_GROUPS.equals(extensionElement.getName())){
          text[2] = extensionElement.getElementText();
        }
        if (IDM_CANDIDATE_USERS.equals(extensionElement.getName())){
          text[3] = extensionElement.getElementText();
        }
        if (IS_EDITDATA.equals(extensionElement.getName())){
          text[4] = extensionElement.getElementText();
        }
        if (NODE_TYPE.equals(extensionElement.getName())){
          text[5] = extensionElement.getElementText();
        }
        if (NEXT_SEQUENCE_FLOW_LABEL.equals(extensionElement.getName())) {
          text[6] = extensionElement.getElementText();
        }
        if (NEXT_USER_LABEL.equals(extensionElement.getName())) {
          text[7] = extensionElement.getElementText();
        }
      }));
      if (StringUtils.isNotBlank(text[0])){
        propertiesNode.put(ASSIGNEE_TYPE, text[0]);
      }
      if (StringUtils.isNotBlank(text[1])){
        propertiesNode.put(IDM_ASSIGNEE, text[1]);
      }
      if (StringUtils.isNotBlank(text[2])){
        propertiesNode.put(IDM_CANDIDATE_GROUPS, text[2]);
      }
      if (StringUtils.isNotBlank(text[3])){
        propertiesNode.put(IDM_CANDIDATE_USERS, text[3]);
      }
      if (StringUtils.isNotBlank(text[4])){
        propertiesNode.put(IS_EDITDATA, text[4]);
      }
      if (StringUtils.isNotBlank(text[5])){
        propertiesNode.put(NODE_TYPE, text[5]);
      }
      if (StringUtils.isNotBlank(text[6])) {
        propertiesNode.put(NEXT_SEQUENCE_FLOW_LABEL, text[6]);
      }

      if (StringUtils.isNotBlank(text[7])) {
        propertiesNode.put(NEXT_USER_LABEL, text[7]);
      }
    }
  }

  @Override
  protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
    FlowElement flowElement = super.convertJsonToElement(elementNode, modelNode, shapeMap);
    this.addExtansionPropertiesElement(flowElement, elementNode);
    return flowElement;
  }

  private void addExtansionPropertiesElement(FlowElement flowElement, JsonNode elementNode) {
    if (flowElement instanceof UserTask){
      UserTask userTask = (UserTask) flowElement;
      this.addExtansionPropertiesElement(elementNode, userTask, ASSIGNEE_TYPE);
      this.addExtansionPropertiesElement(elementNode, userTask, IDM_ASSIGNEE);
      this.addExtansionPropertiesElement(elementNode, userTask, IDM_CANDIDATE_GROUPS);
      this.addExtansionPropertiesElement(elementNode, userTask, IDM_CANDIDATE_USERS);
      this.addExtansionPropertiesElement(elementNode, userTask, IS_EDITDATA);
      this.addExtansionPropertiesElement(elementNode, userTask, NODE_TYPE);
      this.addExtansionPropertiesElement(elementNode, userTask, NEXT_SEQUENCE_FLOW_LABEL);
      this.addExtansionPropertiesElement(elementNode, userTask, NEXT_USER_LABEL);
    }
  }

  public void addExtansionPropertiesElement(JsonNode objectNode, Activity activity, String name) {
    JsonNode expansionNode = JsonConverterUtil.getProperty(name, objectNode);
    if (expansionNode instanceof TextNode){
      if (expansionNode != null && StringUtils.isNotBlank(expansionNode.asText())){
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setName(name);
        extensionElement.setNamespacePrefix(BpmnXMLConstants.FLOWABLE_EXTENSIONS_PREFIX);
        extensionElement.setNamespace(BpmnXMLConstants.FLOWABLE_EXTENSIONS_NAMESPACE);
        extensionElement.setElementText(expansionNode.asText());
        activity.addExtensionElement(extensionElement);
      }
    }
  }

//    @Override
//    protected void addExtensionElement(String name, String elementText, UserTask task) {
//        ExtensionElement extensionElement = new ExtensionElement();
//        extensionElement.setNamespace(NAMESPACE);
//        extensionElement.setNamespacePrefix("flowable");
//        extensionElement.setName(name);
//        extensionElement.setElementText(elementText);
//        task.addExtensionElement(extensionElement);
//    }

}
