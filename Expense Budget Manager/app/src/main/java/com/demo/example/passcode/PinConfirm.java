package com.demo.example.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.Utile.CustomDialog;
import com.demo.example.R;


public class PinConfirm extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    String comeFormHome;
    private EditText etConfirmPassword;
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
    String password = "";
    int length = 0;

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pinconfirm);
        findViewById();
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
        this.etConfirmPassword = (EditText) findViewById(R.id.et_password);
        this.etConfirmPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                this.password += 0;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn1:
                this.password += 1;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn2:
                this.password += 2;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn3:
                this.password += 3;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn4:
                this.password += 4;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn5:
                this.password += 5;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn6:
                this.password += 6;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn7:
                this.password += 7;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn8:
                this.password += 8;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            case R.id.btn9:
                this.password += 9;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.password);
                return;
            default:
                return;
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.length = this.etConfirmPassword.length();
        if (this.length != 4) {
            return;
        }
        if (getIntent().getStringExtra("pin").equals(this.etConfirmPassword.getText().toString())) {
            startActivity(new Intent(this, EmailConfirm.class));
            finish();
            return;
        }
        CustomDialog.getInstance().OtherDialog(this, "Wrong password please enter again", new CustomDialog.DismissListenerWithStatus() {
            @Override
            public void onDismissed(String str) {
                if (str.equalsIgnoreCase("Ok")) {
                    PinConfirm.this.startActivity(new Intent(PinConfirm.this, PinEnterActivity.class));
                    PinConfirm.this.finish();
                }
            }
        });
    }
}
