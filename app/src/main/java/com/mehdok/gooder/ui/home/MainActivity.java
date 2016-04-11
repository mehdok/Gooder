/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mehdok.gooder.R;
import com.mehdok.gooder.crypto.Crypto;
import com.mehdok.gooder.crypto.KeyManager;
import com.mehdok.gooder.database.DatabaseHelper;
import com.mehdok.gooder.network.JsonHandler;
import com.mehdok.gooder.network.interfaces.AccessCodeListener;
import com.mehdok.gooder.network.model.UserInfo;
import com.mehdok.gooder.preferences.PreferencesManager;
import com.mehdok.gooder.ui.home.fragments.CommentViewFragment;
import com.mehdok.gooder.ui.home.fragments.FriendsItemFragment;
import com.mehdok.gooder.ui.home.fragments.NotificationsFragment;
import com.mehdok.gooder.ui.home.fragments.StaredItemFragment;
import com.mehdok.gooder.utils.Util;
import com.mehdok.gooder.views.VazirButton;
import com.mehdok.gooder.views.VazirEditText;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AccessCodeListener
{
    private BottomBar mBottomBar;
    private boolean firstRun = true;
    private Dialog loginDialog;
    private UserInfo userInfo = null;
    private ProgressDialog waitingDialog;
    private String mAccessCode;
    private CoordinatorLayout mRootLayout;

    // used for database storage
    private byte[] mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.main_root_layout);

        // set the private encryption key
        handleFirstRun();

        //TODO setup crash reporter and bug feature
        //TODO setup new version reminder

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup add new post
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addNewPost();
            }
        });

        // setup drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // setup left navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // setup bottom navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottom_bar_menu, new OnMenuTabClickListener()
        {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId)
            {
                if (firstRun)
                {
                    // this library call onClick on it's init, so ignore first click
                    firstRun = false;
                    return;
                }

                if (menuItemId == R.id.friends_item)
                {
                    changeView(FriendsItemFragment.getInstance());
                }
                else if (menuItemId == R.id.comments_view)
                {
                    changeView(CommentViewFragment.getInstance());
                }
                else if (menuItemId == R.id.stared_item)
                {
                    changeView(StaredItemFragment.getInstance());
                }
                else if (menuItemId == R.id.notification)
                {
                    changeView(NotificationsFragment.getInstance());
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId)
            {

            }
        });

        //TODO get notification number and set it

        int colorId = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        mBottomBar.mapColorForTab(0, colorId);
        mBottomBar.mapColorForTab(1, colorId);
        mBottomBar.mapColorForTab(2, colorId);
        mBottomBar.mapColorForTab(3, colorId);

        // TODO get new access token then connect
        // TODO check notif number every 15 min to prevent token expire
        userInfo = DatabaseHelper.getInstance(this).getUserInfo();
        if (userInfo != null)
        {
            //TODO set user info
            initFirstView();
        }
        else
        {
            showLoginDialog();
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO
        if (id == R.id.nav_people_you_follow)
        {

        }
        else if (id == R.id.nav_special_item)
        {

        }
        else if (id == R.id.nav_bug_report)
        {

        }
        else if (id == R.id.nav_about_app)
        {

        }
        else if (id == R.id.nav_log_out)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        JsonHandler.getInstance().setAccessCodeListener(this);
    }

    private void addNewPost()
    {

    }

    private void initFirstView()
    {
        // TODO send access data to fragment

//        Bundle bundle = new Bundle();
//        bundle.putString(TAG, id);
//        FriendsItemFragment.getInstance().setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.base_fragment_layout, FriendsItemFragment.getInstance())
                .commit();
    }

    private void changeView(Fragment fragment)
    {
        // TODO add token to fragment via bundle
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void handleFirstRun()
    {
        PreferencesManager pref = new PreferencesManager(this);

        if (pref.isFirstRun())
        {
            // set private key for any encryption, this will run once
            KeyManager keyManager = new KeyManager();
            keyManager.setIv(Util.getDeviceName().getBytes(), this);
            keyManager.setId(Util.getSoftwareInfo().getBytes(), this);

            pref.setFirstRun(false);
        }
    }

    private void showLoginDialog()
    {
        loginDialog = new Dialog(this, R.style.DialogStyle);
        loginDialog.setContentView(R.layout.dialog_login);
        loginDialog.setCancelable(false);
        loginDialog.show();
        loginDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final VazirEditText name = (VazirEditText)loginDialog.findViewById(R.id.login_user_name);
        final VazirEditText password = (VazirEditText)loginDialog.findViewById(R.id.login_password);
        final VazirButton loginButton = (VazirButton)loginDialog.findViewById(R.id.login_do);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean error = false;

                if (name.getText().toString().equals(""))
                {
                    name.setError(getString(R.string.error_required_field));
                    error = true;
                }

                if (password.getText().toString().equals(""))
                {
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

    private void loginWithUserPass(String userName, String password)
    {
        try
        {
            mPassword = Crypto.encrypt(password.getBytes(), this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        waitingDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        waitingDialog.setTitle(R.string.wait_for_server_title);
        waitingDialog.setMessage(getResources().getString(R.string.wait_for_server_body));
        waitingDialog.show();

        requestAccessCode(userName, password);
    }

    private void requestAccessCode(String userName, String password)
    {
        JsonHandler.getInstance().requestAccessCode(this, userName, password);
    }

    @Override
    public void onAccessCodeReceive(String accessCode)
    {
        if (userInfo != null)
        {
            // this is a token renew
            mAccessCode = accessCode;
            waitingDialog.dismiss();

            initFirstView();
        }
        else
        {
            // TODO get user info
        }
    }

    private void putUserInfo(UserInfo userInfo)
    {
        //TODO
    }

    @Override
    public void onAccessCodeFailure(String error)
    {
        Snackbar.make(mRootLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.send_report, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Util.sendBugReport(getApplicationContext());
                    }
                }).show();
    }
}
