/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Harsh.Gupta
 * 
 */
@NamedQueries({
	@NamedQuery (
		    name = "findAllUsersByWebsiteId",
		    query = "select new com.til.service.common.vo.UserVO(u.id, u.fullName, u.emailAddress, u.secondaryEmailAddress," + 
					"u.mobileNumber, u.enabled, u.validFrom, u.validTill, u.createDate, up.id, up.roleName, up.sortOrder, w.id," +
					"w.name) from User u join u.websiterole up join up.index w where w.id = :websiteid " + 
					"order by u.fullName"
	),
	@NamedQuery (
		    name = "findAllUsers",
		    query = "select new com.til.service.common.vo.UserVO(u.id, u.fullName, u.emailAddress, u.secondaryEmailAddress," + 
					"u.mobileNumber, u.enabled, u.validFrom, u.validTill, u.createDate, up.id, up.roleName, up.sortOrder, w.id," +
					"w.name) from User u join u.websiterole up join up.index w " + 
					"order by u.fullName"
	)
})
@Entity
@Table(name = "user")
public class User implements Serializable {
	// fields
	private static final Log log = LogFactory.getLog(User.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "emailaddress")
	@NotBlank(message = "EmailAddress cannot be empty.")
	private String emailAddress;
	@NotBlank(message = "Password cannot be empty.")
	private String passphrase;
	private String decrypted;
	@Column(name = "fullname")
	@NotBlank(message = "FullName cannot be empty.")
	private String fullName;
	@Column(name = "mobilenumber")
	private String mobileNumber;
	@Column(name = "emailaddress1")
	private String secondaryEmailAddress;
	@Column(name = "isenabled")
	private boolean enabled = true;
	@Column(name = "validfrom")
	//@NotBlank(message = "ValidFrom cannot be empty.")
	private Date validFrom;
	@Column(name = "validtill")
	//@NotBlank(message = "ValidTill cannot be empty.")
	private Date validTill;
	@Column(name = "createdate")
	private Date createDate = new Date();

	// Below 2 variables represent one of the current website & role to which
	// user belongs,
	// out of many different websites and roles;
	private transient Website website;
	private transient Role role;
	private transient List<Short> websiteids;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_permission", joinColumns = { @JoinColumn(name = "userid") }, inverseJoinColumns={@JoinColumn(name="roleid")})
	@MapKeyJoinColumn(name="websiteid")
	private Map<Website, Role> websiterole;

	public User() {

	}
	
	public User(Integer id) {
		this.id = id;
	}
	
	public User(User user, Set<WebsiteRole> websiterole) {
		this.id = user.id;
		this.emailAddress = user.emailAddress;
		this.passphrase = user.passphrase;
		this.decrypted = user.decrypted;
		this.fullName = user.fullName;
		this.mobileNumber = user.mobileNumber;
		this.secondaryEmailAddress = user.secondaryEmailAddress;
		this.enabled = user.enabled;
		this.validFrom = user.validFrom;
		this.validTill = user.validTill;
		this.createDate = user.createDate;
		/*this.websiterole = websiterole;*/
	}

	public String getDecrypted() {
		return decrypted;
	}

	public void setDecrypted(String decrypted) {
		this.decrypted = decrypted;
	}

	public String getSecondaryEmailAddress() {
		return secondaryEmailAddress;
	}

	public void setSecondaryEmailAddress(String secondaryEmailAddress) {
		this.secondaryEmailAddress = secondaryEmailAddress;
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

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the passphrase
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * @param passphrase
	 *            the passphrase to set
	 */
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
		result = prime * result
				+ ((passphrase == null) ? 0 : passphrase.hashCode());

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (enabled != other.enabled)
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobileNumber == null) {
			if (other.mobileNumber != null)
				return false;
		} else if (!mobileNumber.equals(other.mobileNumber))
			return false;
		if (passphrase == null) {
			if (other.passphrase != null)
				return false;
		} else if (!passphrase.equals(other.passphrase))
			return false;

		return true;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public Role getRole() {
		return role;
	}

	public Website getWebsite() {
		return website;
	}

	public Map<Website, Role> getWebsiterole() {
		return websiterole;
	}

	public void setWebsiterole(Map<Website, Role> websiterole) {
		this.websiterole = websiterole;
	}

	
	public List<Short> getWebsiteids() {
		return websiteids;
	}

	public void setWebsiteids(List<Short> websiteids) {
		this.websiteids = websiteids;
	}

	public void copyTo(User user) {
		user.setId(getId());
		user.setEmailAddress(getEmailAddress());
		user.setPassphrase(getPassphrase());
		user.setDecrypted(getDecrypted());
		user.setFullName(getFullName());
		user.setMobileNumber(getMobileNumber());
		user.setSecondaryEmailAddress(getSecondaryEmailAddress());
		user.setEnabled(isEnabled());
		user.setValidFrom(getValidFrom());
		user.setValidTill(getValidTill());
		user.setCreateDate(getCreateDate());
		user.setRole(getRole());
		user.setWebsite(getWebsite());
		user.setWebsiterole(getWebsiterole());
	}

	@Override
	public String toString() {
		String user = "UserName " + getFullName() + " EmailAdress "
				+ getEmailAddress();
		return user;
	}

}
