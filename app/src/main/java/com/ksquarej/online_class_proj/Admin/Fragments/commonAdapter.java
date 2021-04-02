package com.ksquarej.online_class_proj.Admin.Fragments;

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

public class commonAdapter  extends RecyclerView.Adapter<commonAdapter.MyViewHolder> {
    private ArrayList<add_class> list1;
    Context context;

    public commonAdapter(Context context, ArrayList<add_class> list1) {
        this.context = context;
        this.list1 = list1;
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
        add_class obj=list1.get(i);
        viewHolder.personemail.setText(obj.getEmail());
        viewHolder.person_id.setText(obj.getId());
    }
    @Override
    public int getItemCount() {
        return list1.size();
    }

}
