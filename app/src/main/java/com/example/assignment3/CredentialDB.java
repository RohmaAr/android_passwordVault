package com.example.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class CredentialDB {
    private final String tbUsers="users";
    private final String tbVault="vault";
    private final String tbDeleted="deleted";
    private final String colId="_id";
    private final String colUsername="_username";
    private final String colPassword="_password";
    private final String colURL="_url";
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Credentials_DB";
    Context context;
    MyDatabaseHelper helper;
    SQLiteDatabase sqLiteDatabase;
    public CredentialDB(Context context)
    {
        this.context=context;
    }
    public void open()
    {
        helper = new MyDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }
    public int logIn(User user)
    {
        String query="SELECT "+colId+" FROM "+tbUsers+ " WHERE "+colUsername+" = '"+ user.getUsername()+"' and "+colPassword+" = '"+user.getPassword()+"'";
        Cursor c =  sqLiteDatabase.rawQuery(query, null);
        int id_index = c.getColumnIndex(colId);

        if(c.moveToFirst())
        {
                user.setId(c.getInt(id_index));
                c.close();
                //Toast.makeText(context,"Logged in ",Toast.LENGTH_SHORT).show();
                return 0;
        }

        c.close();
        return -1;
    }

    public void restore(Credential credential)
    {
        String[] whereArgs = new String[]{credential.getId()+"", credential.getUsername(), credential.getPassword(),credential.getUrl()};
        String whereClause = colId+" = ? AND "+colUsername+" = ? AND "+colPassword+" = ? AND "+colURL+"= ?";
        int records=sqLiteDatabase.delete(tbDeleted, whereClause, whereArgs);
        System.out.println("In restore :"+records+" deleted from bin");

        ContentValues contactValues=new ContentValues();
        contactValues.put(colPassword,credential.getPassword());
        contactValues.put(colUsername,credential.getUsername());
        contactValues.put(colURL,credential.getUrl());
        contactValues.put(colId,credential.getId());
        long inserted=sqLiteDatabase.insert(tbVault,null,contactValues);
        System.out.println("In restore :"+records+" added to the main table");


    }
    public int deleteCredentials(Credential credential)
    {
        String[] whereArgs = new String[]{credential.getId()+"", credential.getUsername(), credential.getPassword(),credential.getUrl()};
        String whereClause = colId+" = ? AND "+colUsername+" = ? AND "+colPassword+" = ? AND "+colURL+"= ?";
        addCredentialToBin(credential);
        return sqLiteDatabase.delete(tbVault, whereClause, whereArgs);
    }

    private void addCredentialToBin(Credential credential)
    {
        ContentValues contactValues=new ContentValues();
        contactValues.put(colPassword,credential.getPassword());
        contactValues.put(colUsername,credential.getUsername());
        contactValues.put(colURL,credential.getUrl());
        contactValues.put(colId,credential.getId());
        long inserted=sqLiteDatabase.insert(tbDeleted,null,contactValues);
        if (inserted==-1)
            System.out.println("Data not stored to Bin");
        else
            System.out.println("Data saved to Bin successfully");

    }
    public ArrayList<Credential> getAllUserCredentials(User user)
    {
        ArrayList<Credential> credentials=new ArrayList<>();
        String query="SELECT * FROM "+tbVault+ " WHERE "+colId+" = '"+ user.getId()+"'";
        Cursor c =  sqLiteDatabase.rawQuery(query, null);
        int id_index = c.getColumnIndex(colId);
        int id_username=c.getColumnIndex(colUsername);
        int id_password=c.getColumnIndex(colPassword);
        int id_url=c.getColumnIndex(colURL);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(id_index);
                String username = c.getString(id_username);
                String password = c.getString(id_password);
                String url=c.getString(id_url);
                credentials.add(new Credential(id,username,password,url));
            } while (c.moveToNext());
        }

        return credentials;
    }
    public ArrayList<Credential> getDeletedUserCredentials(User user)
    {
        ArrayList<Credential> credentials=new ArrayList<>();
        String query="SELECT * FROM "+tbDeleted+ " WHERE "+colId+" = '"+ user.getId()+"'";
        Cursor c =  sqLiteDatabase.rawQuery(query, null);
        int id_index = c.getColumnIndex(colId);
        int id_username=c.getColumnIndex(colUsername);
        int id_password=c.getColumnIndex(colPassword);
        int id_url=c.getColumnIndex(colURL);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(id_index);
                String username = c.getString(id_username);
                String password = c.getString(id_password);
                String url=c.getString(id_url);
                credentials.add(new Credential(id,username,password,url));
            } while (c.moveToNext());
        }
        return credentials;
    }
    public long signUp(User user)
    {
        if(logIn(user)==0)
        {
            Toast.makeText(context, "Account already exists", Toast.LENGTH_SHORT).show();
            return -1;
        }
        ContentValues contactValues=new ContentValues();
        contactValues.put(colPassword,user.getPassword());
        contactValues.put(colUsername,user.getUsername());
        return sqLiteDatabase.insert(tbUsers,null,contactValues);
    }
    public void insertCredential(Credential credential){
        ContentValues contactValues=new ContentValues();
        contactValues.put(colPassword,credential.getPassword());
        contactValues.put(colUsername,credential.getUsername());
        contactValues.put(colURL,credential.getUrl());
        contactValues.put(colId,credential.getId());
        long inserted=sqLiteDatabase.insert(tbVault,null,contactValues);
        if (inserted==-1)
            Toast.makeText(context, "Error!!! Data not saved", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }
    public int updateCredential(Credential credential,String changeTo,int column)
    {
        System.out.println("updating a "+column);
        ContentValues cv = new ContentValues();
        if(column==1)
            cv.put(colUsername, changeTo);
        else
            cv.put(colPassword, changeTo);

        return sqLiteDatabase.update(tbVault, cv, colURL+"=?", new String[]{credential.getUrl()+""});

    }
    public void getAllDb()
    {
        String query="SELECT * FROM "+tbUsers;
        Cursor c =  sqLiteDatabase.rawQuery(query, null);
        int id_index = c.getColumnIndex(colId);
        int id_username=c.getColumnIndex(colUsername);
        int id_password=c.getColumnIndex(colPassword);
        if (c.moveToFirst()) {
            System.out.println(tbUsers);
            do {
                int id = c.getInt(id_index);
                String username = c.getString(id_username);
                String password = c.getString(id_password);

                System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password);
            } while (c.moveToNext());
        }
        System.out.println();

        query ="SELECT * FROM "+tbVault;
        c=sqLiteDatabase.rawQuery(query,null);
        id_index = c.getColumnIndex(colId);
        id_username=c.getColumnIndex(colUsername);
        id_password=c.getColumnIndex(colPassword);
        int id_url=c.getColumnIndex(colURL);
        if (c.moveToFirst()) {
            System.out.println(tbVault);
            do {
                int id = c.getInt(id_index);
                String username = c.getString(id_username);
                String password = c.getString(id_password);
                String url=c.getString(id_url);

                System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password+" url "+url);
            } while (c.moveToNext());
        }
        System.out.println();

        query ="SELECT * FROM "+tbDeleted;
        c=sqLiteDatabase.rawQuery(query,null);
        id_index = c.getColumnIndex(colId);
        id_username=c.getColumnIndex(colUsername);
        id_password=c.getColumnIndex(colPassword);
        id_url=c.getColumnIndex(colURL);

        if (c.moveToFirst()) {
            System.out.println(tbDeleted);
            do {
                int id = c.getInt(id_index);
                String username = c.getString(id_username);
                String password = c.getString(id_password);
                String url=c.getString(id_url);
                System.out.println("ID: " + id + ", Username: " + username +" , Password " + password+" url  "+url);
              //  Toast.makeText(context, "ID: " + id + ", Username: " + username +" , Password " + password+" url  "+url, Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }

        System.out.println();
    }
    public void close()
    {
        sqLiteDatabase.close();
        helper.close();
    }

    private class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbUsers +
                    " (" + colId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    colUsername + " TEXT NOT NULL, " +
                    colPassword + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbVault +
                    " (" + colId + " INTEGER NOT NULL, " +
                    colUsername + " TEXT NOT NULL, " +
                    colPassword + " TEXT NOT NULL, " +
                    colURL + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + colId + ") REFERENCES " + tbUsers + "(" + colId + "));");

            db.execSQL("CREATE TABLE IF NOT EXISTS "+tbDeleted +
                    " (" + colId + " INTEGER NOT NULL, " +
                    colUsername + " TEXT NOT NULL, " +
                    colPassword + " TEXT NOT NULL, " +
                    colURL + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + colId + ") REFERENCES " + tbUsers + "(" + colId + "));");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+tbVault+" IF EXISTS");
            db.execSQL("DROP TABLE "+tbDeleted+" IF EXISTS");
            db.execSQL("DROP TABLE "+tbUsers+" IF EXISTS");
            onCreate(db);
        }
    }
}
