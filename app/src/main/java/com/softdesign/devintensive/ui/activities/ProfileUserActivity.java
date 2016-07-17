package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.bio_et)
    EditText mUserBio;
    @BindView(R.id.ratings_user_info)
    TextView mUserRating;
    @BindView(R.id.codeLines_user_info)
    TextView mUserCodeLines;
    @BindView(R.id.projects_user_info)
    TextView mUserProjects;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.repositories_list)
    ListView mRepoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        try {
            ButterKnife.bind(this);
            setupToolbar();
            initProfileData();
        } catch (Exception e) {
            Log.e("DEV ProfileUserActivity", e.toString());
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        try {
            UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

            final List<String> repositories = userDTO.getRepositories();
            final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
            mRepoListView.setAdapter(repositoriesAdapter);

            mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Snackbar.make(mCollapsingToolbarLayout, "Репозиторий " + repositories.get(position), Snackbar.LENGTH_LONG).show();
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + repositories.get(position)));
                }
            });

            mUserRating.setText(userDTO.getRating());
            mUserCodeLines.setText(userDTO.getCodeLines());
            mUserProjects.setText(userDTO.getProjects());
            mUserBio.setText(userDTO.getBio());

            mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

            Picasso.with(this)
                    .load(userDTO.getPhoto())
                    .placeholder(R.drawable.user_bg)
                    .error(R.drawable.user_bg)
                    .into(mProfileImage);
        }catch(Exception e){
            Log.e("DEV PUA(iPD)", e.toString());
        }
    }
}
