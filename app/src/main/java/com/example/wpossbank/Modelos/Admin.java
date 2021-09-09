package com.example.wpossbank.Modelos;

public class Admin {
    private String id = "0";
    private String email = "carlos@wposs.com";
    private String password = "123456";
    private String balance = "0";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }
}
