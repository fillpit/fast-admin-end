package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.core.exception.AppException;
import com.admin.user.entity.SysDeptEntity;
import com.admin.user.entity.SysUserEntity;
import com.admin.user.repository.SysUserRepository;
import com.admin.user.service.SysDeptService;
import com.admin.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 用户业务
 *
 * @author fei
 * @date 2017/9/19
 */
@Slf4j
@Service
@Validated
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends AbstractServiceImpl<SysUserEntity, Long> implements SysUserService {

  private final SysUserRepository userRepository;
  private final SysDeptService sysDeptService;

  @Autowired
  public SysUserServiceImpl(SysUserRepository userRepository,
                            SysDeptService sysDeptService) {
    super(userRepository);
    this.userRepository = userRepository;
    this.sysDeptService = sysDeptService;
  }

  @Override
  public SysUserEntity save(@Valid SysUserEntity entity) {
    // 加密用户密码
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encode = encoder.encode(entity.getPassword());
    entity.setPassword(encode);

    // 检查用户名是否存在
    boolean exist = userRepository.existsByUserName(entity.getUserName());
    Assert.isTrue(!exist, "用户已存在");

    // 检查手机号
    boolean existPhone = userRepository.existsByPhone(entity.getPhone());
    Assert.isTrue(!existPhone, "手机号已存在");

    // 检查邮箱
    boolean existEmail = userRepository.existsByEmail(entity.getEmail());
    Assert.isTrue(!existEmail, "邮箱已存在");

    return userRepository.save(entity);
  }

  @Override
  public SysUserEntity update(@Valid SysUserEntity entity) {
    SysUserEntity user1 = userRepository.findByPhone(entity.getPhone());
    if (user1 != null && !Objects.equals(entity.getId(), user1.getId())) {
      throw new AppException("手机号重复");
    }
    user1 = userRepository.findByEmail(entity.getEmail());
    if (user1 != null && !Objects.equals(entity.getId(), user1.getId())) {
      throw new AppException("Email 重复");
    }

    return super.update(entity);
  }

  @Override
  public boolean checkUserName(String name) {
    return userRepository.existsByUserName(name);
  }

  @Override
  public SysUserEntity findByUserName(String userName) {
    return userRepository.findByUserName(userName);
  }

  @Override
  public Page<SysUserEntity> index(String userName, Long deptId, Pageable pageable) {
    Long[] deptIds = null;
    if (!Objects.isNull(deptId)) {
      List<SysDeptEntity> depts = sysDeptService.findChildrenList(deptId);
     deptIds = depts.stream().map(SysDeptEntity::getId).toArray(Long[]::new);
    }
    return userRepository.findAll(this.findWhere(userName, deptIds), pageable);
  }


  /**
   * 查询公共条件
   * @param userName 菜单名称
   * @return 条件构造对象
   */
  private Specification<SysUserEntity> findWhere(String userName, Long... deptId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();
      if (StringUtils.hasLength(userName)) {
        list.add(criteriaBuilder.like(root.get("userName"), "%"+userName+"%"));
      }

      if (!Objects.isNull(deptId)) {
        list.add((root.get("dept").get("id").in((Object[]) deptId)));
      }

      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
