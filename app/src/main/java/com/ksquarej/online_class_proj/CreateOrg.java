package com.ksquarej.online_class_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ksquarej.online_class_proj.Admin.HomeAdmin;
import com.ksquarej.online_class_proj.Teachers.TeacherHome;

public class CreateOrg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_org);
        EditText org_name=findViewById(R.id.organisation_name);
        Button create_org=findViewById(R.id.create_org);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CreateOrg.this);
        create_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference m_ref=FirebaseDatabase.getInstance().getReference();
                if (org_name.getText().toString() != "") {
                    String key=m_ref.push().getKey();
                    m_ref.child("organisation").child(key).setValue(new OrgClass(org_name.getText().toString(),acct.getEmail()));
                    Intent i3=new Intent(CreateOrg.this, HomeAdmin.class);
                    i3.putExtra("org_name",org_name.getText().toString());
                    i3.putExtra("org_id",key);
                    startActivity(i3);
                    finish();
                }
            }
        });
    }
}
