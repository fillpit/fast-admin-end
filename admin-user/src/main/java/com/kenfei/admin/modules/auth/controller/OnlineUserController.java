package com.kenfei.admin.modules.auth.controller;

import com.kenfei.admin.modules.auth.service.OnlineUserService;
import com.kenfei.admin.core.config.resolvers.JsonParam;
import com.kenfei.admin.core.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 在线用户 控制器
 * @author fei
 * @since 2019/12/25 14:35
 */
@Validated
@RestController
@RequestMapping("sys/online")
public class OnlineUserController {
  @Autowired private OnlineUserService onlineUserService;

  /**
   * 查询在线用户
   * @param filter
   * @param pageable
   * @return
   */
  @GetMapping
  public Object query(String filter, Pageable pageable){
    return onlineUserService.getAll(filter, pageable);
  }

  /**
   * 强制下线
   * @param userId 用户ID
   */
  @PutMapping("offline")
  public void offline(@JsonParam Long userId) {

  }

  /**
   * 踢出用户
   * @param keys
   * @return
   * @throws Exception
   */
  @DeleteMapping
  public void delete(@RequestBody Set<String> keys) throws Exception {
    for (String key : keys) {
      // 解密Key
      key = EncryptUtils.desDecrypt(key);
      onlineUserService.kickOut(key);
    }
  }

}

