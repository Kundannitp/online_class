package com.ksquarej.online_class_proj.Teachers.Fragmets;

public class add_course {
    String course_name,course_id,flag="false",course_key;

    public add_course(String course_name, String course_id) {
        this.course_name = course_name;
        this.course_id = course_id;
    }

    public add_course(String course_name, String course_id, String course_key) {
        this.course_name = course_name;
        this.course_id = course_id;
        this.course_key = course_key;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCourse_key() {
        return course_key;
    }

    public void setCourse_key(String course_key) {
        this.course_key = course_key;
    }
}
