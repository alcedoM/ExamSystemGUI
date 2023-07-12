package com.examsys.modules;

import com.alibaba.fastjson.annotation.JSONField;

public class User {
    @JSONField(name = "ID")
    private String id;
    @JSONField(name = "PASSWORD")
    private String password;
    @JSONField(name = "IS_ADMIN")
    private int is_admin;

    public User(String id,String password, int is_admin) {
        this.id =id;
        this.password = password;
        this.is_admin = is_admin;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }
}
