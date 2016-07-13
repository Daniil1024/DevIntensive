package com.softdesign.devintensive.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.softdesign.devintensive.R;

/**
 * Класс позволяющий обрабатывать пользовательский ввод
 */
public class TextValidator implements TextWatcher {

    //обрабатываемый EditText
    private EditText mEditText;

    //Конструктор класса
    public TextValidator(EditText editText) {
        this.mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //обработчик
    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        //определяет обрабатываемое поле
        switch (mEditText.getId()) {
            case R.id.phone_et:
                if (text.length() <= 11) {
                    mEditText.setError("Телефонный номер должен быть длиннее 10 символов");
                    break;
                }
                if (text.length() >= 20) {
                    mEditText.setError("Телефонный номер должен быть короче 21 символа");
                    break;
                }
                break;
            case R.id.mail_et:
                try {
                    if (!text.contains(".")) {
                        mEditText.setError("Нет разделителя доменного имени(точки)");
                        break;
                    }
                    if (!text.contains("@")) {
                        mEditText.setError("Нет @");
                        break;
                    }
                    if (text.split("@").length != 2) {
                        mEditText.setError("E-mail некорректен");
                        break;
                    }
                    if (text.split("@")[0].length() < 3) {
                        mEditText.setError("Перед @ должно быть не меньше 3 символов");
                        break;
                    }
                    if (text.split("@")[1].split("\\.")[0].length() < 2) {
                        mEditText.setError("Перед точкой должно быть не меньше 2 символов");
                        break;
                    }
                    if (text.split("@")[1].split("\\.")[1].length() < 2) {
                        mEditText.setError("После точки должно быть не меньше 2 символов");
                        break;
                    }
                } catch (Exception e) {
                }
                break;
            case R.id.login_email_et:
                try {
                    if (!text.contains(".")) {
                        mEditText.setError("Нет разделителя доменного имени(точки)");
                        break;
                    }
                    if (!text.contains("@")) {
                        mEditText.setError("Нет @");
                        break;
                    }
                    if (text.split("@").length != 2) {
                        mEditText.setError("E-mail некорректен");
                        break;
                    }
                    if (text.split("@")[0].length() < 3) {
                        mEditText.setError("Перед @ должно быть не меньше 3 символов");
                        break;
                    }
                    if (text.split("@")[1].split("\\.")[0].length() < 2) {
                        mEditText.setError("Перед точкой должно быть не меньше 2 символов");
                        break;
                    }
                    if (text.split("@")[1].split("\\.")[1].length() < 2) {
                        mEditText.setError("После точки должно быть не меньше 2 символов");
                        break;
                    }
                } catch (Exception e) {
                }
                break;
            case R.id.vk_et:
                if (!text.startsWith("vk.com/")) {
                    mEditText.setError("Неверный формат адреса");
                    break;
                }
                if (text.equals("vk.com/")) {
                    mEditText.setError("Введите ссылку на страницу");
                }
                break;
            case R.id.git_et:
                if (!text.startsWith("github.com/")) {
                    mEditText.setError("Неверный формат адреса");
                    break;
                }
                if (text.equals("github.com/")) {
                    mEditText.setError("Введите ссылку на страницу");
                }
                break;
        }
    }
}
