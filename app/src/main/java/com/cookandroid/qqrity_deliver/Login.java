package com.cookandroid.qqrity_deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    EditText loginID, loginPW;
    TextView go_register, find_idpw;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginID = (EditText)findViewById(R.id.login_edtId);
        loginPW = (EditText)findViewById(R.id.login_edtPw);
        go_register = (TextView)findViewById(R.id.go_register);
        find_idpw = (TextView)findViewById(R.id.find_idpw);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        // 회원가입하기
        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getApplicationContext(),Register1.class);
                startActivity(mIntent);
            }
        });

        // 아이디/비밀번호 찾기
        find_idpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 로그인하기
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(mIntent);
            }
        });

    }
}
