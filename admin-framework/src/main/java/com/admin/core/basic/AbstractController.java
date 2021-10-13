package com.admin.core.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基本 控制器
 * @author fei
 * @date 2018/10/25
 */
@Slf4j
@Validated
public abstract class AbstractController<T, ID extends Serializable> {

  private InterfaceService<T, ID> service;

  ObjectMapper mapper = new ObjectMapper();

  public AbstractController(InterfaceService<T, ID> service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public T findOne(@NotNull @PathVariable ID id) {
    return service.findById(id);
  }

  /**
   * 获取查询构造器
   * @return 查询构造器
   */
  public static ExampleMatcher getMatcher() {
    return ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
  }


  protected <O> String toJSONString(O o) {
    try {
      return mapper.writer().writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

}

