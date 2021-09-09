package com.example.wpossbank.Modelos;

import com.example.wpossbank.database.Database;

public class Admin {
    private String id = "1";
    private String email = "carlos@wposs.com";
    private String password = "123456";
    private int balance = 0;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

}
