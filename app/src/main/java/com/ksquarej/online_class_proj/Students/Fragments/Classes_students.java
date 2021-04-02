package com.ksquarej.online_class_proj.Students.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksquarej.online_class_proj.R;
import com.ksquarej.online_class_proj.Students.StudentHome;

import java.util.ArrayList;

public class Classes_students extends Fragment {
    View v;
    String org_id="";
    RecyclerView mRecyclerView;

    public Classes_students(){

    }

    public Classes_students(String org_id){
        this.org_id=org_id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.courses_student, container, false);

        ((StudentHome) getActivity()).getSupportActionBar().setTitle("Courses");
        mRecyclerView = v.findViewById(R.id.courses_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("teachers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<course_class> student_data=new ArrayList<>();
                        for(DataSnapshot teachers:dataSnapshot.getChildren()){
                            for(DataSnapshot courses:teachers.child("courses").getChildren()){
                                for(DataSnapshot students:courses.child("students").getChildren()){
                                    if(students.child("email").getValue().toString().trim().equals(acct.getEmail())){
                                        student_data.add(new course_class(courses.child("course_id").getValue().toString(),courses.child("course_name").getValue().toString(),courses.child("flag").getValue().toString()));
                                        break;
                                    }
                                }
                            }
                        }
                        CourseAdapter m_adapter=new CourseAdapter(getContext(),student_data);
                        mRecyclerView.setAdapter(m_adapter);
                        ProgressBar progressBar = v.findViewById(R.id.progress_circle);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return v;
    }
}
