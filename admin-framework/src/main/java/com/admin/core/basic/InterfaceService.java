package com.admin.core.basic;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author fei
 * @date 2017/8/16
 */
public interface InterfaceService<T, ID extends Serializable> {

  /**
   * 保存对象
   *
   * @param entity 实体
   * @return 返回实体对象
   */
  @NotNull
  T save(@Valid T entity);

  /**
   * 批量保存对象
   *
   * @param entities 对象集合
   * @return 实体对象集合
   */
  @NotNull
  List<T> save(@Valid Iterable<? extends T> entities);

  /**
   * 更新对象
   *
   * @param entity 实体
   * @return 更新后的实体对象
   */
  @NotNull
  T update(@Valid T entity);

  /**
   * 删除对象
   *
   * @param id 主键
   */
  void delete(@NotNull ID id);

  /**
   * 删除对象
   *
   * @param entity 实体
   */
  void delete(@NotNull T entity);

  /**
   * 批量删除对象
   *
   * @param entities 实体集合
   */
  void delete(@NotNull Iterable<? extends T> entities);

  /**
   * 根据ID获取实体对象
   *
   * @param id ID主键
   * @return 实体
   */
  T findById(@NotNull ID id);

  /**
   * 根据 ID 集合获取 实体集合
   * @param ids ID 集合
   * @return 实体集合
   */
  List<T> findAllById(Iterable<ID> ids);

  /**
   * 获取所有的实体对象
   *
   * @return 实体对象
   */
  List<T> findAll();

  /**
   * 获取所有的实体对象
   *
   * @param pageable 分页参数
   * @return 实体对象
   */
  Page<T> findAll(@NotNull Pageable pageable);

  /**
   * 获取所有的实体对象
   *
   * @param example QBE
   * @return 实体对象集合
   */
  List<T> findAll(@NotNull Example<T> example);

  /**
   * 获取所有的实体对象
   *
   * @param example QBE
   * @param sort 排序条件
   * @return 实体对象集合
   */
  List<T> findAll(@NotNull Example<T> example, Sort sort);

  /**
   * 获取所有的实体对象
   *
   * @param example QBE
   * @param pageable 分页参数
   * @return 实体对象集合
   */
  Page<T> findAll(@NotNull Example<T> example, @NotNull Pageable pageable);
}
