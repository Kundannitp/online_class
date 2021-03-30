package com.ksquarej.online_class_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksquarej.online_class_proj.Admin.HomeAdmin;
import com.ksquarej.online_class_proj.Students.StudentHome;
import com.ksquarej.online_class_proj.Teachers.TeacherHome;

public class MainActivity extends AppCompatActivity {

    Button googlesign;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    Context context=MainActivity.this;
    DatabaseReference d_ref;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        d_ref=FirebaseDatabase.getInstance().getReference().child("organisation");

        googlesign=findViewById(R.id.sign_in_button);
        googlesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }




    protected void onStart() {
        super.onStart();
        //new BuildNotification(this).buildNotification("Main Activity");
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if(user!=null){
            try {
                valueEventListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        Toast.makeText(MainActivity.this, "data", Toast.LENGTH_SHORT).show();
                        Log.i("data",dataSnapshot.toString());
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Log.i("entered",ds.child("admin_email").getValue().toString());
                            Log.i("entered_get",user.getEmail());
                            if(ds.child("admin_email").getValue().toString().equals(user.getEmail())){
                                Log.i("entered_if",ds.child("admin_email").getValue().toString());
                                Intent i2=new Intent(MainActivity.this,HomeAdmin.class);
                                i2.putExtra("org_name",ds.child("org_name").getValue().toString());
                                i2.putExtra("org_id",ds.getKey());
                                startActivity(i2);
                                finish();
                                break;
                            }
                            try{
                                boolean abc=false;
                                for (DataSnapshot d_teach: ds.child("teachers").getChildren()){
                                    if(d_teach.child("email").getValue().toString().equals(user.getEmail())){
                                        Intent i3=new Intent(MainActivity.this, TeacherHome.class);
                                        i3.putExtra("org_name",ds.child("org_name").getValue().toString());
                                        i3.putExtra("org_id",ds.getKey());
                                        i3.putExtra("teach_id",d_teach.getKey());
                                        startActivity(i3);
                                        finish();
                                        abc=true;
                                        break;
                                    }
                                }
                                if(abc) break;
                            }catch(Exception e){

                            }
                            try{
                                boolean abd=false;
                                for (DataSnapshot d_teach: ds.child("students").getChildren()){
                                    if(d_teach.child("email").getValue().toString().equals(user.getEmail())){
                                        Intent i4=new Intent(MainActivity.this, StudentHome.class);
                                        i4.putExtra("org_name",ds.child("org_name").getValue().toString());
                                        i4.putExtra("org_id",ds.getKey());
                                        startActivity(i4);
                                        finish();
                                        abd=true;
                                    }
                                }
                                if(abd) break;
                            }catch(Exception e){

                            }
                        }

//                        Toast.makeText(MainActivity.this, "Sorry you are not part of organisation", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                };
                d_ref.addValueEventListener(valueEventListener);
//                d_ref.child("organisation").removeEventListener(valueEventListener);
            }catch(Exception e){
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (d_ref != null && valueEventListener != null) {
            d_ref.removeEventListener(valueEventListener);
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Some error occur", Toast.LENGTH_SHORT).show();
        }
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Sign In"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                            try {
                                valueEventListener=new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        Toast.makeText(MainActivity.this, "data", Toast.LENGTH_SHORT).show();
                                        boolean value_check=true;
                                        Log.i("data",dataSnapshot.toString());
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            Log.i("entered",ds.child("admin_email").getValue().toString());
                                            Log.i("entered_get",user.getEmail());
                                            if(ds.child("admin_email").getValue().toString().equals(user.getEmail())){
                                                Log.i("entered_if",ds.child("admin_email").getValue().toString());
                                                value_check=false;
                                                Intent i2=new Intent(MainActivity.this,HomeAdmin.class);
                                                i2.putExtra("org_name",ds.child("org_name").getValue().toString());
                                                i2.putExtra("org_id",ds.getKey());
                                                progressDialog.dismiss();
                                                startActivity(i2);
                                                finish();
                                                break;
                                            }
                                            try{
                                                boolean abc=false;
                                                for (DataSnapshot d_teach: ds.child("teachers").getChildren()){
                                                    if(d_teach.child("email").getValue().toString().equals(user.getEmail())){
                                                        value_check=false;
                                                        Intent i3=new Intent(MainActivity.this, TeacherHome.class);
                                                        i3.putExtra("org_name",ds.child("org_name").getValue().toString());
                                                        i3.putExtra("org_id",ds.getKey());
                                                        i3.putExtra("teach_id",d_teach.getKey());
                                                        progressDialog.dismiss();
                                                        startActivity(i3);
                                                        finish();
                                                        abc=true;
                                                        break;
                                                    }
                                                }
                                                if(abc) break;
                                            }catch(Exception e){

                                            }
                                            try{
                                                boolean abd=false;
                                                for (DataSnapshot d_teach: ds.child("students").getChildren()){
                                                    if(d_teach.child("email").getValue().toString().equals(user.getEmail())){
                                                        value_check=false;
                                                        Intent i4=new Intent(MainActivity.this, StudentHome.class);
                                                        i4.putExtra("org_name",ds.child("org_name").getValue().toString());
                                                        i4.putExtra("org_id",ds.getKey());
                                                        progressDialog.dismiss();
                                                        startActivity(i4);
                                                        finish();
                                                        abd=true;
                                                    }
                                                }
                                                if(abd) break;
                                            }catch(Exception e){

                                            }
                                        }
                                        if(value_check) {
                                            Intent i1 = new Intent(MainActivity.this, CreateOrg.class);
                                            startActivity(i1);
                                            progressDialog.dismiss();
                                            finish();
                                        }
//                                        Toast.makeText(MainActivity.this, "Sorry you are not part of organisation", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                };
                                d_ref.addValueEventListener(valueEventListener);
//                                d_ref.child("organisation").removeEventListener(valueEventListener);
                            }catch(Exception e){
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Intent i1=new Intent(MainActivity.this,CreateOrg.class);
                                startActivity(i1);
                                progressDialog.dismiss();
                                finish();
                            }
//                            Intent i=new Intent(MainActivity.this,HomeAdmin.class);
//                            startActivity(i);
//                            progressDialog.dismiss();
//                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }
}
