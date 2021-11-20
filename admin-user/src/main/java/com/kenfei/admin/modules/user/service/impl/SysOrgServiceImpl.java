package com.kenfei.admin.modules.user.service.impl;

import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.modules.user.entity.SysOrgEntity;
import com.kenfei.admin.modules.user.repository.SysOrgRepository;
import com.kenfei.admin.modules.user.service.SysOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (SysOrg)表服务实现类
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
@Service("sysOrgService")
public class SysOrgServiceImpl extends AbstractServiceImpl<SysOrgEntity, Long> implements SysOrgService {
  private final SysOrgRepository sysOrgRepository;

  @Autowired
  public SysOrgServiceImpl(SysOrgRepository sysOrgRepository) {
    super(sysOrgRepository);
    this.sysOrgRepository = sysOrgRepository;
  }

  @Override
  public Page<SysOrgEntity> index(Long parentId, Pageable pageable) {
    return sysOrgRepository.findAll(findWhere(parentId, null), pageable);
  }

  @Override
  public List<SysOrgEntity> list(Boolean enabled, Long parentId) {
    return sysOrgRepository.findAll(findWhere(parentId, enabled));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public SysOrgEntity save(SysOrgEntity entity) {
    entity = super.save(entity);
    // 清理缓存
    updateSubCnt(entity.getParentId());

    return entity;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public SysOrgEntity update(SysOrgEntity entity) {
    // 旧的部门
    Long oldPid = findById(entity.getId()).getParentId();
    Long newPid = entity.getParentId();
    if(entity.getParentId() != null && entity.getParentId().equals(entity.getParentId())) {
      throw new AppException("上级不能为自己");
    }
    entity = super.update(entity);
    // 更新父节点中子节点数目
    updateSubCnt(oldPid);
    updateSubCnt(newPid);

    return entity;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(SysOrgEntity entity) {
    super.delete(entity);

    // 更新父节点中子节点数目
    updateSubCnt(entity.getParentId());
  }

  @Override
  public List<SysOrgEntity> getSuperior(SysOrgEntity entity, List<SysOrgEntity> objects) {
    if(entity.getParentId() == null){
      objects.addAll(sysOrgRepository.findAllByParentIdIsNull());
      return objects;
    }
    objects.addAll(sysOrgRepository.findAllByParentId(entity.getParentId()));
    return getSuperior(findById(entity.getParentId()), objects);
  }

  @Override
  public List<SysOrgEntity> findByParentId(Long parentId) {
    return sysOrgRepository.findAll(findWhere(parentId, null));
  }

  @Override
  public List<SysOrgEntity> findChildrenList(Long parentId) {
    SysOrgEntity dept = findById(parentId);
    return childrenList(dept, new LinkedList<>());
  }

  private List<SysOrgEntity> childrenList(SysOrgEntity dept, List<SysOrgEntity> depts) {
    List<SysOrgEntity> result = this.findByParentId(dept.getId());
    for (SysOrgEntity item : result) {
      childrenList(item, depts);
    }
    depts.add(dept);
    return depts;
  }


  @Override
  public List<SysOrgEntity> buildTree(List<SysOrgEntity> entitys) {
    return tree(null, entitys);
  }

  private List<SysOrgEntity> tree(Long parentId, List<SysOrgEntity> menus) {
    List<SysOrgEntity> current =
      menus.stream()
        .parallel()
        .filter(v -> Objects.equals(parentId, v.getParentId()))
        .distinct()
        .collect(Collectors.toList());
    for (SysOrgEntity menu : current) {
      List<SysOrgEntity> childrenMenu = this.tree(menu.getId(), menus);
      menu.setChildren(childrenMenu);
    }

    return current;
  }

  /**
   * 查询公共条件
   * @param enabled 菜单名称
   * @return 条件构造对象
   */
  private Specification<SysOrgEntity> findWhere(Long parentId, Boolean enabled) {
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


  private void updateSubCnt(Long deptId){
    if(deptId != null){
      int count = sysOrgRepository.countByParentId(deptId);
      sysOrgRepository.updateSubCntById(count, deptId);
    }
  }
}

