package com.kenfei.admin.bpmn.repository;

import com.kenfei.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.kenfei.admin.core.common.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.lang.Long;
import java.util.List;

/**
 * 执行的流程节点(ApproveTaskNode)表数据库访问层
 *
 * @author kenfei
 * @since 2021-11-03 13:56:15
 */
public interface ApproveTaskNodeRepository extends BaseRepository<ApproveTaskNodeEntity, Long> {

  /**
   * 查询代表列表
   * @param userId 用户ID
   * @param roleId 角色ID
   * @param jobsId 岗位ID
   * @param deptId 部门ID
   * @return 代办列表
   */
  @Query(value = "select node.* from tb_approve_task task, tb_approve_task_node node where (node.candidate_approve_user_id is null or node.candidate_approve_user_id= ?1) " +
    "and (node.candidate_approve_role_id is null or node.candidate_approve_role_id in ?2) " +
    "and (node.candidate_approve_jobs_id is null or node.candidate_approve_jobs_id in ?3) " +
    "and (node.candidate_approve_dept_id is null or node.candidate_approve_dept_id = ?4) " +
    "and task.del_flag = 0 and (node.approve_state not in (0, 1) or node.approve_state is null) " +
    "and node.status = ?5 and task.id = node.approve_task_id",
    nativeQuery = true)
  Page<ApproveTaskNodeEntity> findApproveTaskList(Long userId, List<Long> roleId, List<Long> jobsId, Long deptId, Integer status, Pageable pageable);

  /**
   * 统计代办的数量
   * @param userId 用户ID
   * @param roleId 角色ID
   * @param jobsId 岗位ID
   * @param deptId 部门ID
   * @return 数量
   */
  @Query(value = "select count(1) from tb_approve_task where (candidate_Approve_User_id is null or candidate_Approve_User_id= ?1) " +
    "and (candidate_approve_role_id is null or candidate_approve_role_id in ?2) " +
    "and (candidate_approve_jobs_id is null or candidate_approve_jobs_id in ?3) " +
    "and (candidate_approve_dept_id is null or candidate_approve_dept_id = ?4) " +
    "and del_flag = 0 and status = 0 ", nativeQuery = true)
  Long countApproveTaskList(Long userId, List<Long> roleId, List<Long> jobsId, Long deptId);

}

