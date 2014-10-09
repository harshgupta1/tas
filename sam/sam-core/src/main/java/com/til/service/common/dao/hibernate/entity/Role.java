/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Harsh.Gupta
 *
 */
@NamedQueries({
	@NamedQuery (
		    name = "findByRoleName",
		    query = "SELECT r FROM Role r WHERE roleName = :name"
	)
	})
@Entity
@Table(name="role")
public class Role implements Serializable{
	
	// fields
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Short  id;
    @Column(name="rolename")
    private String 	roleName;
    @Column(name="roledescription")
    private String 	roleDescription;
    @Column(name="sortorder")
    private int sortOrder;
    @Column(name="createdate")
    private Date 	createDate = new Date() ;
    @Column(name="updatedate")
    private Date 	updateDate = createDate ;
	
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * @return the id
	 */
	public Short getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Short id) {
		this.id = id;
	}
	/**
	 * @return the key
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param key the key to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return the description
	 */
	public String getRoleDescription() {
		return roleDescription;
	}
	/**
	 * @param description the description to set
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result
				+ ((roleDescription == null) ? 0 : roleDescription.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (roleDescription == null) {
			if (other.roleDescription != null)
				return false;
		} else if (!roleDescription.equals(other.roleDescription))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		return true;
	}
    
	public void copyTo(Role role) {
		role.setId(getId());
		role.setRoleName(getRoleName());
		role.setRoleDescription(getRoleDescription());
		role.setCreateDate(getCreateDate());
		role.setUpdateDate(getUpdateDate());;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[W{" + getId() + "," + getRoleName() + "," + getRoleDescription() + "}]" ;
	}
}
