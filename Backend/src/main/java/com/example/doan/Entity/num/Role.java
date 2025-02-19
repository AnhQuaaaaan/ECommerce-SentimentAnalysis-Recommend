package com.example.doan.Entity.num;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
