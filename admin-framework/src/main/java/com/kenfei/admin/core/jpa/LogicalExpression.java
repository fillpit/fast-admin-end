package com.kenfei.admin.core.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑条件表达式 用于复杂条件时使用，如但属性多对应值的OR查询等
 * @author fei
 * @date 2021/11/17 21:40
 */
public class LogicalExpression implements Criterion {
  private Criterion[] criterion;
  private Operator operator;

  public LogicalExpression(Criterion[] criterion, Operator operator) {
    this.criterion = criterion;
    this.operator = operator;
  }

  @Override
  public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    List<Predicate> predicates = new ArrayList<Predicate>();
    for (Criterion aCriterion : this.criterion) {
      predicates.add(aCriterion.toPredicate(root, query, builder));
    }
    switch (operator) {
      case OR:
        return builder.or(predicates.toArray(new Predicate[0]));
      case AND:
        return builder.and(predicates.toArray(new Predicate[0]));
      default:
        return null;
    }
  }
}
