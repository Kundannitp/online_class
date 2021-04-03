package com.ksquarej.online_class_proj.Teachers.Fragmets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ksquarej.online_class_proj.MainActivity;
import com.ksquarej.online_class_proj.R;
import com.ksquarej.online_class_proj.Teachers.TeacherHome;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_class  extends RecyclerView.Adapter<adapter_class.MyViewHolder> {
    private ArrayList<add_course> list1;
    Context context;
    String org_id="",teacher_id="";
    StudentAdapter class_adapter_add;
    ArrayList<Student_class> students_arr;

    public adapter_class(Context context, ArrayList<add_course> list1,String org_id,String teacher_id) {
        this.context = context;
        this.list1 = list1;
        this.org_id=org_id;
        this.teacher_id=teacher_id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course_name,course_id;
        CircleImageView personimg;
        Button add_student,view_student,start_class,end_class;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.cardView_course_name);
            course_id = itemView.findViewById(R.id.cardView_course_id);
            add_student=itemView.findViewById(R.id.add_student_btn);
            view_student=itemView.findViewById(R.id.view_student_btn);
            start_class=itemView.findViewById(R.id.start_class_btn);
            end_class=itemView.findViewById(R.id.end_class_btn);
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
        viewHolder.add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customView = new Dialog(context);
                customView.setContentView(R.layout.add_student_to_course);
                customView.setTitle("dialog box");
                customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//                customView.getWindow().setGravity(Gravity.BOTTOM);
                RecyclerView mRecyclerView = customView.findViewById(R.id.live_update_recycler);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                FloatingActionButton fab = customView.findViewById(R.id.scroll_to_top);
                fab.setOnClickListener(view -> {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView
                            .getLayoutManager();
                    layoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
                });

                EditText searchField=customView.findViewById(R.id.searchview);

                searchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) { }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // filter your list from your input
                        String text=s.toString();
                        ArrayList<Student_class> liveClassestemp = new ArrayList();
                        for(int k=0;k<students_arr.size();k++){
                            //or use .equal(text) with you want equal match
                            //use .toLowerCase() for better matches
                            if(students_arr.get(k).getId().trim().toLowerCase().contains(text)){
                                liveClassestemp.add(students_arr.get(k));
                            }
                        }
                        //update recyclerview
                        class_adapter_add.updateList(liveClassestemp);
                    }
                });

                FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("students")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<Student_class> st_arr=new ArrayList<>();
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    st_arr.add(new Student_class(ds.child("id").getValue().toString(),ds.child("email").getValue().toString()));
                                }
                                StudentAdapter adapter=new StudentAdapter(context,st_arr,obj.getCourse_key(),org_id,teacher_id,true);
                                mRecyclerView.setAdapter(adapter);
                                update_arr(adapter,st_arr);
                                ProgressBar progressBar = customView.findViewById(R.id.progress_circle2);
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                customView.show();
            }
        });


        viewHolder.view_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customView = new Dialog(context);
                customView.setContentView(R.layout.add_student_to_course);
                customView.setTitle("dialog box");
                customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//                customView.getWindow().setGravity(Gravity.BOTTOM);
                RecyclerView mRecyclerView = customView.findViewById(R.id.live_update_recycler);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                FloatingActionButton fab = customView.findViewById(R.id.scroll_to_top);
                fab.setOnClickListener(view -> {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView
                            .getLayoutManager();
                    layoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
                });

                EditText searchField=customView.findViewById(R.id.searchview);

                searchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) { }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // filter your list from your input
                        String text=s.toString();
                        ArrayList<Student_class> liveClassestemp = new ArrayList();
                        for(int k=0;k<students_arr.size();k++){
                            //or use .equal(text) with you want equal match
                            //use .toLowerCase() for better matches
                            if(students_arr.get(k).getId().trim().toLowerCase().contains(text)){
                                liveClassestemp.add(students_arr.get(k));
                            }
                        }
                        //update recyclerview
                        class_adapter_add.updateList(liveClassestemp);
                    }
                });

                FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("teachers").child(teacher_id).child("courses").child(obj.getCourse_key()).child("students")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<Student_class> st_arr=new ArrayList<>();
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    st_arr.add(new Student_class(ds.child("id").getValue().toString(),ds.child("email").getValue().toString()));
                                }
                                StudentAdapter adapter=new StudentAdapter(context,st_arr,obj.getCourse_key(),org_id,teacher_id,true);
                                mRecyclerView.setAdapter(adapter);
                                update_arr(adapter,st_arr);
                                ProgressBar progressBar = customView.findViewById(R.id.progress_circle2);
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                customView.show();
            }
        });


        viewHolder.start_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("teachers")
                        .child(teacher_id).child("courses").child(obj.getCourse_key()).child("flag").setValue("true");
                //class starts here
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(obj.getCourse_id())
                        .build();
                JitsiMeetActivity.launch(context, options);
            }
        });

        viewHolder.end_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog(obj);
            }
        });
    }

    public void update_arr(StudentAdapter st,ArrayList<Student_class> s_arr){
        this.class_adapter_add=st;
        this.students_arr=new ArrayList<>(s_arr);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }


    private void showLogoutDialog(add_course obj)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Want to end the class?");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("organisation").child(org_id).child("teachers")
                        .child(teacher_id).child("courses").child(obj.getCourse_key()).child("flag").setValue("false");
                dialog.cancel();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
