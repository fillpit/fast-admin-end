package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.user.entity.SysDictDetailEntity;
import com.admin.user.repository.SysDictDetailRepository;
import com.admin.user.service.SysDictDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据字典详情(SysDictDetail)表服务实现类
 *
 * @author kenfei
 * @since 2021-08-16 22:40:46
 */
@Service("sysDictDetailService")
public class SysDictDetailServiceImpl extends AbstractServiceImpl<SysDictDetailEntity, Long>
    implements SysDictDetailService {
  private final SysDictDetailRepository sysDictDetailRepository;

  @Autowired
  public SysDictDetailServiceImpl(SysDictDetailRepository sysDictDetailRepository) {
    super(sysDictDetailRepository);
    this.sysDictDetailRepository = sysDictDetailRepository;
  }

  @Override
  public Page<SysDictDetailEntity> index(String dictName, Pageable pageable) {
    return sysDictDetailRepository.findAll(findWhere(dictName), pageable);
  }

  @Override
  public List<SysDictDetailEntity> list(String dictName) {
    return sysDictDetailRepository.findAll(findWhere(dictName));
  }

  /**
   * 查询公共条件
   *
   * @return 条件构造对象
   */
  private Specification<SysDictDetailEntity> findWhere(String dictName) {
    return (root, cq, cb) -> {
      List<Predicate> list = new LinkedList<>();
      if (StringUtils.hasLength(dictName)) {
        list.add(cb.equal(root.get("dict").get("name"), dictName));
      }

      return cq.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
