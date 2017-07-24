package org.apache.struts2.shiro.example.model;

import java.util.ArrayList;
import java.util.List;

public class Role {

    private String name;
    private List<String> permissions = new ArrayList<>();   

    public Role(String name, List<String> permissions)
    {
        this.name = name;
        if (permissions != null)
        {
            this.permissions = permissions;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the permissions
     */
    public List<String> getPermissions()
    {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(List<String> permissions)
    {
        this.permissions = permissions;
    }

    
}
