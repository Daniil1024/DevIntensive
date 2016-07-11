package com.softdesign.devintensive.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.TextValidator;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* LoginActivity - activity для взаимодействия с меню входа пользователя
* */
public class LoginActivity extends Activity implements View.OnClickListener {
    /*
    * Инициалиация компонентов activity с помощью библиотеки ButterKnife
    * */
    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.login_email)
    EditText mEmail;
    @BindView(R.id.login_password)
    EditText mPassword;

    /*
    * Переопределение стандартного метода activity
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        mEmail.addTextChangedListener(new TextValidator(mEmail));
        mLoginButton.setOnClickListener(this);
    }

    /*
    * Обрабочик нажатия на элементы activity*/
    @Override
    public void onClick(View view) {
        //switch на какой элемент нажал пользователь
        switch (view.getId()) {
            /*
            * кнопка входа в профиль*/
            case R.id.login_button:
                // TODO: 06.07.2016 обработать вход
                Intent enter = new Intent(this, MainActivity.class);
                startActivity(enter);
                break;
        }
    }
}
