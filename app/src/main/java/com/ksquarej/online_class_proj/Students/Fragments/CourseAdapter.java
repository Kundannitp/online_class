package com.ksquarej.online_class_proj.Students.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksquarej.online_class_proj.R;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {
    private ArrayList<course_class> list1;
    Context context;

    public CourseAdapter(Context context, ArrayList<course_class> list1) {
        this.context = context;
        this.list1 = list1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course_name,course_id;
        CircleImageView personimg;
        Button join_class;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.cardView_course_name);
            course_id = itemView.findViewById(R.id.cardView_course_id);
            join_class=itemView.findViewById(R.id.join_class_btn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_course_design, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        course_class obj=list1.get(i);
        viewHolder.course_id.setText(obj.getCourse_id());
        viewHolder.course_name.setText(obj.getCourse_name());
        if(obj.getFlag().equals("false")){
            viewHolder.join_class.setVisibility(View.GONE);
        }else{
            viewHolder.join_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(obj.getCourse_id())
                            .build();
                    JitsiMeetActivity.launch(context, options);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return list1.size();
    }
}
