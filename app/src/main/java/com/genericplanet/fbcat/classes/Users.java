package com.genericplanet.fbcat.classes;

public class Users {

    private String id,username,name;

    public Users(String id,String username,String name){
        this.name=name;
        this.id=id;
        this.username=username;
    }
    public Users()
    {

    }

    public void setid(String id) {
        this.id = id;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public String getusername() {
        return username;
    }

    public String getname() {
        return name;
    }
}
