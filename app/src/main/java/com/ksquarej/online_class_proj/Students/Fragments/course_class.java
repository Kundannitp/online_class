package com.ksquarej.online_class_proj.Students.Fragments;

public class course_class {
    String course_id,course_name,flag;

    public course_class(String course_id, String course_name,String flag) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.flag=flag;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
