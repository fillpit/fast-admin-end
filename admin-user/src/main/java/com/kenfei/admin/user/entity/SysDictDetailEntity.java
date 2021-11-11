package com.kenfei.admin.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 数据字典详情(SysDictDetail)实体类
 *
 * @author kenfei
 * @since 2021-08-16 22:18:42
 */
@Entity
@Table(name = "sys_dict_detail")
public class SysDictDetailEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = 381053518398846207L;

  /** 主键列*/
  private Long id;
  /** 字典 */
  private SysDictEntity dict;
  /** 字典ID */
  private Long dictId;

  /** 字典标签 */
  private String label;

  /** 字典值 */
  private String value;

  /** 排序 */
  private Integer dictSort;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "label")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Basic
  @Column(name = "value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Basic
  @Column(name = "dict_sort")
  public Integer getDictSort() {
    return dictSort;
  }

  public void setDictSort(Integer dictSort) {
    this.dictSort = dictSort;
  }

  @JsonIgnore
  @JoinColumn(name = "dict_id", insertable = false, updatable = false)
  @ManyToOne(fetch=FetchType.LAZY)
  public SysDictEntity getDict() {
    return dict;
  }

  public void setDict(SysDictEntity dict) {
    this.dict = dict;
  }

  @Basic
  @Column(name = "dict_id")
  public Long getDictId() {
    return dictId;
  }

  public void setDictId(Long dictId) {
    this.dictId = dictId;
  }
}
