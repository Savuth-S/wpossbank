package com.example.wpossbank.Modelos;

public class Admin {
    private String id = "0";
    private String email = "carlos@wposs.com";
    private String password = "123456";
    private String balance = "0";
    private String name = "Usuario Usuario";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getCc() { return cc; }
    public void setCc(String cc) { this.cc = cc; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Admin() { }
}
