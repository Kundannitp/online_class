package com.ksquarej.online_class_proj.Teachers.Fragmets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.ksquarej.online_class_proj.Admin.Fragments.add_class;
import com.ksquarej.online_class_proj.Admin.Fragments.commonAdapter;
import com.ksquarej.online_class_proj.R;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    private ArrayList<Student_class> list1;
    Context context;
    String course_id,organisation_id,teacher_id;
    boolean val;

    public StudentAdapter(Context context, ArrayList<Student_class> list1,String course_id,String organisation_id,String teacher_id,boolean val) {
        this.context = context;
        this.list1 = list1;
        this.course_id=course_id;
        this.organisation_id=organisation_id;
        this.teacher_id=teacher_id;
        this.val=val;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personemail,person_id;
        CircleImageView personimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personemail = itemView.findViewById(R.id.cardView_commodityName);
            person_id=itemView.findViewById(R.id.cardView_people_id);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_people, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        Student_class obj=list1.get(i);
        viewHolder.personemail.setText(obj.getEmail());
        viewHolder.person_id.setText(obj.getId());
        if(val) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("organisation").child(organisation_id).child("teachers")
                            .child(teacher_id).child("courses").child(course_id).child("students").push().setValue(obj);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return list1.size();
    }

    public void updateList(ArrayList<Student_class> list){
        this.list1 = list;
        notifyDataSetChanged();
    }

}
