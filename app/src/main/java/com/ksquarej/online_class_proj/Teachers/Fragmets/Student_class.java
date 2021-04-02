package com.ksquarej.online_class_proj.Teachers.Fragmets;

public class Student_class {
    String id,email;

    public Student_class(String id,String email) {
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
