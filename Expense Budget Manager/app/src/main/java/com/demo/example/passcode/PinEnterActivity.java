package com.demo.example.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.Utile.Preferences;
import com.demo.example.R;


public class PinEnterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    String comeFormHome;
    private EditText etPassword;
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
    private TextView tvReset;
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
        setContentView(R.layout.activity_pinenter);
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
        this.etPassword = (EditText) findViewById(R.id.et_password);
        this.etPassword.addTextChangedListener(this);
        this.tvReset = (TextView) findViewById(R.id.tv_reset);
        this.tvReset.setOnClickListener(this);
        this.comeFormHome = getIntent().getStringExtra("Home");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.tv_reset) {
            switch (id) {
                case R.id.btn0:
                    this.password += 0;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn1:
                    this.password += 1;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn2:
                    this.password += 2;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn3:
                    this.password += 3;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn4:
                    this.password += 4;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn5:
                    this.password += 5;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn6:
                    this.password += 6;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn7:
                    this.password += 7;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn8:
                    this.password += 8;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                case R.id.btn9:
                    this.password += 9;
                    this.etPassword.setText("");
                    this.etPassword.setText(this.password);
                    return;
                default:
                    return;
            }
        } else {
            this.etPassword.setText("");
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.length = this.etPassword.length();
        if (this.length == 4) {
            Preferences instance = Preferences.getInstance();
            Preferences.getInstance().getClass();
            instance.SavePrefValue("pref_pin", this.etPassword.getText().toString());
            Intent intent = new Intent(this, PinConfirm.class);
            intent.putExtra("pin", this.etPassword.getText().toString());
            intent.putExtra("Home", "Home");
            startActivity(intent);
            finish();
        }
    }
}
