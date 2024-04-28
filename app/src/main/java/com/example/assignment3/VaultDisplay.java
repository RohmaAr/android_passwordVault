package com.example.assignment3;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VaultDisplay extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    User user;
    FloatingActionButton fabAdd;
    TextView title,userInitials;
    ImageView ivback;

    ActivityResultLauncher<Intent> activitylauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult o) {
                    Log.d(TAG ,"onActivityResult();");
                    if(o.getResultCode()==55)
                    {
                        Intent intent=o.getData();
                        if(intent!=null){
                            Credential credential = (Credential) intent.getSerializableExtra("credential");
                            if(credential!=null){
                                credential.setId(user.getId());
                                CredentialDB credentialDB=new CredentialDB(VaultDisplay.this);
                                credentialDB.open();
                                credentialDB.insertCredential(credential);
                                user.getCredentials().add(credential);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_display);
        listView = findViewById(android.R.id.list);
        fabAdd=findViewById(R.id.fabAdd);
        title=findViewById(R.id.toolbartitle);
        title.setText((String)"Log Ins");
        ivback=findViewById(R.id.ivtoolbarback);

        Intent intent=getIntent();
        user= (User) intent.getSerializableExtra("user");

        userInitials=findViewById(R.id.toolbaruser);
        userInitials.setText((String)user.getUsername().substring(0,2).toUpperCase());

        CredentialDB db=new CredentialDB(this);
        db.open();
        user.setCredentials(db.getAllUserCredentials(user));
        db.close();

      adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, user.getCredentials()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Credential credential=(Credential) getItem(position);
                        assert credential != null;
                      //  Toast.makeText(VaultDisplay.this,credential.getUsername()+" " +credential.getPassword()+" "+credential.getUrl(),Toast.LENGTH_SHORT).show();
                        AlertDialog editDialog = new AlertDialog.Builder(VaultDisplay.this).create();
                        @SuppressLint("ViewHolder") View view = LayoutInflater.from(VaultDisplay.this).inflate(R.layout.activity_edit_credential, null, false);
                        editDialog.setView(view);

                        EditText etUserName = view.findViewById(R.id.etEditUsername);
                        EditText etPass= view.findViewById(R.id.etEditPassword);
                        EditText etUrl= view.findViewById(R.id.etEditURL);
                        TextView btnSave = view.findViewById(R.id.btEditSave);
                        TextView btnDelete = view.findViewById(R.id.btEditDelete);

                        etUrl.setText(credential.getUrl());
                        etPass.setText(credential.getPassword());
                        etUserName.setText(credential.getUsername());

                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.open();
                                int deleted=db.deleteCredentials(credential);
                                db.close();
                                if(deleted==1)
                                {
                                    Toast.makeText(editDialog.getContext(), "Credential sent to Bin",Toast.LENGTH_SHORT).show();
                                }
                                user.getCredentials().remove(credential);
                                adapter.notifyDataSetChanged();
                                editDialog.dismiss();
                            }
                        });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String use,pas,url;
                                use=etUserName.getText().toString().trim();
                                pas=etPass.getText().toString().trim();
                                url=etUrl.getText().toString().trim();
                                if(use.isEmpty() || pas.isEmpty() || url.isEmpty())
                                    Toast.makeText(editDialog.getContext(), "No field can be empty",Toast.LENGTH_SHORT).show();
                                else{
                                    if(!use.equals(credential.getUsername()))
                                    {

                                        db.open();
                                        if(db.updateCredential(credential,use,1)>0)
                                            Toast.makeText(editDialog.getContext(), "Username for "+credential.getUrl()+" updated", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(editDialog.getContext(), "Nothing updated", Toast.LENGTH_SHORT).show();
                                        db.close();
                                        credential.setUsername(use);
                                        adapter.notifyDataSetChanged();
                                    }
                                    if(!pas.equals(credential.getPassword())){
                                        System.out.println("password not same");
                                        db.open();
                                        if(db.updateCredential(credential,pas,2)>0)
                                            Toast.makeText(editDialog.getContext(), "Password for "+credential.getUrl()+" updated", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(editDialog.getContext(), "Nothing updated", Toast.LENGTH_SHORT).show();
                                        db.close();
                                        credential.setPassword(pas);
                                        adapter.notifyDataSetChanged();
                                    }
                                    editDialog.dismiss();

                                }
                            }
                        });
                        editDialog.show();

                        return true;
                    }
                });
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(user.getCredentials().get(position).getUsername());
                text2.setText(user.getCredentials().get(position).getUrl());

                text1.setTextColor(Color.WHITE);
                text2.setTextColor(Color.GRAY);
                return view;
            }

        };
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(   VaultDisplay.this,AddCredentialActivity.class);
                activitylauncher.launch(intent);
            }
        });

        listView.setAdapter(adapter);

    }
}