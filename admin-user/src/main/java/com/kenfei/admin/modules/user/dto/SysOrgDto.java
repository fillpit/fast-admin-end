package com.kenfei.admin.modules.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.modules.user.entity.SysOrgEntity;
import lombok.Data;

/**
 * (SysOrg)表单类
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
@Data
public class SysOrgDto implements DtoConvert<SysOrgEntity> {


    private Long id;

    /** 排序 */

    private Integer deptSort;

    /** 状态 */

    private Boolean enabled;

    /** 编码 */

    private String code;

    /** 全名称 */

    private String name;

    /** 简称 */

    private String shortName;

    /** 描述 */

    private String descr;

    /** 父ID */

    private Long parentId;
}


