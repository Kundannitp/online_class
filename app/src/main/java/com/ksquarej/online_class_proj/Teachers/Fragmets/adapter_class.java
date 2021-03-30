package com.ksquarej.online_class_proj.Teachers.Fragmets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksquarej.online_class_proj.R;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_class  extends RecyclerView.Adapter<adapter_class.MyViewHolder> {
    private ArrayList<add_course> list1;
    Context context;

    public adapter_class(Context context, ArrayList<add_course> list1) {
        this.context = context;
        this.list1 = list1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course_name,course_id;
        CircleImageView personimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.cardView_course_name);
            course_id = itemView.findViewById(R.id.cardView_course_id);
        }
    }

    @NonNull
    @Override
    public adapter_class.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_courses, viewGroup, false);
        return new adapter_class.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_class.MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        add_course obj=list1.get(i);
        viewHolder.course_name.setText(obj.getCourse_name());
        viewHolder.course_id.setText(obj.getCourse_id());
    }
    @Override
    public int getItemCount() {
        return list1.size();
    }

}
