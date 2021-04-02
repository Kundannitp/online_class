package com.ksquarej.online_class_proj.Teachers.Fragmets;

import android.app.Dialog;
import android.content.Context;
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
import com.ksquarej.online_class_proj.Admin.Fragments.add_class;
import com.ksquarej.online_class_proj.Admin.Fragments.commonAdapter;
import com.ksquarej.online_class_proj.R;
import com.ksquarej.online_class_proj.Teachers.TeacherHome;

import java.util.ArrayList;

public class Classes_teachers extends Fragment {
    View v;
    Context mContext;
    String org_id="",teacher_id;
    RecyclerView mRecyclerView;
    public Classes_teachers(){

    }
    public Classes_teachers(String org_id,String teacher_id){
        this.org_id=org_id;
        this.teacher_id=teacher_id;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.courses, container, false);
        ((TeacherHome) getActivity()).getSupportActionBar().setTitle("Courses");

        mContext=getContext();

        FloatingActionButton fab = v.findViewById(R.id.add_courses);


        mRecyclerView = v.findViewById(R.id.courses_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("teachers").child(teacher_id).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<add_course> student_lst=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    student_lst.add(new add_course(ds.child("course_name").getValue().toString(),ds.child("course_id").getValue().toString(),ds.getKey()));
                }
                adapter_class adapter_class1=new adapter_class(getContext(),student_lst,org_id,teacher_id);
                mRecyclerView.setAdapter(adapter_class1);
                ProgressBar progressBar = v.findViewById(R.id.progress_circle);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(view -> {
            final Dialog customView = new Dialog(mContext);
            customView.setContentView(R.layout.add_courses);
            customView.setTitle("Add Courses");
//            customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//            customView.getWindow().setGravity(Gravity.BOTTOM);

            // Get a reference for the custom view close button
            ImageButton closeButton = customView.findViewById(R.id.ib_close);
            closeButton.setOnClickListener(v -> customView.dismiss());
            Button btn = customView.findViewById(R.id.add_courses_btn);
            EditText course_name=customView.findViewById(R.id.course_name);
            EditText course_id=customView.findViewById(R.id.course_id);
            TextView title_value=customView.findViewById(R.id.title_val);
            title_value.setText("Add Courses");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!course_name.getText().toString().equals("")&&!course_id.getText().toString().equals("")) {
                        DatabaseReference m_ref = FirebaseDatabase.getInstance().getReference();
                        m_ref.child("organisation").child(org_id).child("teachers").child(teacher_id).child("courses").push().setValue(new add_course(course_name.getText().toString(),course_id.getText().toString()));
                        customView.dismiss();
                    }
                }
            });
            customView.show();
        });

        return v;
    }
}
