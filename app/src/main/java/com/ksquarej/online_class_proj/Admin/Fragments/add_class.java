package com.ksquarej.online_class_proj.Admin.Fragments;

public class add_class {
    String id,email;

    public add_class(String id,String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
