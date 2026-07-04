package com.example.bankapplication.models;

public class User {

    private String uid;
    private String id;
    private String name;
    private String email;
    private String phone;
    private double balance;

    public User() {}

    public User(String uid, String id, String name, String email, String phone, double balance) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
    }

    public String getUid() { return uid; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
}