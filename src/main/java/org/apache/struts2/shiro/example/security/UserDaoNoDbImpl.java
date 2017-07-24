package org.apache.struts2.shiro.example.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.struts2.shiro.example.model.Role;
import org.apache.struts2.shiro.example.model.User;

public class UserDaoNoDbImpl implements UserDao
{
    private static Map<String, Role> rolesMap;
    private static ArrayList<User> users;
    static
    {
        // Initialize roles DAO and get the static rolesMap from it
        RoleDao rolesDao = new RoleDaoNoDbImpl();
        rolesMap = rolesDao.getAllRoles();
        
        // Initialize the static users field & encrypt/hash password
        users = new ArrayList<>();
        users.add(new User("root", new Sha256Hash("secret").toString(), Arrays.asList(rolesMap.get("admin"))));
        users.add(new User("guest", new Sha256Hash("guest").toString(), Arrays.asList(rolesMap.get("guest"))));
        users.add(new User("presidentskroob", new Sha256Hash("12345").toString(), Arrays.asList(rolesMap.get("president"))));
        users.add(new User("darkhelmet", new Sha256Hash("ludicrousspeed").toString(), Arrays.asList(rolesMap.get("darklord"), rolesMap.get("schwartz"))));
        users.add(new User("lonestarr", new Sha256Hash("vespa").toString(), Arrays.asList(rolesMap.get("goodguy"), rolesMap.get("schwartz"))));            
    }

    public UserDaoNoDbImpl()
    {
        // No implementation, all initialization is done in static initializer block
    }

    public User getUser(String username)
    {
        User user = null;
        Iterator<User> iter = users.iterator();
        boolean found = false;
        while (! found && iter.hasNext())
        {
            user = iter.next();
            if (user.getUsername().equals(username))
            {
                found = true;
            }
        }
        
        return user;
    }
    
    public boolean isUserValid(String username, String password)
    {
        boolean result = false;
        User user = getUser(username);
        
        if (user != null && user.getUsername().equals(username) && user.getPassword().equals(new Sha256Hash(password).toString()))
        {
            result = true;
        }
            
        return result;
    }

    @Override
    public List<Role> lookupAllRolesForUser(String username)
    {
        return getUser(username).getRoles();
    }

}