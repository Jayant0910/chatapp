package com.demo.example.passcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.Utile.CustomDialog;
import com.demo.example.Utile.Preferences;
import com.demo.example.dailyincomeexpensemanager.HomeActivity;
import com.demo.example.dailyincomeexpensemanager.MyDatabaseHandler;
import com.demo.example.dailyincomeexpensemanager.bean.AccountBean;
import com.demo.example.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    MyDatabaseHandler databaseHandler;
    private EditText etPasswordLogin;
    String getPassword;
    private ImageButton ivBtnEight;
    private ImageButton ivBtnFive;
    private ImageButton ivBtnFour;
    private ImageButton ivBtnNine;
    private ImageButton ivBtnOne;
    private ImageButton ivBtnSeven;
    private ImageButton ivBtnSix;
    private ImageButton ivBtnThree;
    private ImageButton ivBtnTwo;
    private ImageButton ivBtnZero;
    private TextView tvForgot;
    private TextView tvReset;
    String password = "";
    int length = 0;
    List<AccountBean> arraylist = new ArrayList();

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }


    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        findViewById();
        this.databaseHandler = new MyDatabaseHandler(this);
        this.arraylist = this.databaseHandler.getAccountList();
        Preferences instance = Preferences.getInstance();
        Preferences.getInstance().getClass();
        this.getPassword = instance.getPrefValue("pref_pin");
    }

    private void findViewById() {
        this.ivBtnOne = (ImageButton) findViewById(R.id.btn1);
        this.ivBtnTwo = (ImageButton) findViewById(R.id.btn2);
        this.ivBtnThree = (ImageButton) findViewById(R.id.btn3);
        this.ivBtnFour = (ImageButton) findViewById(R.id.btn4);
        this.ivBtnFive = (ImageButton) findViewById(R.id.btn5);
        this.ivBtnSix = (ImageButton) findViewById(R.id.btn6);
        this.ivBtnSeven = (ImageButton) findViewById(R.id.btn7);
        this.ivBtnEight = (ImageButton) findViewById(R.id.btn8);
        this.ivBtnNine = (ImageButton) findViewById(R.id.btn9);
        this.ivBtnZero = (ImageButton) findViewById(R.id.btn0);
        this.ivBtnOne.setOnClickListener(this);
        this.ivBtnTwo.setOnClickListener(this);
        this.ivBtnThree.setOnClickListener(this);
        this.ivBtnFour.setOnClickListener(this);
        this.ivBtnFive.setOnClickListener(this);
        this.ivBtnSix.setOnClickListener(this);
        this.ivBtnSeven.setOnClickListener(this);
        this.ivBtnEight.setOnClickListener(this);
        this.ivBtnNine.setOnClickListener(this);
        this.ivBtnZero.setOnClickListener(this);
        this.etPasswordLogin = (EditText) findViewById(R.id.et_password);
        this.etPasswordLogin.addTextChangedListener(this);
        this.tvForgot = (TextView) findViewById(R.id.tv_forgot);
        this.tvForgot.setOnClickListener(this);
        this.tvReset = (TextView) findViewById(R.id.tv_reset);
        this.tvReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
         if (id != R.id.tv_reset) {
            switch (id) {
                case R.id.btn0:
                    this.password += 0;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn1:
                    this.password += 1;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn2:
                    this.password += 2;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn3:
                    this.password += 3;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn4:
                    this.password += 4;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn5:
                    this.password += 5;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn6:
                    this.password += 6;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn7:
                    this.password += 7;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn8:
                    this.password += 8;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                case R.id.btn9:
                    this.password += 9;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.password);
                    return;
                default:
                    return;
            }
        } else {
            this.password = "";
            this.etPasswordLogin.setText("");
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.length = this.etPasswordLogin.length();
        if (this.length == 4) {
            Preferences instance = Preferences.getInstance();
            Preferences.getInstance().getClass();
            if (instance.getPrefValue("pref_pin").equals(this.etPasswordLogin.getText().toString())) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            }
            this.etPasswordLogin.setText("");
            this.password = "";
            CustomDialog.getInstance().ErrorDialog(this, "Wrong password please enter again");
        }
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && Build.VERSION.SDK_INT >= 16) {
            finishAffinity();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
