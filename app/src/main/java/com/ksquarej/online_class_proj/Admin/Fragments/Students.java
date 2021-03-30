package com.ksquarej.online_class_proj.Admin.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksquarej.online_class_proj.Admin.HomeAdmin;
import com.ksquarej.online_class_proj.R;

import java.util.ArrayList;

public class Students extends Fragment {
    View v;
    Context mContext;
    String org_id="";
    RecyclerView mRecyclerView;

    public Students(){

    }
    public Students(String org_id){
        this.org_id=org_id;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.admin_students, container, false);

        ((HomeAdmin) getActivity()).getSupportActionBar().setTitle("Students");

        mContext=getContext();

        FloatingActionButton fab = v.findViewById(R.id.add_students);


        mRecyclerView = v.findViewById(R.id.students_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<add_class> student_lst=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    student_lst.add(new add_class(ds.child("email").getValue().toString()));
                }
                commonAdapter commonAdapter=new commonAdapter(getContext(),student_lst);
                mRecyclerView.setAdapter(commonAdapter);
                ProgressBar progressBar = v.findViewById(R.id.progress_circle);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(view -> {
            final Dialog customView = new Dialog(mContext);
            customView.setContentView(R.layout.add_teachers);
            customView.setTitle("Add Teachers");
//            customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//            customView.getWindow().setGravity(Gravity.BOTTOM);

            // Get a reference for the custom view close button
            ImageButton closeButton = customView.findViewById(R.id.ib_close);
            closeButton.setOnClickListener(v -> customView.dismiss());
            Button btn = customView.findViewById(R.id.add_teacher_btn);
            EditText email_teacher=customView.findViewById(R.id.email_teacher);
            TextView title_value=customView.findViewById(R.id.title_val);
            title_value.setText("Add Students");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!email_teacher.getText().toString().equals("")) {
                        DatabaseReference m_ref = FirebaseDatabase.getInstance().getReference();
                        m_ref.child("organisation").child(org_id).child("students").push().setValue(new add_class(email_teacher.getText().toString()));
                        customView.dismiss();
                    }
                }
            });
            customView.show();
        });

        return v;
    }
}
