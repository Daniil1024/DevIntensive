package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements OnClickListener {

    /*
    * Инициалиация компонентов activity с помощью библиотеки ButterKnife
    * */
    @BindView(R.id.login_button)
    Button mSignIn;
    @BindView(R.id.remember_txt)
    TextView mRememberPassword;
    @BindView(R.id.login_email_et)
    EditText mLogin;
    @BindView(R.id.login_password_et)
    EditText mPassword;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //    Log.d("DEV", "auth");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        try {
                      mDataManager = DataManager.getInstance();
        } catch (Exception e) {
            Log.d("DEV ", e.toString());
        }
        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                try {
                    signIn();
                } catch (Exception e) {
                    Log.d("DEV ", e.toString());
                }
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(Response<UserModelRes> response) {
        showSnackbar(response.body().getData().getToken());
        Intent loginSuccessIntent = new Intent(this, MainActivity.class);
        startActivity(loginSuccessIntent);
    }

    private void signIn() {
        Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
        call.enqueue(new Callback<UserModelRes>() {
            @Override
            public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                Log.d("DEV ", response.code()+"");
                if (response.code() == 200) {
                    mDataManager.getPrefencesManager().saveAuthToken(response.body().getData().getToken());
                    mDataManager.getPrefencesManager().saveUserId(response.body().getData().getUser().getId());

                    List<String> userProfileData = new ArrayList<String>();
                    userProfileData.add(response.body().getData().getUser().getContacts().getPhone());
                    userProfileData.add(response.body().getData().getUser().getContacts().getEmail());
                    userProfileData.add(response.body().getData().getUser().getContacts().getVk());
                    userProfileData.add(response.body().getData().getUser().getRepositories().getRepo().get(0).getGit());
                    userProfileData.add(response.body().getData().getUser().getPublicInfo().getBio());

                    mDataManager.getPrefencesManager().saveUserProfileData(userProfileData);


                    List<Uri> photos = new ArrayList<Uri>();
                    photos.add(Uri.parse(response.body().getData().getUser().getPublicInfo().getPhoto()));
                    photos.add(Uri.parse(response.body().getData().getUser().getPublicInfo().getAvatar()));
                    mDataManager.getPrefencesManager().saveUserPhotos(photos);

                    try {
                        String[] info = new String[3];
                        info[0] = response.body().getData().getUser().getProfileValues().getRating();
                        info[1] = response.body().getData().getUser().getProfileValues().getCodelines();
                        info[2] = response.body().getData().getUser().getProfileValues().getProjects();
                        mDataManager.getPrefencesManager().saveUserInfo(info);
                    }catch(Exception e) {
                        Log.d(TAG, e.toString());
                    }

                    showSnackbar(response.body().getData().getToken());
                    loginSuccess(response);
                    Log.d("DEV ", "Success!!!");
                } else if (response.code() == 404) {
                    showSnackbar("неверный логин или пароль");
                } else {
                    showSnackbar("Всё пропало Шеф!!!!");
                }
            }

            @Override
            public void onFailure(Call<UserModelRes> call, Throwable t) {
                // TODO: 11.07.2016 обработать ошибки ретрофита
                Log.d("DEV ", t.toString());
            }
        });
    }
}
