package org.apache.struts2.shiro.example.security;

import java.util.List;

import org.apache.struts2.shiro.example.model.Role;
import org.apache.struts2.shiro.example.model.User;



public class SecurityServiceImpl implements SecurityService
{
    private UserDao userDao = null;
    
    public User getUser(String username) 
    {
        return userDao.getUser(username);
    }
    
    public boolean isUserValid(String username, String password)
    {
        return userDao.isUserValid(username, password);        
    }

    public List<Role> lookupRolesForUser(String username)
    {
    	return userDao.lookupAllRolesForUser(username);
    }
    
    // DAO getters and setters
    public UserDao getUserDao() 
    {
        return userDao;
    }
    
    public void setUserDao(UserDao userDao) 
    {
        this.userDao = userDao;
    }

}
