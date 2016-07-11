package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.softdesign.devintensive.utils.TextValidator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    //переменная отвечающая за чтение/запись(сохранение) пользовательских данных
    private DataManager mDataManager;

    //переменная отвечающая за режим просмотра/редактирования false/true соответственно
    private boolean mCurrentEditMode = false;

    /*
    * Инициалиация компонентов activity с помощью библиотеки ButterKnife
    * */
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;

    @BindView(R.id.phone_et)
    EditText mUserPhone;
    @BindView(R.id.mail_et)
    EditText mUserMail;
    @BindView(R.id.vk_et)
    EditText mUserVk;
    @BindView(R.id.git_et)
    EditText mUserGit;
    @BindView(R.id.bio_et)
    EditText mUserBio;
    List<EditText> mUserInfoViews;

    //некоторые переменные связанные с обработкой пользовательских данных и фото
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;

    @BindView(R.id.call)
    ImageView mCall;
    @BindView(R.id.send_mail)
    ImageView mSendMail;
    @BindView(R.id.view_vk)
    ImageView mViewVk;
    @BindView(R.id.view_gitub)
    ImageView mViewGithub;

    //стандартный метод activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");
        //bind всех view инициализированных через @BindView
        ButterKnife.bind(this);

        mDataManager = DataManager.getINSTANCE();

        //добавление EditText-ов в массив
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        //установка обработчика нажатий
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mSendMail.setOnClickListener(this);
        mViewVk.setOnClickListener(this);
        mViewGithub.setOnClickListener(this);

        //установка обработчика(валидатора) ввода
        mUserPhone.addTextChangedListener(new TextValidator(mUserPhone));
        mUserMail.addTextChangedListener(new TextValidator(mUserMail));
        mUserVk.addTextChangedListener(new TextValidator(mUserVk));
        mUserGit.addTextChangedListener(new TextValidator(mUserGit));

        //установка тулбара и NavigationDrawer
        setupToolbar();
        setupDrawer();

        //загрузка пользовотельских данных
        loadUserInfoValue();

        //установка фото пользователя
        Picasso.with(this).load(mDataManager.getPrefencesManager().loadUserPhoto()).placeholder(R.drawable.userphoto).into(mProfileImage);// TODO: 30.06.2016 сделать плейсхолдер и transform + crop
        if (savedInstanceState == null) {
            //активити запускается впревые
        } else {
            //активити уже создавалось

            //получение и установка режима релактирования
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            changeEditMode(mCurrentEditMode);
        }
    }

    //обработчик нажатия на раскрытие NavigationDrawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    //обработчик нажатий
    @Override
    public void onClick(View view) {
        //выбор на который элемент нажал пользователь
        switch (view.getId()) {
            //FloatingActionButton(измена режима редактирования)
            case R.id.fab:
                mCurrentEditMode = !mCurrentEditMode;
                changeEditMode(mCurrentEditMode);
                break;
            //Placeholder(сделать фото или загрузить из галлереи)
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            //ImageView(позвонить)
            case R.id.call:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, ConstantManager.REQUEST_CALL);
                }
                Uri phone = Uri.parse("tel:" + mUserPhone.getText().toString());
                Intent call = new Intent(Intent.ACTION_CALL, phone);
                try {
                    startActivity(call);
                } catch (Exception e) {
                }
                break;
            //ImageView(отправить email сообщение адресату)
            case R.id.send_mail:
                Uri mail_adress = Uri.parse("mailto: " + mUserMail.getText().toString());
                Intent sendMail = new Intent(Intent.ACTION_SENDTO);
                sendMail.setData(mail_adress);
                try {
                    startActivity(Intent.createChooser(sendMail, "Выберите приложение для отправки"));
                } catch (Exception e) {
                }
                break;
            //ImageView(просмотреть страницу ВК)
            case R.id.view_vk:
                Uri vkAdress = Uri.parse("https://www." + mUserVk.getText().toString());
                Intent viewVk = new Intent(Intent.ACTION_VIEW, vkAdress);
                try {
                    startActivity(viewVk);
                } catch (Exception e) {
                }
                break;
            //ImageView(просмотреть страницу Github)
            case R.id.view_gitub:
                Uri githubAdress = Uri.parse("https://www." + mUserGit.getText().toString());
                Intent viewGithub = new Intent(Intent.ACTION_VIEW, githubAdress);
                try {
                    startActivity(viewGithub);
                } catch (Exception e) {
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    //показывает snackbar с передаваемым сообщением
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    //установка тулбара
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //установка NavigationDrawer
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Bitmap userphoto = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        if (userphoto != null) {
            userphoto = RoundedAvatarDrawable.getRoundedBitmap(userphoto);
            RoundedAvatarDrawable.setAvatarBitmap(userphoto, navigationView);
        } else {
            Log.d(TAG, "Userphoto is null");
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.toString());
                item.setChecked(true);
                if (item.getItemId() == R.id.exit) {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    //Обработка нажатия системной кнопки back(сворачивание NavigationDrawer)
    @Override
    public void onBackPressed() {
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return;
    }

    /**
     * Получение результата из другой Activity (фото из камеры или галлереи)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    /**
     * переключает режим редактирования
     *
     * @param mode если true, режим редактирования, если false, режим просмотра
     */
    private void changeEditMode(boolean mode) {
        if (mode) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            }
            mUserPhone.requestFocus();
        } else {
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();

                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            }
        }
    }

    //загрузка пользовательских данных
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPrefencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    //сохранение пользовательских данных
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPrefencesManager().saveUserProfileData(userData);
    }

    //загрузка фото из галереи
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    //загрузка фото из камеры(сделать снимок)
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d("Camera ", "loading");
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Camera", "error creating: " + e.toString());
                // TODO: 30.06.2016 обработать ошибку
            }
            if (mPhotoFile != null) {
                // TODO: 30.06.2016 передать фотофайл в intent
                Log.d("Camera ", "mPhotoFile != null");
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_RQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openApplicationSettings();
                }
            }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //выбор с каким requestCode было запрошено разрешение
        switch (requestCode) {
            //запрос разрешений для камеры/галереи
            case ConstantManager.CAMERA_RQUEST_PERMISSION_CODE:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // TODO: 30.06.2016 тут обрабатываем разрешение (разрешение получено) например вывести сообщение или обработать какой-то логикой если нужно
                    }
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // TODO: 30.06.2016 тут обрабатываем разрешение (разрешение получено) например вывести сообщение или обработать какой-то логикой если нужно
                    }
                } catch (IndexOutOfBoundsException e) {

                }
                break;
            //запрос разрешения совершать звонки
            case ConstantManager.REQUEST_CALL:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемое разрешение", Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openApplicationSettings();
                            }
                        }).show();
                    }
                } catch (IndexOutOfBoundsException e) {

                }
                break;
        }

    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                //загрузка из галлереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                // загрузка из камеры
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                // отмена
                                dialogInterface.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    //создаётся File с уникальным именем
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.d("Camera", "No error");

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    //вставка изображения в профиль
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this).load(selectedImage).into(mProfileImage);
        mDataManager.getPrefencesManager().saveUserPhoto(mSelectedImage);
        // TODO: 30.06.2016 сделать плейсхолдер и transform + crop
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }
}