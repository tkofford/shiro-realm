package org.apache.struts2.shiro.example.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.shiro.example.model.Role;

public class RoleDaoNoDbImpl implements RoleDao
{
    private static Map<String, Role> roles;
    static
    {
        roles = new HashMap<>();
        roles.put("admin", new Role("admin", Arrays.asList("*")));
        roles.put("guest", new Role("guest", null));
        roles.put("president", new Role("president", null));
        roles.put("darklord", new Role("darklord", null));
        roles.put("schwartz", new Role("schwartz", Arrays.asList("lightsaber:*")));
        roles.put("goodguy", new Role("goodguy", Arrays.asList("winnebago:drive:eagle5")));            
    }

    public RoleDaoNoDbImpl()
    {
        // No implementation, all initialization is done in static initializer block
    }

    public Map<String, Role> getAllRoles()
    {
    	return roles;
    }
    
    public Role getRole(String roleName)
    {
        return roles.get(roleName);
    }

    @Override
    public List<String> lookupAllPermissionsForRole(String roleName)
    {
        Role role = getRole(roleName);
        
        if (role != null)
        {
            return role.getPermissions();
        }
        else
        {
            return new ArrayList<>();
        }
    }

    
}
