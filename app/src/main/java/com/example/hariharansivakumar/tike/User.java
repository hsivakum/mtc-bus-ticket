package com.example.hariharansivakumar.tike;

/**
 * Created by Hariharan Sivakumar on 3/27/2018.
 */

public class User {

    private int id;
    private String username, email;
    private Long mobile;

    public User(int id,String username, String email, Long mobile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getMobile() {
        return mobile;
    }
}
