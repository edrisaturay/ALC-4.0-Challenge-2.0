package com.edrisa.travelmantics.models;

public class Users {
    private String user_full_name;
    private String user_email;
    private String user_password;

    public Users(String user_full_name, String user_email, String user_password) {
        this.user_full_name = user_full_name;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public Users(String user_full_name, String user_email) {
        this.user_full_name = user_full_name;
        this.user_email = user_email;
    }

}
