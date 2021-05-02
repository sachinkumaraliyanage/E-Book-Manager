package com.mad.e_librarymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.e_librarymanager.admin.Admin_Add_Book;
import com.mad.e_librarymanager.admin.Admin_Update_Book;
import com.mad.e_librarymanager.admin.Admin_View_Books;
import com.mad.e_librarymanager.user.User_Main;

public class MainActivity extends AppCompatActivity {

    Button btn_admin,btn_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_admin = findViewById(R.id.btn_admin);
        btn_user = findViewById(R.id.btn_user);

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Admin_View_Books.class);
                startActivity(intent);
            }
        });

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, User_Main.class);
                startActivity(intent);
            }
        });

    }
}