package org.apache.struts2.shiro.example.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.struts2.shiro.example.model.Role;
import org.apache.struts2.shiro.example.model.User;

/**
 * The application's configured Apache Shiro Realm.
 *
 */
public class ShiroRealm extends AuthorizingRealm 
{

	private SecurityService securityService;
	
    private static final transient Logger log = LogManager.getLogger(ShiroRealm.class);
	
    /**
     * Retrieves all the roles for the given user, "guest" role, or null
     * 
     * @param user
     * @return a list of roles for the given user, or "guest" role if user is not in the system, 
     *         or null if given user is null.
     */
    private List<String> retrieveRolesForUser(User user)
    {
        List<String> roles = null;
        
        if (user != null)
        {
            roles = new ArrayList<>();
            // Only try to lookup roles if user is valid  user, otherwise assume default guest role
            if (securityService.isUserValid(user.getUsername(), user.getPassword()))
            {
                List<Role> roleList = securityService.lookupRolesForUser(user.getUsername());
                // Need role name only to add to list
                for (Role role : roleList)
                {
                    roles.add(role.getName());
                }
            }
            else
            {
                roles.add("guest");
            }            
        }
        
        return roles;
    }
    
    /**
     * Retrieves all the permissions for the given user,  or null.
     * NOTE - This method can/should return all permissions associated directly with a role (Implicit Roles)
     *        or it can/should return all permissions associated directly with a user (Explicit Roles)
     *        depending on the implementation. This implementation uses Implicit Roles
     * 
     * @param user
     * @return a list of permissions for the given user, or null if given user is null.
     */
    private Set<String> retrievePermissionsForUser(User user)
    {
        Set<String> permissions = null;
        
        if (user != null)
        {
            // In this user/data model, permissions are only assigned to roles and not directly to users,
            // so we must retrieve the roles for the user to get the permissions.
            List<Role> roles = securityService.lookupRolesForUser(user.getUsername());
            for (Role role : roles) 
            {
                if (! role.getPermissions().isEmpty())
                {
                    if (permissions != null)
                    {
                        permissions.addAll(role.getPermissions());
                    }
                    else
                    {
                        permissions = new HashSet<>(role.getPermissions());
                    }                    
                }                   
            }
        }

        return permissions;
    }
    
	/**
	 * @param authcToken Has user name and password
	 * @return return the SimpleAuthenticationInfo object, otherwise return null.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) 
	{
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;		
		SimpleAuthenticationInfo authcInfo = null;
        
        try 
        {
        	User user = null;
        	
        	log.debug("In method 'doGetAuthenticationInfo' authenticating " + token.getUsername());
        	if (securityService.isUserValid(token.getUsername(), String.valueOf(token.getPassword())))
        	{                
            	user = new User();
            	user.setUsername(token.getUsername());
            	user.setPassword(String.valueOf(token.getPassword()));
            	authcInfo = new SimpleAuthenticationInfo(user, token.getPassword(), getName());        	
        	}
        	else 
        	{
        		throw new AuthenticationException("Invalid username and/or password. User cannot be found."); 
        	}
        } 
        catch (Exception e) 
        {
        	log.error("Error authenticating user: " + token.getUsername(), e);
		}
       
        return authcInfo;
	}

	/**
	 * @param principals Has user data object.
	 * @return If the user has roles for this application,
	 * return the AuthorizationInfo object. Otherwise return null. 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) 
	{
		SimpleAuthorizationInfo authzInfo = null;
		
		try 
		{			
			if (principals != null)
			{
	            User user = (User) principals.fromRealm(getName()).iterator().next();
	            
	            log.debug("In method 'doGetAuthorizationInfo()' authorizing " + user.getUsername());
	            authzInfo = new SimpleAuthorizationInfo();
	            
	            List<String> roles = retrieveRolesForUser(user);
	            
	            // Add roles to shiro subject.
	            authzInfo.addRoles(roles);	            
	            
	            Set<String> permissions = retrievePermissionsForUser(user);
	            
                // Add permissions to shiro subject.
	            authzInfo.setStringPermissions(permissions);
			}
			else
			{
				throw new AuthorizationException("Cannot authorize with no principals.");
			}

			
		} 
		catch (Exception e) 
		{
			log.error(e, e);
		}
		
		return authzInfo;
	}
	
	public void setSecurityService(SecurityService securityService) 
	{
		this.securityService = securityService;
	}
}
