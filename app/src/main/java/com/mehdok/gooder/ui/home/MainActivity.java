/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.crypto.KeyManager;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.preferences.PreferencesManager;
import com.mehdok.gooder.ui.about.AboutActivity;
import com.mehdok.gooder.ui.addpost.dialogs.AddPostDialog;
import com.mehdok.gooder.ui.followed.FollowedActivity;
import com.mehdok.gooder.ui.home.fragments.BaseFragment;
import com.mehdok.gooder.ui.home.fragments.CommentViewFragment;
import com.mehdok.gooder.ui.home.fragments.FriendsItemFragment;
import com.mehdok.gooder.ui.home.fragments.NotificationsFragment;
import com.mehdok.gooder.ui.home.fragments.StaredItemFragment;
import com.mehdok.gooder.ui.home.navigation.MainActivityDelegate;
import com.mehdok.gooder.ui.profile.ProfileActivity;
import com.mehdok.gooder.ui.recommended.RecommendedActivity;
import com.mehdok.gooder.utils.CustomExceptionHandler;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooder.views.VazirButton;
import com.mehdok.gooder.views.VazirEditText;
import com.mehdok.gooder.views.VazirTextView;
import com.mehdok.gooderapilib.GooderApi;
import com.mehdok.gooderapilib.QueryBuilder;
import com.mehdok.gooderapilib.RequestBuilder;
import com.mehdok.gooderapilib.models.BaseResponse;
import com.mehdok.gooderapilib.models.user.UserInfo;

