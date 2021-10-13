package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.user.entity.SysJobsEntity;
import com.admin.user.repository.SysJobsRepository;
import com.admin.user.service.SysJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * (SysJob)表服务实现类
 *
 * @author kenfei
 * @since 2021-08-17 20:08:08
 */
@Service("sysJobService")
public class SysJobsServiceImpl extends AbstractServiceImpl<SysJobsEntity, Long>
    implements SysJobsService {
  private final SysJobsRepository sysJobsRepository;

  @Autowired
  public SysJobsServiceImpl(SysJobsRepository sysJobsRepository) {
    super(sysJobsRepository);
    this.sysJobsRepository = sysJobsRepository;
  }

  @Override
  public Page<SysJobsEntity> index(String name, Boolean enabled, Pageable pageable) {
    return sysJobsRepository.findAll(findWhere(name, enabled), pageable);
  }

  @Override
  public List<SysJobsEntity> list(Boolean enabled) {
    return sysJobsRepository.findAll(findWhere(null, enabled));
  }

  /**
   * 查询公共条件
   *
   * @return 条件构造对象
   */
  private Specification<SysJobsEntity> findWhere(String name, Boolean enabled) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();
      if (StringUtils.hasLength(name)) {
        list.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
      }
      if (!Objects.isNull(enabled)) {
        list.add(criteriaBuilder.equal(root.get("enabled"), enabled));
      }
      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
