package com.kenfei.admin.user.service;

import com.kenfei.admin.core.common.base.InterfaceService;
import com.kenfei.admin.user.entity.SysUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 系统用户业务
 * @author fei
 * @date 2017/9/19
 */
public interface SysUserService extends InterfaceService<SysUserEntity, Long> {

  /**
   * 检查用户名是否存在
   *
   * @param name 用户名
   * @return false：存在，true：不存在
   */
  boolean checkUserName(String name);

  /**
   * 获取用户根据用户名称
   *
   * @param userName 用户名称
   * @return 用户对象
   */
  SysUserEntity findByUserName(String userName);

  /**
   * 获取首页的数据
   * @param userName 用户名
   * @param deptId 部门ID
   * @param pageable 分页参数
   * @return /
   */
  Page<SysUserEntity> index(String userName, Long deptId, Pageable pageable);

}
