package com.admin.user.dto;

import java.util.Date;
import com.admin.core.basic.DtoConvert;
import com.admin.user.entity.SysJobsEntity;
import lombok.Data;

/**
 * (SysJob)表单类
 *
 * @author kenfei
 * @since 2021-08-17 20:08:07
 */
@Data
public class SysJobsDto implements DtoConvert<SysJobsEntity> {

  private Long id;

  private Date createDate;

  private String createUser;

  private Date updateDate;

  private String updateUser;

  private Boolean enabled;

  private Long jobSort;

  private String name;
}
