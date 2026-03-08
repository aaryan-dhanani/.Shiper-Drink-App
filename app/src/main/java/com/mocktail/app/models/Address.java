package com.mocktail.app.models;

public class Address {
    private String name;       // Home, Office, Gym
    private String type;       // home, office, gym
    private String line1;
    private String line2;
    private boolean isDefault;

    public Address(String name, String type, String line1, String line2, boolean isDefault) {
        this.name = name;
        this.type = type;
        this.line1 = line1;
        this.line2 = line2;
        this.isDefault = isDefault;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLine1() { return line1; }
    public void setLine1(String line1) { this.line1 = line1; }

    public String getLine2() { return line2; }
    public void setLine2(String line2) { this.line2 = line2; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}
