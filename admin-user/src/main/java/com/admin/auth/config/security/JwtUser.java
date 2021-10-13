package com.admin.auth.config.security;

import com.admin.user.entity.*;
import com.admin.user.enums.MenuTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用来生成token 和 验证安全的 用户类.
 *
 * @author fei
 * @date 2018/10/22
 */
@Data
public class JwtUser implements UserDetails, Serializable {
  private static final long serialVersionUID = 1138529140227318349L;

  /** 用户ID. */
  private Long id;
  /** 用户名. */
  private String userName;
  /** 昵称 */
  private String nickName;
  /** 性别 */
  private String gender;
  /** 头像 */
  private String avatar;
  /** 密码. */
  @JsonIgnore
  private String password;
  /** 邮箱. */
  private String email;
  /** 状态. */
  private Boolean enabled;
  /** 手机号码. */
  private String phone;
  /** 部门ID. */
  private Long departmentId;
  /** 最后登陆时间. */
  private Date lastLoginTime;

  /** 角色集合. */
  private Set<JwtRole> roles;
  /** 权限按钮 */
  private Set<JwtPermission> permissions;
  /** 用户岗位 */
  private Set<JwtJobs> jobs;
  /** 用户部门（显示用） */
  private JwtDept dept;

  public JwtUser(){}

  public JwtUser(SysUserEntity userEntity){
    this.id = userEntity.getId();
    this.userName = userEntity.getUserName();
    this.nickName = userEntity.getNickName();
    this.gender = userEntity.getGender();
    this.avatar = userEntity.getAvatar();
    this.password = userEntity.getPassword();
    this.email = userEntity.getEmail();
    this.enabled = userEntity.getEnabled();
    this.phone = userEntity.getPhone();
    this.departmentId = userEntity.getDeptId();
    this.lastLoginTime = userEntity.getLastLoginTime();

    this.roles = new LinkedHashSet<>();
    // 权限（打算移走）
    this.permissions = new LinkedHashSet<>();
    for (SysRoleEntity roleEntity : userEntity.getRoles()) {
      roles.add(new JwtRole(roleEntity));
      Set<JwtPermission> jwtPermissions = roleEntity.getMenus().stream()
          .filter(v -> Objects.equals(v.getType(), MenuTypeEnum.BUTTON.value()))
          .map(JwtPermission::new)
          .collect(Collectors.toSet());
      this.permissions.addAll(jwtPermissions);
    }
    // 岗位
    this.jobs = userEntity.getJobs().stream().map(JwtJobs::new).collect(Collectors.toSet());
    // 部门
    this.dept = new JwtDept(userEntity.getDept());
  }

  @Data
  public static
  class JwtRole {
    /** 角色名 */
    private String roleName;
    /** 角色ID */
    private Long roleId;

    public JwtRole(SysRoleEntity sysRoleEntity) {
      this.roleId = sysRoleEntity.getId();
      this.roleName = sysRoleEntity.getName();
    }
  }

  @Data
  public static
  class JwtPermission {
    /** 名称. */
    private String name;
    /** 路径地址. */
    private String requestUrl;
    /** 请求方法(PUT/POST/DELETE). */
    private String requestMethod;
    /** 权限标示(用来控制界面的按钮显示) */
    private String keyword;

    public JwtPermission(SysMenuEntity menu) {
      this.name = menu.getName();
      this.requestUrl = menu.getRequestUrl();
      this.requestMethod = menu.getRequestMethod();
      this.keyword = menu.getKeyword();
    }
  }

  @Data
  public static class JwtJobs {
    /** 主键列*/
    private Long id;
    /** 岗位名称 */
    private String name;

    public JwtJobs(SysJobsEntity entity) {
      this.id = entity.getId();
      this.name = entity.getName();
    }
  }

  @Data
  public static class JwtDept {
    /** 主键列*/
    private Long id;
    /** 岗位名称 */
    private String name;

    public JwtDept(SysDeptEntity entity) {
      this.id = Optional.ofNullable(entity).map(SysDeptEntity::getId).orElse(0L);
      this.name = Optional.ofNullable(entity).map(SysDeptEntity::getName).orElse("");
    }
  }

  /**
   * 用来验证当前登陆用户的角色信息.
   *
   * @return 角色名列表
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (JwtRole role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    }
    return authorities;
  }

  /**
   * 登陆密码(加密规则写在配置文件里).
   *
   * @return 密码
   */
  @JsonIgnore
  @Override
  public String getPassword() {
    return this.password;
  }

  /**
   * 当前登陆用户.
   *
   * @return 用户名
   */
  @Override
  public String getUsername() {
    return this.userName;
  }

  /**
   * 账户是否未过期.
   *
   * @return true: 正常， false: 过期
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 账户是否未锁定.
   *
   * @return true: 正常， false: 锁定
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 密码是否未过期.
   *
   * @return true: 正常， false: 过期
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 账户是否激活.
   *
   * @return true: 正常， false: 禁用
   */
  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}
