package com.kenfei.admin.modules.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * 用户表.
 *
 * @author fei
 * @date 2018/2/27
 */
@Entity
@Table(name = "sys_user", schema = "fastAdmin")
public class SysUserEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -2028298888228036L;
  /** 主键列 */
  private Long id;
  /** 用户名. */
  @NotBlank private String userName;
  /** 昵称 */
  @NotBlank private String nickName;
  /** 性别 */
  @NotBlank private String gender;
  /** 密码. */
  private String password;
  /** 邮箱. */
  private String email;
  /** 状态. */
  @NotNull private Boolean enabled;
  /** 最后登陆时间. */
  private Date lastLoginTime;
  /** 手机号码. */
  @NotNull private String phone;
  /** 头像. */
  private String avatar;
  /** 微信用户ID. */
  private String openId;
  /** 角色集合. */
  private Set<SysRoleEntity> roles;
  /** 用户岗位 */
  private Set<SysJobsEntity> jobs;
  /** 用户部门（显示用） */
  private SysDeptEntity dept;
  /** 部门ID（更新用） */
  private Long deptId;
  /** 工号 */
  private String code;
  /** 公司（显示用） */
  private SysOrgEntity org;
  /** 公司ID（更新用） */
  private Long orgId;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "user_name")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @JsonIgnore
  @Basic
  @Column(name = "password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Basic
  @Column(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Basic
  @Column(name = "last_login_time")
  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  @Basic
  @Column(name = "phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Basic
  @Column(name = "avatar")
  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  // cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
  @ManyToMany(fetch = FetchType.EAGER) // FetchType.LAZY：延迟加载 。FetchType.EAGER：急加载
  @JoinTable(
      name = "sys_users_roles",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  @org.hibernate.annotations.Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
  public Set<SysRoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<SysRoleEntity> roles) {
    this.roles = roles;
  }

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "dept_id", updatable = false, insertable = false)
  public SysDeptEntity getDept() {
    return dept;
  }

  public void setDept(SysDeptEntity dept) {
    this.dept = dept;
  }

  @Basic
  @Column(name = "open_id")
  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  @Basic
  @Column(name = "nick_name")
  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  @Basic
  @Column(name = "gender")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Basic
  @Column(name = "dept_id")
  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "sys_users_jobs",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")})
  public Set<SysJobsEntity> getJobs() {
    return jobs;
  }

  public void setJobs(Set<SysJobsEntity> jobs) {
    this.jobs = jobs;
  }

  @Basic
  @Column(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "org_id", updatable = false, insertable = false)
  public SysOrgEntity getOrg() {
    return org;
  }

  public void setOrg(SysOrgEntity org) {
    this.org = org;
  }

  @Basic
  @Column(name = "org_id")
  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }
}
