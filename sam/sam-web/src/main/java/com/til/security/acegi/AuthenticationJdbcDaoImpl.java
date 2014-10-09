package com.til.security.acegi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.til.service.common.dao.hibernate.entity.Role;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;

public class AuthenticationJdbcDaoImpl extends JdbcDaoImpl {

	// Fetch all the website details of 1 user. Used IN Clause
	public static final String DEF_WEBSITE_BY_ID_QUERY = "SELECT id,name,description,emailfromaddress FROM website WHERE id in (:id)";
	public static final String DEF_ROLE_QUERY = "SELECT * FROM role";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public AuthenticationJdbcDaoImpl()
	{
	}
	
	protected void initDao() throws ApplicationContextException {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
    }
	/**
	 * LOGIC
	 * 	// Fetch all the websites to which this user has access to
	 * 	// Fetch all the Roles to which this user can belong to
	 * 	// Set the first user as custom user
	 * 	// Set All the Websites and Role to WebsiteRole Set
	 * 	// Finally add this set above to first user;
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		List<CustomUser> users = mapUsersByUsername(username);

		if (users.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}

		// Fetch all the websites to which this user has access to
		List websiteidList = new ArrayList();
		
		for (CustomUser user : users) {
			websiteidList.add(((User)user.getUser()).getWebsite().getId());
		}
		
	    SqlParameterSource namedParameters = new MapSqlParameterSource("id", websiteidList);

	    List<Website> websiteList = namedParameterJdbcTemplate.query(DEF_WEBSITE_BY_ID_QUERY, namedParameters, new RowMapper<Website>() {
	            public Website mapRow(ResultSet rs, int rowNum)
	                    throws SQLException {
	            	Website website = new Website();
	    			website.setId(rs.getShort(1));
	    			website.setName(rs.getString(2));
	    			website.setDescription(rs.getString(3));
	    			website.setEmailFromAddress(rs.getString(4));
	    			
	                return website;
	            }
	        });
	    
	    // Set the first user as custom user
	    CustomUser customUser = (CustomUser) users.get(0); // 
	    
	    // Fetch all the Roles to which this user can belong to
		List<Role> roleList = mapAuthoritiesByUsername(customUser.getUsername());
		
		List dbAuths = new ArrayList();
		
		// Grant only 1 role to this user based on CustomUser object
		for (Role role : roleList) {
			if(role.getId().equals(((User)customUser.getUser()).getRole().getId()))
			{
				GrantedAuthorityImpl authority = new GrantedAuthorityImpl(role.getRoleName());
				dbAuths.add(authority);
			}
		}
		if (dbAuths.size() == 0) {
			throw new UsernameNotFoundException("User has no GrantedAuthority");
		}

		GrantedAuthority[] arrayAuths = {};

		addCustomAuthorities(customUser.getUsername(), dbAuths);

		arrayAuths = (GrantedAuthority[]) dbAuths.toArray(arrayAuths);

		String returnUsername = customUser.getUsername();

		if (!isUsernameBasedPrimaryKey()) {
			returnUsername = username;
		}
		
		// Set All the Websites and Role to WebsiteRole Set
		Map<Website, Role> websiteRoleMap = new HashMap<Website,Role>();
		boolean isfirstuser = true;
		for (CustomUser user : users) {
			
			Short websiteid = (((User)user.getUser()).getWebsite().getId());
			Short roleid = (((User)user.getUser()).getRole().getId());
			
			Website websiteI = null;
			Role roleI = null;
			for (Website website : websiteList) {
				if(websiteid.equals(website.getId()))
				{
					websiteI = website;
					if(isfirstuser)
					{
						((User)customUser.getUser()).setWebsite(website);
						isfirstuser = false;
					}
					break;
				}
			}
			
			for (Role role : roleList) {
				if(roleid.equals(role.getId()))
				{
					roleI = role;
					break;
				}
			}
			
			// Add this website and role to WebsiteRole Set
			websiteRoleMap.put(websiteI, roleI);
		}
		
		// Finally add this set above to User;
		((User)customUser.getUser()).setWebsiterole(websiteRoleMap);
		
		
		return new CustomUser(returnUsername, customUser.getPassword(), customUser.isEnabled(),
				arrayAuths,customUser.getUser());

	}

	// ~ Inner Classes
	// ==========================================================

	/**
	 * Query object to look up a user.
	 */
	private List<CustomUser> mapUsersByUsername(String username) {
		
		return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] {username}, new RowMapper<CustomUser>() {
			public CustomUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				boolean enabled = rs.getBoolean(4);
				String fullName = rs.getString(5);
				String mobileNumber = rs.getString(6);
				Date validFrom = rs.getDate(7);
				Date validTill = rs.getDate(8);
				Short websiteId = rs.getShort(9);
				Short roleId = rs.getShort(10);
				int rightLevel = rs.getInt(11);
				String secondaryEmailAddress = rs.getString(12);
				com.til.service.common.dao.hibernate.entity.User user = new com.til.service.common.dao.hibernate.entity.User();
				
				user.setId(id);
				user.setEmailAddress(username);
				user.setPassphrase(password);
				user.setEnabled(enabled);
				user.setFullName(fullName);
				user.setMobileNumber(mobileNumber);
				user.setValidFrom(validFrom);
				user.setValidTill(validTill);
				user.setSecondaryEmailAddress(secondaryEmailAddress);
				// Set Website Properties to User Object
				// This will become the default website of the user
				Website website = new Website();
				website.setId(websiteId);
				user.setWebsite(website);
				
				// Set Role Properties to User Object
				// This will become the default role of the user
				Role role = new Role();
				role.setId(roleId);
				role.setSortOrder(rightLevel);
				user.setRole(role);
				
				CustomUser userDetails = new CustomUser(
						username,
						password,
						enabled,
						new GrantedAuthority[] { new GrantedAuthorityImpl("HOLDER") },user);
	
				return userDetails;
			}
		 });
	}

	/**
	 * Query object to look up a user's authorities.
	 */
	private List<Role> mapAuthoritiesByUsername(String username){
		return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(), new String[] {username}, new RowMapper<Role>() {
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            	Short roleid = rs.getShort(1);
				String roleName = getRolePrefix() + rs.getString(3);
				Role role = new Role();
				role.setId(roleid);
				role.setRoleName(roleName);
				role.setSortOrder(rs.getInt(4));
				//GrantedAuthorityImpl authority = new GrantedAuthorityImpl(roleName);
				// return authority;
				return role;
			}
		});
	}
	
}
