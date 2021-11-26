package com.kenfei.admin.core.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fei
 * @date 2021/11/17 21:37
 */
public class Criteria<T> implements Specification<T> {
  private final List<Criterion> criterions = new ArrayList<>();

  @Override
  @Nullable
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    if (!criterions.isEmpty()) {
      List<Predicate> predicates = new ArrayList<>();
      for (Criterion c : criterions) {
        predicates.add(c.toPredicate(root, query, builder));
      }
      // 将所有条件用 and 联合起来
      return builder.and(predicates.toArray(new Predicate[0]));
    }
    return builder.conjunction();
  }

  public Criteria<T> add(Criterion criterion) {
    if (criterion != null) {
      criterions.add(criterion);
    }
    return this;
  }
}
