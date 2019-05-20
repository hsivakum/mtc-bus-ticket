package com.example.hariharansivakumar.tike;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private BottomBar mBottomBar;
    private FragNavController fragNavController;


    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    private final int TAB_FOURTH = FragNavController.TAB4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        //FragNav
        //list of fragments
        List<Fragment> fragments = new ArrayList<>(3);

        //add fragments to list
        fragments.add(HomeFragment.newInstance(0));
        fragments.add(WalletFragment.newInstance(0));
        fragments.add(HistoryFragment.newInstance(0));
        fragments.add(ProfileFragment.newInstance(0));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);
        //End of FragNav

        //BottomBar menu
        //mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar = findViewById(R.id.bottomBar);
        mBottomBar.setItems(R.menu.navigation);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //switch between tabs
                switch (menuItemId) {
                    case R.id.navigation_home:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.navigation_wallet:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.navigation_history:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                    case R.id.navigation_profile:
                        fragNavController.switchTab(TAB_FOURTH);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.navigation_home) {
                    fragNavController.clearStack();
                }
            }
        });
        //End of BottomBar menu

        //Navigation drawer
        new DrawerBuilder().withActivity(this).build();

        //primary items
        PrimaryDrawerItem home = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Home")
                .withIcon(R.drawable.ic_home_black_24dp);
        PrimaryDrawerItem primary_item1 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Wallet")
                .withIcon(R.drawable.ic_dashboard_black_24dp);
        PrimaryDrawerItem primary_item2 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("History")
                .withIcon(R.drawable.ic_notifications_black_24dp);
        PrimaryDrawerItem primary_item3 = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Profile")
                .withIcon(R.drawable.ic_account_box_black_24dp);
        //secondary items
        SecondaryDrawerItem secondary_item1 = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(11)
                .withName("Get Timings")
                .withIcon(R.drawable.ic_looks_one_black_24dp);
        SecondaryDrawerItem secondary_item2 = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(12)
                .withName("Change Password")
                .withIcon(R.drawable.ic_looks_two_black_24dp);
        //settings, help, contact items
        SecondaryDrawerItem settings = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(13)
                .withName("Settings")
                .withIcon(R.drawable.ic_settings_black_24dp);
        SecondaryDrawerItem help = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(14)
                .withName("Help")
                .withIcon(R.drawable.help);
        SecondaryDrawerItem contact = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(15)
                .withName("Contact")
                .withIcon(R.drawable.ic_contact_mail_black_24dp);
        SecondaryDrawerItem about = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(16)
                .withName("About")
                .withIcon(R.drawable.ic_info_black_24dp);

        //Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withFullscreen(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        home,
                        primary_item1,
                        primary_item2,
                        primary_item3,
                        new SectionDrawerItem().withName("Settings"),
                        secondary_item1,
                        secondary_item2,
                        new DividerDrawerItem(),
                        settings,
                        help,
                        contact,
                        about

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                fragNavController.switchTab(TAB_FIRST);
                            } else if (drawerItem.getIdentifier() == 2) {
                                fragNavController.switchTab(TAB_SECOND);
                            } else if (drawerItem.getIdentifier() == 3) {
                                fragNavController.switchTab(TAB_THIRD);
                            }else if (drawerItem.getIdentifier() == 4) {
                                fragNavController.switchTab(TAB_FOURTH);
                            } else if (drawerItem.getIdentifier() == 11) {

                            } else if (drawerItem.getIdentifier() == 12) {

                            } else if (drawerItem.getIdentifier() == 13) {

                            } else if (drawerItem.getIdentifier() == 14) {
                                //intent = new Intent(Main2Activity.this, Settings.class);
                            } else if (drawerItem.getIdentifier() == 15) {
                                //intent = new Intent(Main2Activity.this, Help.class);
                            } else if (drawerItem.getIdentifier() == 16) {
                                // intent = new Intent(Main2Activity.this, Contact.class);
                            }
                            if (intent != null) {
                                Main2Activity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();
        //End of Navigation drawer

    }
    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

}