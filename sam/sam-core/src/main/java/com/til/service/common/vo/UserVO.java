package com.til.service.common.vo;

import java.io.Serializable;
import java.util.Date;

public class UserVO implements Serializable{
	
	private Integer id;
	private String fullName;
	private String emailAddress;
	private String secondaryEmailAddress;
	private String mobile;
	private boolean enabled;
	private Date validFrom;
	private Date validTill;
	private Date createdate;
	private Short roleId;
	private String roleName;
	private int rightLevel;
	private Short websiteId;
	private String websiteName;
	
	public UserVO() {
		
	}
	
	public UserVO(Integer id, String fullName, String emailAddress, String secondaryEmailAddress, String mobile,
			boolean enabled, Date validFrom, Date validTill, Date createdate, Short roleId, String roleName, 
			int rightLevel, Short websiteId, String websiteName) {
		
		this.id = id;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.secondaryEmailAddress = secondaryEmailAddress;
		this.mobile = mobile;
		this.enabled = enabled;
		this.validFrom = validFrom;
		this.validTill = validTill;
		this.createdate = createdate;
		this.roleId = roleId;
		this.roleName = roleName;
		this.rightLevel = rightLevel;
		this.websiteId = websiteId;
		this.websiteName = websiteName;
	}

	public int getRightLevel() {
		return rightLevel;
	}
	public void setRightLevel(int rightLevel) {
		this.rightLevel = rightLevel;
	}
	public String getSecondaryEmailAddress() {
		return secondaryEmailAddress;
	}
	public void setSecondaryEmailAddress(String secondaryEmailAddress) {
		this.secondaryEmailAddress = secondaryEmailAddress;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Short getRoleId() {
		return roleId;
	}
	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTill() {
		return validTill;
	}
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}
	public Short getWebsiteId() {
		return websiteId;
	}
	public void setWebsiteId(Short websiteId) {
		this.websiteId = websiteId;
	}
	public String getWebsiteName() {
		return websiteName;
	}
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
	
	
}
