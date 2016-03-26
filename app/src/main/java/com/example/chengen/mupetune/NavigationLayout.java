package com.example.chengen.mupetune;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class NavigationLayout extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerPane;
    private ListView listView;
    private List<NavItem> listNavItems;
    private List<Fragment>listFragments;
    private Bitmap rooms,local,online,help,settings,about;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_layout);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout)findViewById(R.id.drawer_pane);
        listView = (ListView) findViewById(R.id.nav_list);
        listNavItems = new ArrayList<>();
        rooms = BitmapFactory.decodeResource(getResources(),R.drawable.mupetune);
        local = BitmapFactory.decodeResource(getResources(),R.drawable.folder);
        online = BitmapFactory.decodeResource(getResources(),R.drawable.online);
        help = BitmapFactory.decodeResource(getResources(),R.drawable.help);
        settings = BitmapFactory.decodeResource(getResources(),R.drawable.settings);
        about = BitmapFactory.decodeResource(getResources(),R.drawable.infor);
        listNavItems.add(new NavItem("Moop Rooms",rooms));
        listNavItems.add(new NavItem("Local Music",local));
        listNavItems.add(new NavItem("Online Music",online));
        listNavItems.add(new NavItem("Help",help));
        listNavItems.add(new NavItem("Settings",settings));
        listNavItems.add(new NavItem("About...",about));
        listFragments = new ArrayList<>();
        listFragments.add(new LocalMusics());
        NavListAdapter navListAdapter = new NavListAdapter(this,R.layout.activity_nav_item,listNavItems);
        listView.setAdapter(navListAdapter);
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content,listFragments.get(0)).commit();
        setTitle(listNavItems.get(1).getTitle());
        listView.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(position)).commit();
                setTitle(listNavItems.get(position).getTitle());
                listView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_opened,R.string.drawer_closed){
           @Override
            public void onDrawerOpened(View drawerView){
               invalidateOptionsMenu();
               super.onDrawerOpened(drawerView);
               rooms = BitmapFactory.decodeResource(getResources(),R.drawable.mupetune);
               local = BitmapFactory.decodeResource(getResources(),R.drawable.folder);
               online = BitmapFactory.decodeResource(getResources(),R.drawable.online);
               help = BitmapFactory.decodeResource(getResources(),R.drawable.help);
               settings = BitmapFactory.decodeResource(getResources(),R.drawable.settings);
               about = BitmapFactory.decodeResource(getResources(),R.drawable.settings);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
                rooms.recycle();
                local.recycle();
                online.recycle();
                help.recycle();
                settings.recycle();
                about.recycle();
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            actionBarDrawerToggle.syncState();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
