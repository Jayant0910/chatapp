package com.anass.ninflix.Activities.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anass.ninflix.R;


public class Signintojcartoon extends AppCompatActivity {

    ImageView back;
    EditText onenameedit,emaileditnew,passedit;
    RelativeLayout gotopagename;

    String fullname , email ,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signintojcartoon);

        back = findViewById(R.id.back);
        gotopagename = findViewById(R.id.gotopagename);
        onenameedit = findViewById(R.id.onenameedit);
        emaileditnew = findViewById(R.id.emaileditnew);
        passedit = findViewById(R.id.passedit);



        gotopagename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = onenameedit.getText().toString();
                email = emaileditnew.getText().toString();
                password = passedit.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(Signintojcartoon.this, "Email is empty", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){

                        Toast.makeText(Signintojcartoon.this, "Password Is empty", Toast.LENGTH_SHORT).show();


                }else if(fullname.isEmpty()){

                        Toast.makeText(Signintojcartoon.this, "Full Name is Empty", Toast.LENGTH_SHORT).show();


                }else {

                    Intent sendDataToDetailsActivity = new Intent(Signintojcartoon.this, Signintojcartoon2.class);
                    sendDataToDetailsActivity.putExtra("fullname",fullname);
                    sendDataToDetailsActivity.putExtra("email",email);
                    sendDataToDetailsActivity.putExtra("password",password);
                    startActivity(sendDataToDetailsActivity);

                }

            }
        });







        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}