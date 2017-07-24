package org.apache.struts2.shiro.example.model;

import java.util.List;

public class User
{
    private String username;
    private String password;
    private List<Role> roles;


    public User()
    {
    }

    public User(String username, String password, List<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<Role> getRoles()
    {
        return roles;
    }
    
    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    } 
    
}
