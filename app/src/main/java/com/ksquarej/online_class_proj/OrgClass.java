package com.ksquarej.online_class_proj;

public class OrgClass {
    String org_name,admin_email;

    public OrgClass(String org_name, String admin_email) {
        this.org_name = org_name;
        this.admin_email = admin_email;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }
}
