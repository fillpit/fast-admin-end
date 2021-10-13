package com.admin.user.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 登陆日志(SysLoginInfo)实体类
 *
 * @author fei
 * @since 2020-10-22 15:25:19
 */
@Entity
@Table(name = "sys_login_info")
public class SysLoginInfoEntity implements Serializable {
  private static final long serialVersionUID = -26040520980520062L;
  /** 主键列*/
  private Long id;
  /** 登陆用户名 */
  private String loginName;
  /** 0=成功,1=失败 */
  private Boolean status;
  /** 登录地址 */
  private String ipaddr;
  /** 登录地点 */
  private String loginLocation;
  /** 浏览器 */
  private String browser;
  /** 操作系统 */
  private String os;
  /** 提示消息 */
  private String msg;
  /** 访问时间 */
  private Date loginTime;

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
  @Column(name = "login_name")
  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  @Basic
  @Column(name = "status")
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Basic
  @Column(name = "ipaddr")
  public String getIpaddr() {
    return ipaddr;
  }

  public void setIpaddr(String ipaddr) {
    this.ipaddr = ipaddr;
  }

  @Basic
  @Column(name = "login_location")
  public String getLoginLocation() {
    return loginLocation;
  }

  public void setLoginLocation(String loginLocation) {
    this.loginLocation = loginLocation;
  }

  @Basic
  @Column(name = "browser")
  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  @Basic
  @Column(name = "os")
  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  @Basic
  @Column(name = "msg")
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Basic
  @Column(name = "login_time")
  public Date getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }
}
