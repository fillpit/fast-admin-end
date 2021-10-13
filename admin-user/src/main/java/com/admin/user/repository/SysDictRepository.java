package com.admin.user.repository;

import com.admin.user.entity.SysDictEntity;
import com.admin.core.repository.BaseRepository;
import java.lang.Long;
import java.util.List;

/**
 * 数据字典(SysDict)表数据库访问层
 *
 * @author kenfei
 * @since 2021-08-16 22:18:11
 */
public interface SysDictRepository extends BaseRepository<SysDictEntity, Long> {}
