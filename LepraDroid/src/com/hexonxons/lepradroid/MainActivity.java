package com.hexonxons.lepradroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hexonxons.lepradroid.fragment.MainFragment;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class MainActivity extends SherlockFragmentActivity
{
    private DrawerLayout mDrawer                        = null;
    private SherlockActionBarDrawerToggle mDrawerToggle = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerToggle = new SherlockActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer, R.string.main_drawer_open, R.string.main_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.main_wrapper, new MainFragment(), MainFragment.TAG).commit();
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
