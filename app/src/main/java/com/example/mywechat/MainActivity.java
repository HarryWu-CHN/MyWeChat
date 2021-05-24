package com.example.mywechat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoginView();
    }

    private void setLoginView() {
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.login_button);
        Button registerUserButton = findViewById(R.id.register_user_button);

        loginButton.setOnClickListener(v -> {
            //TODO: 增加登录验证
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        });
        registerUserButton.setOnClickListener(v -> {
            setRegisterView();
        });
    }

    private void setRegisterView() {
        setContentView(R.layout.fragment_register);
        Button loginUserButton = findViewById(R.id.login_user_button);
        Button registerButton = findViewById(R.id.register_button);

        loginUserButton.setOnClickListener(v -> {
            setLoginView();
        });
        registerButton.setOnClickListener(v -> {
            // TODO: 进行注册验证
        });
    }

}