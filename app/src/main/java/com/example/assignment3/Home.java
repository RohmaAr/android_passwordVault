package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    TextView tvLogin,tvBin;
    TextView title,userInitials;
    ImageView ivback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent =getIntent();
        User user= (User) intent.getSerializableExtra("user");
        //Toast.makeText(this,user.username+" "+user.password,Toast.LENGTH_SHORT).show();

        tvLogin=findViewById(R.id.tvlogin);
        tvBin=findViewById(R.id.tvHomeBin);

        //Tool bar setting
        title=findViewById(R.id.toolbartitle);
        title.setText((String)"My Vault");
        ivback=findViewById(R.id.ivtoolbarback);
        userInitials=findViewById(R.id.toolbaruser);
        userInitials.setText((String)user.getUsername().substring(0,2).toUpperCase());

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To credentials Display activity
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent=new Intent(Home.this,VaultDisplay.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        //open bin activity
        tvBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(Home.this,BinActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }

}