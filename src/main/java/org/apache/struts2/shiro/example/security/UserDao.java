package org.apache.struts2.shiro.example.security;

import java.util.List;

import org.apache.struts2.shiro.example.model.Role;
import org.apache.struts2.shiro.example.model.User;

public interface UserDao
{
    User getUser(String username);   
    boolean isUserValid(String username, String password);
    List<Role> lookupAllRolesForUser(String username);
}
