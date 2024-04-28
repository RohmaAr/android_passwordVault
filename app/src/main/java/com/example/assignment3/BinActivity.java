package com.example.assignment3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class BinActivity extends AppCompatActivity {

    ListView deletedList;
    ImageView ivback;
    TextView title,userInitials,tvnorecords;
    User user;
    ArrayAdapter adapter;
    ArrayList<Credential> deletedCredentials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);
        title=findViewById(R.id.toolbartitle);
        title.setText((String)"Bin");
        ivback=findViewById(R.id.ivtoolbarback);
        tvnorecords=findViewById(R.id.binnorecord);
        deletedList=findViewById(android.R.id.list);

        Intent intent=getIntent();
        user= (User) intent.getSerializableExtra("user");

        userInitials=findViewById(R.id.toolbaruser);
        userInitials.setText(user.getUsername().substring(0,2).toUpperCase());


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CredentialDB db=new CredentialDB(this);
        db.open();
        deletedCredentials=db.getDeletedUserCredentials(user);
        db.close();
        if(deletedCredentials.isEmpty())
        {
            tvnorecords.setVisibility(View.VISIBLE);
        }
        for(Credential cr:deletedCredentials){
            System.out.println("in Bin call "+cr.getUrl()+" "+cr.getUsername()+" "+cr.getPassword()+" "+cr.getId());
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,android.R.id.text1, deletedCredentials) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Credential credential=(Credential) getItem(position);
                System.out.println("IN THE ADAPTER "+credential.getId()+" "+credential.getUsername()+" "+credential.getPassword()+" "+credential.getUrl()+" ");
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        db.open();
                        db.restore(credential);
                        db.close();
                        deletedCredentials.remove(credential);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(BinActivity.this,"Restored credential"+credential.getUrl()+" "+ credential.getUsername(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });


                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(credential.getUsername());
                text2.setText(credential.getUrl());

                text1.setTextColor(Color.WHITE);
                text2.setTextColor(Color.GRAY);
                return view;
            }

        };
        deletedList.setAdapter(adapter);
    }
}