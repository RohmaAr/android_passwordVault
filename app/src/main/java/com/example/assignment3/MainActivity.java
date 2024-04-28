package com.example.assignment3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    View loginView, signupView;

    Button btnLLogin, btnSLogin, btnLSignup, btnSSignup;
    TextInputEditText etLUsername, etLPassword, etSUsername, etSPassword, etSConfirmPassword;
    Fragment fragLogIn,fragSignUp;
    User user;

    TextView title,userInitials;
    ImageView ivback,ivmore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //Tool bar setting
        title=findViewById(R.id.toolbartitle);
        title.setText((String)"Password Vault");
        ivback=findViewById(R.id.ivtoolbarback);
        ivmore=findViewById(R.id.toolbarmore);
        userInitials=findViewById(R.id.toolbaruser);
        ivmore.setVisibility(View.GONE);
        ivback.setVisibility(View.GONE);
        userInitials.setVisibility(View.GONE);


        //to see whats in the db
        CredentialDB db=new CredentialDB(this);
        db.open();
        db.getAllDb();
        db.close();



        btnLSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction()
                        .hide(fragLogIn)
                        .show(fragSignUp)
                        .commit();
            }
        });

        btnSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction()
                        .show(fragLogIn)
                        .hide(fragSignUp)
                        .commit();
            }
        });
        btnLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                String us,pas;
                us= Objects.requireNonNull(etLUsername.getText()).toString().trim();
                pas= Objects.requireNonNull(etLPassword.getText()).toString().trim();
                if(us.isEmpty() || pas.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                user= new User(-1,pas,us);
               if( db.logIn(user)==-1)
               {
                   db.close();
                   Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
               }else {
                   db.close();
                   Intent intent=new Intent(MainActivity.this, Home.class);
                   intent.putExtra("user",user);
                   startActivity(intent);
                   finish();
               }
            }
        });
        btnSSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                String us,pas,conpas;
                us= Objects.requireNonNull(etSUsername.getText()).toString().trim();
                pas= Objects.requireNonNull(etSPassword.getText()).toString().trim();
                conpas=Objects.requireNonNull(etSConfirmPassword.getText()).toString().trim();

                if(us.isEmpty() || pas.isEmpty() ||conpas.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!conpas.equals(pas))
                {
                    Toast.makeText(MainActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                user= new User(-1,pas,us);
                if(db.signUp(user)!=-1){
                    db.close();
                    Toast.makeText(MainActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    btnSLogin.performClick();
                }
                else {
                    db.close();
                }
            }
        });
    }
    public void init(){
        manager=getSupportFragmentManager();
        loginView= Objects.requireNonNull(manager.findFragmentById(R.id.fragLogin)).requireView();
        signupView= Objects.requireNonNull(manager.findFragmentById(R.id.fragSignup)).requireView();
        btnLSignup=loginView.findViewById(R.id.btnLSignup);
        btnLLogin=loginView.findViewById(R.id.btnLLogin);
        btnSLogin=signupView.findViewById(R.id.btnSLogin);
        btnSSignup=signupView.findViewById(R.id.btnSSignup);

        etLPassword=loginView.findViewById(R.id.etLPassword);
        etLUsername=loginView.findViewById(R.id.etLUsername);
        etSUsername=signupView.findViewById(R.id.etSUsername);
        etSPassword=signupView.findViewById(R.id.etSPassword);
        etSConfirmPassword=signupView.findViewById(R.id.etSConfirmPassword);

        fragSignUp=manager.findFragmentById(R.id.fragSignup);
        fragLogIn=manager.findFragmentById(R.id.fragLogin);

    }
}