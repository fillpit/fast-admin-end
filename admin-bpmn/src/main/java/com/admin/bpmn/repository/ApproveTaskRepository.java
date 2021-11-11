package com.admin.bpmn.repository;

import com.admin.bpmn.entity.ApproveTaskEntity;
import com.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.admin.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.lang.Integer;
import java.util.List;

/**
 * 激活的任务(ApproveTask)表数据库访问层
 *
 * @author kenfei
 * @since 2021-11-01 13:58:32
 */
public interface ApproveTaskRepository extends BaseRepository<ApproveTaskEntity, Long> {

  /**
   * 获取发起人的任务列表
   *
   * @param userId 用户ID
   * @param pageable 分页
   * @return /
   */
  @Query(
      value =
          "select task.* from tb_approve_task task "
              + "where task.del_flag=0 and task.user_id=?1",
      nativeQuery = true)
  Page<ApproveTaskEntity> findOriginator(Long userId, Pageable pageable);
}

