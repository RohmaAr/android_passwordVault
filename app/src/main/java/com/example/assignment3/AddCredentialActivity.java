package com.example.assignment3;

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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddCredentialActivity extends AppCompatActivity {

    Button btadd;
    ImageView ivback;
    TextView userInitials;
    TextInputEditText etUsername,etPassword,etUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credential);
        ivback=findViewById(R.id.ivtoolbarback);
        etUsername=findViewById(R.id.etAddUsername);
        etPassword=findViewById(R.id.etAddPassword);
        etUrl=findViewById(R.id.etAddURL);

        userInitials=findViewById(R.id.toolbaruser);
        userInitials.setVisibility(View.GONE);
        btadd=findViewById(R.id.btadd);

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String username= Objects.requireNonNull(etUsername.getText()).toString().trim();
                String password= Objects.requireNonNull(etPassword.getText()).toString().trim();
                String Url= Objects.requireNonNull(etUrl.getText()).toString().trim();

                if(username.isEmpty() || password.isEmpty() || Url.isEmpty())
                    Toast.makeText(AddCredentialActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                else {
                    Credential credential = new Credential(0,username, password,Url);
                    intent.putExtra("credential", credential);
                    setResult(55, intent);
                    finish();
                }
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}