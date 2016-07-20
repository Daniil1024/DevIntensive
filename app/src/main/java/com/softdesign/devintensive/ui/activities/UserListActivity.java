package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.LoadingEvent;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private MenuItem mSearchItem;
    private String mQuery;

    private Handler mHandler;

    ProgressDialog mProgressDialog;

    public static int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mHandler = new Handler();
        EventBus.getDefault().register(this);
      //  showProgress();
        //runProgress();

        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        setupToolbar();
        setupDrawer();

        showProgress();
        runDb();

    }

    @Override
    protected void onStart() {
        Log.d("DEV", "Posting an event");
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUsersFromDb() {
        for(int i =0; i < 1; i++) {
            Object c = new Object();
        }
        if (mDataManager.getUserListFromDb().size() == 0) {
            showSnackbar("Список пользователей не может быть загружен");
        } else {
            // TODO: 19.07.2016 поиск по базе
            showUsers(mDataManager.getUserListFromDb());
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupDrawer() {
        Log.d(TAG, "Drawer setup");
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.toString());
                item.setChecked(true);
                if (item.getItemId() == R.id.exit) {
                    Intent login = new Intent(UserListActivity.this, AuthActivity.class);
                    startActivity(login);
                }
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        //  mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchItem = menu.findItem(R.id.search_action);
        android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint("Введите Имя пользователя");
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // TODO: 19.07.2016 поиск вызвать тут
                        if (!newText.isEmpty() && newText != "") {
                            showUsersByQuery(newText);
                        } else {
                            showUsers(mDataManager.getUserListFromDb());
                        }

                        return false;
                    }
                }
        );

        return super.onPrepareOptionsMenu(menu);

    }

    private void showUsers(List<User> users) {
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                showSnackbar("User at index " + position);
                try {
                    showProgress();
                    UserDTO userDTO = new UserDTO(mUsers.get(position));
                    Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                    profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                    startActivity(profileIntent);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });
        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUsersByQuery(String query) {
        mQuery = query;

        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };

        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
    }
    private void runProgress() {
        Runnable show = new Runnable() {
            @Override
            public void run() {
         //       UserListActivity.status = 1;
                showProgress();
                Log.d("DEV_TESTING_EBUS_RUN", "showing began");
                while(true) {
                    if (UserListActivity.status == 2) {
                        hideProgress();
                        Log.d("DEV_TESTING_EBUS_RUN", "showing ended");
                        break;
                    }
                }

            }
        };
        mHandler.post(show);
    }

    private void showProgress() {
        try {
            showDialog(0);
        //    mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
          //  mProgressDialog.setCancelable(false);
            //mProgressDialog.show();
           // mProgressDialog.setContentView(R.layout.progress_splash);
        } catch (Exception e) {

        }
    }

    private void hideProgress() {
        Log.d("DEV_", "hiding");
        try {
            dismissDialog(0);
         //   mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
           // mProgressDialog.hide();
        //    mProgressDialog.setContentView(R.layout.progress_splash);
          //  mProgressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadingEvent(LoadingEvent event) {
        //   this.status = event.status;
        switch (event.status) {
            case 1:
             //   status = 1;
                Log.d("DEV_TESTING_EVENTBUS", "loading began");
                showProgress();
                break;
            case 2:
              //  UserListActivity.status = 2;
                Log.d("DEV_TESTING_EBUS", "loading ended");
                hideProgress();
                break;
        }
    }

    private void runDb() {
        Runnable procces = new Runnable() {
            @Override
            public void run() {
                try {
                    EventBus.getDefault().post(new LoadingEvent(1));
                    Log.d("DEV_TESTING", "running Db");
                    loadUsersFromDb();
                    EventBus.getDefault().post(new LoadingEvent(2));

                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        };
        mHandler.post(procces);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        mProgressDialog = new ProgressDialog(
                UserListActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Загрзка...");
        return mProgressDialog;
    }
}
