package com.ksquarej.online_class_proj.Students.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ksquarej.online_class_proj.R;
import com.ksquarej.online_class_proj.Students.StudentHome;

public class Classes_students extends Fragment {
    View v;
    String org_id="";

    public Classes_students(){

    }

    public Classes_students(String org_id){
        this.org_id=org_id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.courses, container, false);

        ((StudentHome) getActivity()).getSupportActionBar().setTitle("Courses");
        return v;
    }
}
