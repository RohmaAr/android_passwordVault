package com.example.assignment3;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String password,username;

    int id;
    ArrayList<Credential> credentials;
    public User(int id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }
    public void addCredentials(Credential c)
    {
        credentials.add(c);
    }
    public void setCredentials(ArrayList<Credential> credentials) {
        this.credentials = credentials;
    }
    public ArrayList<Credential> getCredentials()
    {
        return credentials;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
