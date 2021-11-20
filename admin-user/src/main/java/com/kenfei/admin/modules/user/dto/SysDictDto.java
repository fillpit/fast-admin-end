package com.kenfei.admin.modules.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.modules.user.entity.SysDictEntity;
import lombok.Data;

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
