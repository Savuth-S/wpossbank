package com.example.wpossbank.Modelos;

public class User {
    private String id = "0";
    private String pin = "1234";
    private String cc = "1234";
    private int balance = 0;
    private String name = "Usuario Usuario";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getCc() { return cc; }
    public void setCc(String cc) { this.cc = cc; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
