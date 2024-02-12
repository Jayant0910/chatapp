package com.demo.example.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.Utile.Preferences;
import com.demo.example.dailyincomeexpensemanager.HomeActivity;
import com.demo.example.dailyincomeexpensemanager.MyDatabaseHandler;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class EmailConfirm extends AppCompatActivity implements View.OnClickListener {
    List<AccountBean> arrayList = new ArrayList();
    private Button btnContinue;
    MyDatabaseHandler databaseHandler;
    private EditText etAccEmail;

    
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_emailconfirm);
        this.etAccEmail = (EditText) findViewById(R.id.etAccountEmail);
        this.btnContinue = (Button) findViewById(R.id.btn_continue);
        this.btnContinue.setOnClickListener(this);
        this.databaseHandler = new MyDatabaseHandler(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_continue) {
            if (!checkmail(this.etAccEmail.getText().toString())) {
                Toast.makeText(this, "Enter valid email.", Toast.LENGTH_SHORT).show();
                return;
            }
            this.arrayList = this.databaseHandler.getAccountList();
            if (this.arrayList.get(0).getEmail().equals(this.etAccEmail.getText().toString())) {
                Preferences.getInstance().setFirstTimeLaunch(true);
                Preferences.getInstance().setLoginStatus(true);
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            }
            Toast.makeText(this, "Email not match with before enter email", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkmail(String str) {
        if (str == null || str.equalsIgnoreCase("")) {
            return false;
        }
        return Pattern.compile(".+@.+\\.[a-z]+").matcher(str).matches();
    }
}
