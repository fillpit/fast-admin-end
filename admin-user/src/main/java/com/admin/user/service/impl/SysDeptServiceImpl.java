package com.admin.user.service.impl;

import com.admin.core.basic.AbstractServiceImpl;
import com.admin.user.entity.SysDeptEntity;
import com.admin.user.entity.SysMenuEntity;
import com.admin.user.repository.SysDeptRepository;
import com.admin.user.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (SysDept)表服务实现类
 *
 * @author kenfei
 * @since 2021-08-16 16:23:38
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends AbstractServiceImpl<SysDeptEntity, Long>
    implements SysDeptService {
  private final SysDeptRepository sysDeptRepository;

  @Autowired
  public SysDeptServiceImpl(SysDeptRepository sysDeptRepository) {
    super(sysDeptRepository);
    this.sysDeptRepository = sysDeptRepository;
  }

  @Override
  public Page<SysDeptEntity> index(Long parentId, Pageable pageable) {
    return sysDeptRepository.findAll(findWhere(parentId, null), pageable);
  }

  @Override
  public List<SysDeptEntity> list(Boolean enabled, Long parentId) {
    return sysDeptRepository.findAll(findWhere(parentId, enabled));
  }

  @Override
  public List<SysDeptEntity> getSuperior(SysDeptEntity entity, List<SysDeptEntity> objects) {
    if(entity.getParentId() == null){
      objects.addAll(sysDeptRepository.findAllByParentIdIsNull());
      return objects;
    }
    objects.addAll(sysDeptRepository.findAllByParentId(entity.getParentId()));
    return getSuperior(findById(entity.getParentId()), objects);
  }

  @Override
  public List<SysDeptEntity> findByParentId(Long parentId) {
    return sysDeptRepository.findAll(findWhere(parentId, null));
  }

  @Override
  public List<SysDeptEntity> findChildrenList(Long parentId) {
    SysDeptEntity dept = findById(parentId);
    return childrenList(dept, new LinkedList<>());
  }

  private List<SysDeptEntity> childrenList(SysDeptEntity dept, List<SysDeptEntity> depts) {
    List<SysDeptEntity> result = this.findByParentId(dept.getId());
    for (SysDeptEntity item : result) {
      childrenList(item, depts);
    }
    depts.add(dept);
    return depts;
  }


  @Override
  public List<SysDeptEntity> buildTree(List<SysDeptEntity> entitys) {
    return tree(null, entitys);
  }

  private List<SysDeptEntity> tree(Long parentId, List<SysDeptEntity> menus) {
    List<SysDeptEntity> current =
      menus.stream()
        .parallel()
        .filter(v -> Objects.equals(parentId, v.getParentId()))
        .distinct()
        .collect(Collectors.toList());
    for (SysDeptEntity menu : current) {
      List<SysDeptEntity> childrenMenu = this.tree(menu.getId(), menus);
      menu.setChildren(childrenMenu);
    }

    return current;
  }

  /**
   * 查询公共条件
   * @param enabled 菜单名称
   * @return 条件构造对象
   */
  private Specification<SysDeptEntity> findWhere(Long parentId, Boolean enabled) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> list = new LinkedList<>();
      if (!Objects.isNull(enabled)) {
        list.add(criteriaBuilder.equal(root.get("enabled"), enabled));
      }

      if (Objects.isNull(parentId)) {
        list.add(criteriaBuilder.isNull(root.get("parentId")));
      } else {
        list.add(criteriaBuilder.equal(root.get("parentId"), parentId));
      }

      return criteriaQuery.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
