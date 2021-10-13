package com.admin.user.repository;

import com.admin.core.repository.BaseRepository;
import com.admin.user.entity.SysUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户表
 * @author fei
 * @date 2017/9/19
 */
public interface SysUserRepository extends BaseRepository<SysUserEntity, Long> {

  /**
   * 获取一个用户
   *
   * @param userName 用户名
   * @return 用户对象
   */
  SysUserEntity findByUserName(String userName);

  /**
   * 获取用户对象
   * @param phone 手机号
   * @return /
   */
  SysUserEntity findByPhone(String phone);

  /**
   * 获取用户对象
   * @param email email
   * @return /
   */
  SysUserEntity findByEmail(String email);

  /**
   * 检查用户名是否存在
   * @param userName 用户名
   * @return true: 已存在, false: 不存在
   */
  boolean existsByUserName(String userName);

  /**
   * 检查手机号是否存在
   * @param phone 手机号
   * @return true: 已存在, false: 不存在
   */
  boolean existsByPhone(String phone);

  /**
   * 检查邮箱是否存在
   * @param email 邮箱
   * @return true: 已存在, false: 不存在
   */
  boolean existsByEmail(String email);
}
