package com.example.midemo.bean;

public class UserBean {
    private int id;
    private String username;
    private String password;

    // 构造函数
    public UserBean() {
    }

    public UserBean(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // getter 和 setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
