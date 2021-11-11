package com.kenfei.admin.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 数据字典(SysDict)实体类
 *
 * @author kenfei
 * @since 2021-08-16 22:18:11
 */
@Entity
@Table(name = "sys_dict")
public class SysDictEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -44651210198746181L;
  /** 主键列*/
  private Long id;
  /** 字典值 */
  private List<SysDictDetailEntity> dictDetails;

  /** 字典名称 */
  private String name;

  /** 描述 */
  private String description;

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
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonIgnore
  @OneToMany(mappedBy = "dict",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
  public List<SysDictDetailEntity> getDictDetails() {
    return dictDetails;
  }

  public void setDictDetails(List<SysDictDetailEntity> dictDetails) {
    this.dictDetails = dictDetails;
  }
}
