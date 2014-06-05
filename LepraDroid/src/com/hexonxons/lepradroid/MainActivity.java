package com.hexonxons.lepradroid;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexonxons.lepradroid.fragment.AuthFragment;
import com.hexonxons.lepradroid.fragment.MainFragment;
import com.hexonxons.lepradroid.structures.SimpeUser;
import com.hexonxons.lepradroid.util.CONSTANTS;
import com.hexonxons.lepradroid.util.TypefaceSpan;

public class MainActivity extends ActionBarActivity
{
    private DrawerLayout mDrawer                        = null;
    private ActionBarDrawerToggle mDrawerToggle         = null;
    
    private SimpeUser mUser                             = null;
    private String[] mWelcomeMessages                   = null;
    private TextView mWelcomeTextView                   = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        mWelcomeMessages = getResources().getStringArray(R.array.welcome_text);
        
        try
        {
            mUser = new ObjectMapper().readValue(getResources().getAssets().open("user.json"), SimpeUser.class);
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        setContentView(R.layout.main);
        
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer, R.string.main_drawer_open, R.string.main_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                generateWelcomeMessage();
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        
        // Find welcome textview.
        mWelcomeTextView = ((TextView)mDrawer.findViewById(R.id.main_drawer_panel_welcome));
        
        // Set logout listener.
        mDrawer.findViewById(R.id.main_drawer_panel_bottom_logout).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Pop back stack.
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                
                // Remove user data.
                SharedPreferences preferences = getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                Editor editor = preferences.edit();
                editor.putString(CONSTANTS.SHARED_PREFERENCES_USERNAME, null);
                editor.commit();
                
                // Hide action bar.
                setActionBarEnabled(false);
                
                getSupportFragmentManager().beginTransaction().replace(R.id.main_wrapper, new AuthFragment(), AuthFragment.TAG).commit();
            }
        });
        
        generateWelcomeMessage();
        
        // Get saved username.
        SharedPreferences preferences = getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String userName = preferences.getString(CONSTANTS.SHARED_PREFERENCES_USERNAME, null);
        
        if(userName == null || userName.length() == 0)
        {
            setActionBarEnabled(false);
            // Load auth fragment.
            if(savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.main_wrapper, new AuthFragment(), AuthFragment.TAG).commit();
            }
        }
        else
        {
            setActionBarEnabled(true);
            // Load main fragment.
            if(savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.main_wrapper, new MainFragment(), MainFragment.TAG).commit();
            }
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
    
    private void generateWelcomeMessage()
    {
        // Build welcome message.
        SpannableStringBuilder builder = new SpannableStringBuilder(mWelcomeMessages[new Random().nextInt(mWelcomeMessages.length)]);
        
        while(true)
        {
            int start = builder.toString().indexOf("lerpousername");
            int end = start + mUser.userName.length();
                    
            if(start < 0)
                break;
            
            builder.replace(start, start + 13, mUser.userName);
            
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(mUser.userGender == 0 ? R.color.lepra_blue : R.color.lepra_magneta)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        mWelcomeTextView.setText(builder);
    }
    
    public void setDrawerEnabled(boolean enabled)
    {
        if(enabled)
        {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else
        {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
    
    public void setActionBarEnabled(boolean enabled)
    {
        if(enabled)
        {
            // Setup action bar.
            SpannableString title = new SpannableString("LEPRADROID");
            title.setSpan(new TypefaceSpan(this, "Bebas.ttf"), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }
        else
        {
            // No saved user. Hide action bar.
            getSupportActionBar().hide();
        }
    }
}