import java.util.UUID;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private enum ViewKind {FRIENDS, COMMENT, NOTIFICATION, STARED}

    public enum CommentKind {ME, ME_FOLLOWED, EVERYONE}

    private boolean firstRun = true;
    private Dialog loginDialog;
    private UserInfo userInfo = null;
    private ProgressDialog waitingDialog;
    // used for database storage
    private byte[] mPassword;
    private boolean showFlag = true;
    private boolean hideFlag = true;
    private int bottomBarSize = 0;
    private ViewKind mViewKind;
    private CommentKind mCommentKind = CommentKind.ME; // default comment view kind

    private CoordinatorLayout mRootLayout;
    private ImageView userImage;
    private VazirTextView tvUserName;
    private FloatingActionButton addPostFab;

    private MenuItem itemReverseOrder;
    private MenuItem itemUnreadOnly;
    private MenuItem itemCommentByMe;
    private MenuItem itemCommentByMeAndFollowers;
    private MenuItem itemCommentByAnyone;
    private MenuItem itemClearNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivityDelegate.subscribeOn(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.main_root_layout);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        // set the private encryption key
        handleFirstRun();

        setupCrashReporter();
        //TODO setup google tracker
        //TODO setup new version reminder

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup add new post
        addPostFab = (FloatingActionButton) findViewById(R.id.add_new_post);
        addPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPost();
            }
        });

        // setup drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // setup left navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set click listener for nav header for login or go to profile page
        LinearLayout headerLayout = (LinearLayout) navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(this);

        userImage = (ImageView) headerLayout.findViewById(R.id.user_photo);
        tvUserName = (VazirTextView) headerLayout.findViewById(R.id.user_name);

        setupBottomBar(savedInstanceState);

        startApp();
    }

    private void startApp() {
        userInfo = DatabaseHelper.getInstance(this).getUserInfo();
        if (userInfo != null) {
            initFirstView();
        } else {
            showLoginDialog();
        }
    }

    private void setupBottomBar(Bundle savedInstanceState) {

        AHBottomNavigation bottomNavigation =
                (AHBottomNavigation) findViewById(R.id.main_bottom_navigation);
        AHBottomNavigationItem
                item1 = new AHBottomNavigationItem(R.string.bottom_bar_home,
                R.drawable.ic_home_grey600_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_bar_comment,
                R.drawable.ic_insert_comment_grey600_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_bar_star,
                R.drawable.ic_star_grey600_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.bottom_bar_notification,
                R.drawable.ic_notifications_grey600_24dp, R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);
        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);
        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);
        // Set listener
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        mViewKind = ViewKind.FRIENDS;
                        showGeneralOption(true);
                        showCommentOptions(false);
                        showNotifOption(false);
                        changeView(FriendsItemFragment.getInstance());
                        return true;
                    case 1:
                        mViewKind = ViewKind.COMMENT;
                        showCommentOptions(true);
                        showGeneralOption(true);
                        showNotifOption(false);
                        changeView(CommentViewFragment.getInstance());
                        return true;
                    case 2:
                        showCommentOptions(false);
                        showGeneralOption(false);
                        showNotifOption(false);
                        mViewKind = ViewKind.STARED;
                        changeView(StaredItemFragment.getInstance());
                        return true;
                    case 3:
                        showCommentOptions(false);
                        showGeneralOption(false);
                        showNotifOption(true);
                        mViewKind = ViewKind.NOTIFICATION;
                        changeView(NotificationsFragment.getInstance());
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        itemReverseOrder = menu.findItem(R.id.action_friends_reverse_order);
        itemUnreadOnly = menu.findItem(R.id.action_friends_unread_only);
        itemCommentByMe = menu.findItem(R.id.action_comment_by_me);
        itemCommentByMeAndFollowers = menu.findItem(R.id.action_comment_by_me_and_followers);
        itemCommentByAnyone = menu.findItem(R.id.action_comment_by_anyone);
        itemClearNotification = menu.findItem(R.id.action_clear_notification);

        itemUnreadOnly.setChecked(true);
        itemCommentByMe.setChecked(true);

        // first view is friends, so hide comment options
        showCommentOptions(false);
        showGeneralOption(true);
        showNotifOption(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_search) {
            return true;
        } else*/
        if (id == R.id.action_friends_reverse_order) {
            itemReverseOrder.setChecked(!itemReverseOrder.isChecked());
        } else if (id == R.id.action_friends_unread_only) {
            itemUnreadOnly.setChecked(!itemUnreadOnly.isChecked());
        } else if (id == R.id.action_comment_by_me) {
            mCommentKind = CommentKind.ME;
            itemCommentByMe.setChecked(true);
        } else if (id == R.id.action_comment_by_me_and_followers) {
            mCommentKind = CommentKind.ME_FOLLOWED;
            itemCommentByMeAndFollowers.setChecked(true);
        } else if (id == R.id.action_comment_by_anyone) {
            mCommentKind = CommentKind.EVERYONE;
            itemCommentByAnyone.setChecked(true);
        } else if (id == R.id.action_clear_notification) {
            clearNotification();
        }

        reloadFragmentsData();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO
        if (id == R.id.nav_people_you_follow) {
            ActivityCompat.startActivity(this, new Intent(this, FollowedActivity.class),
                    ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    this, null).toBundle());
        } else if (id == R.id.nav_special_item) {
            ActivityCompat.startActivity(this, new Intent(this, RecommendedActivity.class),
                    ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    this, null).toBundle());
        } else if (id == R.id.nav_bug_report) {
            Util.sendBugReport(this, getString(R.string.bug_email_subject),
                    getString(R.string.bug_email_context));
        } else if (id == R.id.nav_about_app) {
            ActivityCompat.startActivity(this, new Intent(this, AboutActivity.class),
                    ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    this, null).toBundle());
        } else if (id == R.id.nav_log_out) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivityDelegate.unSubscribe(this);
    }

    private void addNewPost() {
        AddPostDialog dialogFragment = new AddPostDialog();
        dialogFragment.show(getSupportFragmentManager(), "add_post_dialog");
    }

    private void initFirstView() {
        setUserInfoInUI(userInfo);

        Bundle bundle = new Bundle();
        FriendsItemFragment.getInstance().setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.base_fragment_layout, FriendsItemFragment.getInstance())
                .commit();
    }

    private void changeView(Fragment fragment) {
        if (fragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment_layout, fragment)
                .commit();
    }

    private void handleFirstRun() {
        PreferencesManager pref = new PreferencesManager(this);

        if (pref.isFirstRun()) {
            // set private key for any encryption, this will run once
            KeyManager keyManager = new KeyManager();
            keyManager.setId(UUID.randomUUID().toString().substring(0, 32).getBytes(), this);
            keyManager.setIv(UUID.randomUUID().toString().substring(0, 16).getBytes(), this);

            pref.setFirstRun(false);
        }
    }

    private void showLoginDialog() {
        loginDialog = new Dialog(this, R.style.DialogStyle);
        loginDialog.setContentView(R.layout.dialog_login);
        loginDialog.setCancelable(false);
        loginDialog.show();
        loginDialog.getWindow()
                .setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        final VazirEditText name = (VazirEditText) loginDialog.findViewById(R.id.login_user_name);
        final VazirEditText password =
                (VazirEditText) loginDialog.findViewById(R.id.login_password);
        final VazirButton loginButton = (VazirButton) loginDialog.findViewById(R.id.login_do);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;

                if (name.getText().toString().equals("")) {
                    name.setError(getString(R.string.error_required_field));
                    error = true;
                }

                if (password.getText().toString().equals("")) {
                    password.setError(getString(R.string.error_required_field));
                    error = true;
                }

                if (error) return;

                loginWithUserPass(name.getText().toString(),
                        password.getText().toString());

                loginDialog.dismiss();
            }
        });
    }

    private void loginWithUserPass(String userName, String password) {
        try {
            mPassword = Crypto.encrypt(password.getBytes(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //requestAccessCode(userName, Crypto.getMD5BASE64(password));
        showWaitingDialog();

        // build qquery
        RequestBuilder requestBuilder = new RequestBuilder();
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userName);
        queryBuilder.setPassword(Crypto.getMD5BASE64(password));

        // exe request for user info
        requestBuilder.getUserInfo(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        waitingDialog.dismiss();
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        waitingDialog.dismiss();
                        userInfo.setPassword(mPassword);
                        putUserInfo(userInfo);
                        initFirstView();
                    }
                });
    }

    private void showWaitingDialog() {
        waitingDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        waitingDialog.setTitle(R.string.wait_for_server_title);
        waitingDialog.setMessage(getResources().getString(R.string.wait_for_server_body));
        waitingDialog.show();
    }

    private void putUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        DatabaseHelper.getInstance(this).putUserInfo(userInfo);
    }

    private void setUserInfoInUI(UserInfo userInfo) {
        tvUserName.setText(userInfo.getFullname());

        Glide
                .with(this)
                .load(userInfo.getAvatar())
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.logo)
                .into(new BitmapImageViewTarget(userImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        userImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void setupCrashReporter() {
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Intent intent = new Intent(this, CrashReporterActivity.class);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }

            Thread.setDefaultUncaughtExceptionHandler(
                    new CustomExceptionHandler(this, getResources().getString(R.string.app_name),
                            intent));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.nav_header) {
            if (userInfo != null) {
                openProfilePage();
            } else {
                showLoginDialog();
            }
        }
    }

    private void openProfilePage() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        if (userInfo != null) {
            profileIntent.putExtra(ProfileActivity.PROFILE_USER_ID, userInfo.getUid());
        }
        startActivity(profileIntent);
    }

    public void showBugSnackBar(Throwable e) {
        e.printStackTrace();
        String error;
        if (e instanceof HttpException) {
            error = ((HttpException) e).response().body().toString();
        } else {
            error = e.getMessage();
        }
        Snackbar.make(mRootLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.send_report, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.sendBugReport(MainActivity.this, getString(R.string.bug_email_subject),
                                getString(R.string.bug_email_context));
                    }
                }).show();
    }

    public void showSimpleMessage(String str) {
        Snackbar.make(mRootLayout, str, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void logOut() {
        //delete the user from database
        DatabaseHelper.getInstance(this).deleteUserInfo();

        // reset the access code
        GooderApi.lastOperation = 0;
        GooderApi.mAccessCode = "";

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).clearViews();
                trans.remove(fragment);
            } else if (fragment instanceof NotificationsFragment) {
                ((NotificationsFragment) fragment).clearViews();
                trans.remove(fragment);
            }
        }

        trans.commit();
        startApp();
    }

    public void showUI() {
        if (showFlag && addPostFab.getVisibility() != View.VISIBLE) {
            showFlag = false;

            //show fab
            addPostFab.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    super.onShown(fab);
                    showFlag = true;
                }
            });

            //show bottom bar
            //            mBottomBar.show();

            //show actionbar
            //            getSupportActionBar().show();
        }
    }

    public void hideUI() {
        if (hideFlag && addPostFab.getVisibility() == View.VISIBLE) {
            hideFlag = false;

            //hide fab
            addPostFab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    hideFlag = true;
                }
            });

            //hide bottom bar
            //            mBottomBar.hide();

            //hide actionbar
            //            getSupportActionBar().hide();
        }
    }

    private void showCommentOptions(boolean show) {
        itemCommentByMe.setVisible(show);
        itemCommentByMeAndFollowers.setVisible(show);
        itemCommentByAnyone.setVisible(show);
    }

    private void showGeneralOption(boolean show) {
        itemUnreadOnly.setVisible(show);
        itemReverseOrder.setVisible(show);
    }

    private void showNotifOption(boolean show) {
        itemClearNotification.setVisible(show);
    }

    public QueryBuilder.Value isReverseOrder() {
        // if view is null it mean this is the first run, so return default value.
        //TODO ROAD_MAP put a default value in setting
        if (itemReverseOrder == null) return QueryBuilder.Value.NO;

        if (itemReverseOrder.isChecked()) {
            return QueryBuilder.Value.YES;
        } else {
            return QueryBuilder.Value.NO;
        }
    }

    public QueryBuilder.Value isUnreadOnly() {
        // if view is null it mean this is the first run, so return default value.
        //TODO ROAD_MAP put a default value in setting
        if (itemUnreadOnly == null) return QueryBuilder.Value.YES;

        if (itemUnreadOnly.isChecked()) {
            return QueryBuilder.Value.YES;
        } else {
            return QueryBuilder.Value.NO;
        }
    }

    private void reloadFragmentsData() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BaseFragment && fragment.isVisible()) {
                ((BaseFragment) fragment).refreshData();
            }
        }
    }

    public QueryBuilder.TypeFactor getCommentKind() {
        if (mCommentKind == CommentKind.ME) {
            return QueryBuilder.TypeFactor.ME;
        } else if (mCommentKind == CommentKind.ME_FOLLOWED) {
            return QueryBuilder.TypeFactor.ME_FRIENDS;
        } else {
            return QueryBuilder.TypeFactor.MY_POSTS;
        }
    }

    public void requestDeleteNotif(String nid) {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBuilder requestBuilder = new RequestBuilder();

        requestBuilder.deleteNotification(nid, queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        showSimpleMessage(getResources().getString(R.string.notif_delete));
                    }
                });

    }

    private void clearNotification() {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setUserName(userInfo.getUsername());
        try {
            queryBuilder.setPassword(Crypto.getMD5BASE64(
                    new String(Crypto.decrypt(userInfo.getPassword(), this))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.clearNotifications(queryBuilder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showBugSnackBar(e);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        showSimpleMessage(getResources().getString(R.string.notif_clear));
                        //TODO clear adapter data
                    }
                });
    }

}
