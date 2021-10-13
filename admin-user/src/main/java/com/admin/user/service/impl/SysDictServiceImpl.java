package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.user.entity.SysDictEntity;
import com.admin.user.repository.SysDictRepository;
import com.admin.user.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据字典(SysDict)表服务实现类
 *
 * @author kenfei
 * @since 2021-08-16 22:40:46
 */
@Service("sysDictService")
public class SysDictServiceImpl extends AbstractServiceImpl<SysDictEntity, Long>
    implements SysDictService {
  private final SysDictRepository sysDictRepository;

  @Autowired
  public SysDictServiceImpl(SysDictRepository sysDictRepository) {
    super(sysDictRepository);
    this.sysDictRepository = sysDictRepository;
  }

  @Override
  public Page<SysDictEntity> index(Pageable pageable) {
    return sysDictRepository.findAll(
        (root, criteriaQuery, criteriaBuilder) -> {
          List<Predicate> list = new LinkedList<>();
          return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
        },
        pageable);
  }

  /**
   * 查询公共条件
   *
   * @return 条件构造对象
   */
  private Specification<SysDictEntity> findWhere() {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();

      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
