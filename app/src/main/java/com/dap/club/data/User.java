package com.dap.club.data;

/**
 * Created by dap on 16/1/4.
 */
public class User {

    private String id;//用户id
    private int status;//0表示匿名用户，1表示登录用户，2表示认证用户
    private String name;//用户名
    private String url;//用户头像地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
