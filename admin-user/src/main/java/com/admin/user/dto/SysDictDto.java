package com.admin.user.dto;

import com.admin.core.basic.DtoConvert;
import com.admin.user.entity.SysDictEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 数据字典(SysDict)表单类
 *
 * @author kenfei
 * @since 2021-08-16 22:18:10
 */
@Data
public class SysDictDto implements DtoConvert<SysDictEntity> {

  /** ID */
  private Long id;

  /** 字典名称 */
  private String name;

  /** 描述 */
  private String description;
}
