package com.example.intelligentfarmcore.pojo.response;

public class LoginRes {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginRes{" +
                "token='" + token + '\'' +
                '}';
    }

    public LoginRes(String token) {
        this.token = token;
    }
}
