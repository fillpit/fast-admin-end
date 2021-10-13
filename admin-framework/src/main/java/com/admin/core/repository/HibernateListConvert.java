package com.admin.core.repository;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * Hibernate 集合转字符串
 *
 * @author fei
 * @since 2019-01-31 23:40
 */
public class HibernateListConvert implements UserType, ParameterizedType, Serializable {

  /** 保存到数据库时的分割符(default: , ). */
  private String regex = ",";

  /** 转化成 Java 对象时, 对象的类型(default: String.class ). */
  private Class elementType = String.class;

  /** Java 集合类型(default: LinkedList.class ) */
  private Class listType = LinkedList.class;

  /**
   * 设置要转换 Java 类型参数.
   *
   * @param parameters 参数对象
   */
  @Override
  public void setParameterValues(Properties parameters) {
    // 设置 分割符
    String regex = (String)parameters.get("regex");
    if(StringUtils.hasLength(regex)) {
          this.regex = regex;
    }

    // 设置 集合类型
    String listType = (String)parameters.get("listType");
    if (StringUtils.hasLength(listType)) {
      try {
        this.listType = Class.forName(listType);
      } catch (ClassNotFoundException e) {
        throw new HibernateException(e);
      }
    }

    // 设置 元素类型
    String elementType = (String)parameters.get("elementType");
    if (StringUtils.hasLength(elementType)) {
      try {
        this.elementType = Class.forName(elementType);
      } catch (ClassNotFoundException e) {
        throw new HibernateException(e);
      }
    }
  }

  @Override
  public int[] sqlTypes() {
    return new int[] {Types.VARBINARY};
  }

  @Override
  public Class returnedClass() {
    return List.class;
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws HibernateException, SQLException {
    String items = (String) StringType.INSTANCE.get(rs, names[0], session);
    if (!StringUtils.hasLength(items)) {
      return Collections.EMPTY_LIST;
    }

    List list = (List) BeanUtils.instantiateClass(this.listType);
    String[] strArray = items.split(",");
    SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();

    for (String item : strArray) {
      list.add(simpleTypeConverter.convertIfNecessary(item, this.elementType));
    }
    return list;
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws HibernateException, SQLException {
    String deVlaue = null;
    if (Objects.nonNull(value)) {
      List<String> list = new LinkedList<>();
      for (Object a : (List) value) {
        list.add(Objects.toString(a));
      }

      deVlaue = String.join(",", list);
    }

    StringType.INSTANCE.nullSafeSet(st, deVlaue, index, session);
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    if (null == value) {
      return Collections.EMPTY_LIST;
    }

    List result = new LinkedList();
    Collections.copy((List) value, result);
    return result;
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    return Objects.hashCode(x);
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return ((Serializable)value);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }
}
