package com.example.chengen.mupetune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    private EditText user,password;
    private Button login;
    private TextView forget,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        user = (EditText)findViewById(R.id.etusername);
        password  =(EditText)findViewById(R.id.etpassword);
        login = (Button)findViewById(R.id.btnlogin);
        forget = (TextView)findViewById(R.id.tvforget);
        signUp = (TextView)findViewById(R.id.tvsignup);
        user.setHint("UserName/e-mail");
        password.setHint("Password");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin:
                break;
            case R.id.tvforget:
                break;
            case R.id.tvsignup:
                break;
        }
    }
}
