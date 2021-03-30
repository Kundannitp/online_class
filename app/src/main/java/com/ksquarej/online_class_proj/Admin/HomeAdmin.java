package com.ksquarej.online_class_proj.Admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ksquarej.online_class_proj.Admin.Fragments.Students;
import com.ksquarej.online_class_proj.Admin.Fragments.Teachers;
import com.ksquarej.online_class_proj.MainActivity;
import com.ksquarej.online_class_proj.R;

public class HomeAdmin extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView personimage;
    TextView personname,personemail,personOrg,personRole;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    NavigationView navigationView;
    DrawerLayout drawer ;
    View v;
    String tokentodelete=" ";
    private ActionBarDrawerToggle mToggle;
    String orgId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_admin);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mToggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        Bundle extras = getIntent().getExtras();
        orgId=extras.getString("org_id");

        Intent intent = getIntent();
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
        setUpDrawerContent(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.nav_host_fragment, new Teachers(orgId));
        tx.commit();
    }


    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                displaySelectedScreen(menuItem.getItemId());
                return true;
            }
        });
    }

    protected void onStart() {

        super.onStart();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        personimage=hView.findViewById(R.id.patient_profile_pic);
        personname=hView.findViewById(R.id.patient_name);
        personemail=hView.findViewById(R.id.patient_email);
        personOrg=hView.findViewById(R.id.organisation_id);
        personRole=hView.findViewById(R.id.role_id);


        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();
        try {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(HomeAdmin.this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();
                Bundle extras = getIntent().getExtras();
                String org_name= extras.getString("org_name");
                String org_id=extras.getString("org_id");

                personemail.setText(personEmail);
                personname.setText(personName);
                personRole.setText("Admin");
                personOrg.setText(org_name);
                String personPhotostr = "https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
                if (personPhoto != null) {
                    personPhotostr = personPhoto.toString();
                }
                Glide.with(this)
                        .load(personPhotostr)
                        .into(personimage);

//                ProfileClass profileClass = new ProfileClass(personName, personEmail, personPhotostr);
//                mref.child("Profiles").child(acct.getId()).setValue(profileClass);
            }
        }catch (Exception e){

        }
    }


    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                getApplicationContext(),
                permission)) {
            ActivityCompat.requestPermissions(HomeAdmin.this,
                    new String[]{permission},
                    requestCode);
        }
    }


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_admin_teachers:
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                fragment = new Teachers(orgId);
                break;
            case R.id.nav_patient_logout:
                showLogoutDialog();
                break;
            case R.id.nav_admin_student:
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                fragment=new Students(orgId);
                break;
        }


        //replacing the fragment
        if (fragment != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.nav_host_fragment, fragment)
                    .commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeAdmin.this);
        builder.setMessage("Sure to logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(HomeAdmin.this);
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w( "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                updateToken(token);
                            }
                        });


                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();

                Toast.makeText(HomeAdmin.this, "Successfully signed out", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(HomeAdmin.this, MainActivity.class));
                finish();

                dialog.cancel();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();

    }

    void updateToken(String token){
        this.tokentodelete=token;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
