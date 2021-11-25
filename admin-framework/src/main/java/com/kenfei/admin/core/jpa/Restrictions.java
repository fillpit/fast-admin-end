package com.kenfei.admin.core.jpa;

import org.springframework.util.ObjectUtils;

/**
 * 条件构造器 用于创建条件表达式
 * @author fei
 * @date 2021/11/17 21:41
 */
public class Restrictions {
  public static SimpleExpression eq(String fieldName, Object value) {
    return eq(fieldName, value, true);
  }

  public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.EQ);
  }

  public static SimpleExpression notEq(String fieldName, Object value) {
    return notEq(fieldName, value, true);
  }

  public static SimpleExpression notEq(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.NE);
  }

  public static SimpleExpression like(String fieldName, String value) {
    return like(fieldName, value, true);
  }

  public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE);
  }

  public static SimpleExpression likeLeft(String fieldName, String value) {
    return likeLeft(fieldName, value, true);
  }

  public static SimpleExpression likeLeft(String fieldName, String value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE_LEFT);
  }

  public static SimpleExpression likeRight(String fieldName, String value) {
    return likeRight(fieldName, value, true);
  }

  public static SimpleExpression likeRight(String fieldName, String value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE_RIGHT);
  }

  public static SimpleExpression gt(String fieldName, String value) {
    return gt(fieldName, value, true);
  }

  public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.GT);
  }

  public static SimpleExpression lt(String fieldName, String value) {
    return lt(fieldName, value, true);
  }

  public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.LT);
  }

  public static SimpleExpression lte(String fieldName, String value) {
    return lte(fieldName, value, true);
  }

  public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.LTE);
  }

  public static SimpleExpression gte(String fieldName, String value) {
    return gte(fieldName, value, true);
  }

  public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
    if (ignoreNull && ObjectUtils.isEmpty(value)) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.GTE);
  }

  public static SimpleExpression isNull(String fieldName) {
    return new SimpleExpression(fieldName, null, Criterion.Operator.NULL);
  }

  public static SimpleExpression isNotNull(String fieldName) {
    return new SimpleExpression(fieldName, null, Criterion.Operator.NOT_NULL);
  }

  public static LogicalExpression and(Criterion... criterions) {
    return new LogicalExpression(criterions, Criterion.Operator.AND);
  }

  public static LogicalExpression or(Criterion... criterions) {
    return new LogicalExpression(criterions, Criterion.Operator.OR);
  }

  @SuppressWarnings("rawtypes")
  public static SimpleExpression in(String fieldName, Object... value) {
    return in(fieldName, value, true);
  }

  @SuppressWarnings("rawtypes")
  public static SimpleExpression in(String fieldName, Object[] value, boolean ignoreNull) {
    if (ignoreNull && (value == null || ObjectUtils.isEmpty(value))) {
      return null;
    }
    return new SimpleExpression(fieldName, value, Criterion.Operator.IN);
  }

  @SuppressWarnings("rawtypes")
  public static LogicalExpression notIn(String fieldName, Object... value) {
    return notIn(fieldName, value, true);
  }

  @SuppressWarnings("rawtypes")
  public static LogicalExpression notIn(String fieldName, Object[] value, boolean ignoreNull) {
    if (ignoreNull && (value == null || ObjectUtils.isEmpty(value))) {
      return null;
    }

    SimpleExpression[] ses = new SimpleExpression[value.length];
    int i = 0;
    for (Object obj : value) {
      ses[i] = new SimpleExpression(fieldName, obj, Criterion.Operator.NE);
      i++;
    }

    return new LogicalExpression(ses, Criterion.Operator.AND);
  }
}
