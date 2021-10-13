package com.admin.core.basic;

import com.admin.core.repository.BaseRepository;
import com.admin.core.exception.AppException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务层基本方法
 *
 * @author fei
 * @date 2017/8/17
 */
@Validated
public abstract class AbstractServiceImpl<T, ID extends Serializable> implements InterfaceService<T, ID> {

  private BaseRepository<T, ID> repository;

  protected AbstractServiceImpl(BaseRepository<T, ID> repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public T save(@Valid T entity) {
    return repository.save(entity);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<T> save(@Valid Iterable<? extends T> entities) {
    List<T> list = new ArrayList<>();
    for (T t : entities) {
      list.add(this.save(t));
    }
    return list;
  }

  /**
   * 更新对象
   *
   * @param entity 实体
   * @return 更新后的实体对象
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public T update(@Valid T entity) {
    return repository.save(entity);
  }

  /**
   * 删除对象
   *
   * @param id 主键
   */
  @Override
  public final void delete(ID id) {
    T t = this.findById(id);
    this.delete(t);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(T entity) {
    repository.delete(entity);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Iterable<? extends T> entities) {
    for (T t : entities) {
      this.delete(t);
    }
  }

  @Override
  public T findById(@NotNull ID id) {
    return repository.findById(id).orElseThrow(() -> new AppException("无效 ID"));
  }

  @Override
  public List<T> findAllById(Iterable<ID> ids) {
    return repository.findAllById(ids);
  }

  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public List<T> findAll(@NotNull Example<T> example) {
    return repository.findAll(example);
  }

  @Override
  public List<T> findAll(@NotNull Example<T> example, Sort sort) {
    return repository.findAll(example, sort);
  }

  @Override
  public Page<T> findAll(@NotNull Example<T> example, @NotNull Pageable pageable) {
    return repository.findAll(example, pageable);
  }

}
