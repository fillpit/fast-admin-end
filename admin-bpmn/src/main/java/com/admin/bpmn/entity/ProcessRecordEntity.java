package com.admin.bpmn.entity;

import com.admin.core.basic.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (TbProcessRecord)实体类
 *
 * @author kenfei
 * @since 2021-10-28 16:34:34
 */
@Entity
@Table(name = "tb_process_record")
public class ProcessRecordEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -94975191415514695L;

  private Integer id;

  /** 流程ID */
  private String procId;

  /** 流程名称 */
  private String name;

  /** 流程xml */
  private String xmlStr;

  /** svg码 */
  private String svgStr;

  /** 状态，0未发布，1已发布 */
  private Boolean status;

  /** 描述 */
  private String note;

  /** 删除状态 */
  private Boolean delFlag;

  /** 表单组件 */
  private String form;
  /** 版本号 */
  private Integer version;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "proc_id")
  public String getProcId() {
    return procId;
  }

  public void setProcId(String procId) {
    this.procId = procId;
  }

  @Basic
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "xml_str")
  public String getXmlStr() {
    return xmlStr;
  }

  public void setXmlStr(String xmlStr) {
    this.xmlStr = xmlStr;
  }

  @Basic
  @Column(name = "svg_str")
  public String getSvgStr() {
    return svgStr;
  }

  public void setSvgStr(String svgStr) {
    this.svgStr = svgStr;
  }

  @Basic
  @Column(name = "status")
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Basic
  @Column(name = "note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Basic
  @Column(name = "del_flag")
  public Boolean getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(Boolean delFlag) {
    this.delFlag = delFlag;
  }

  @Basic
  @Column(name = "form")
  public String getForm() {
    return form;
  }

  public void setForm(String form) {
    this.form = form;
  }

  @Basic
  @Column(name = "version")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
