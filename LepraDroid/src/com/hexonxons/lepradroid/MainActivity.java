package com.hexonxons.lepradroid;

import org.koroed.lepra.Lepra;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;

import com.hexonxons.lepradroid.fragment.AuthFragment;
import com.hexonxons.lepradroid.fragment.MainFragment;
import com.hexonxons.lepradroid.util.TypefaceSpan;

public class MainActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Set view wrapper.
        setContentView(R.layout.main);
        
        // Load auth fragment if not authorized.
        if(Lepra.getInstance().isAuthorized() == false)
        {
            // Request view to not to fit system windows.
            findViewById(R.id.main).setFitsSystemWindows(false);
            // Hide action bar
            getSupportActionBar().hide();
            
            // Load auth fragment.
            if(savedInstanceState == null)
            {
                // Load auth fragment.
                getSupportFragmentManager().beginTransaction().add(R.id.main, new AuthFragment(), AuthFragment.TAG).commit();
            }
        }
        else
        {
            // Request view to fit system windows.
            findViewById(R.id.main).setFitsSystemWindows(true);
            // Create action bar title.
            SpannableString title = new SpannableString("LEPRADROID");
            title.setSpan(new TypefaceSpan(this, "Bebas.ttf"), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Setup actionbar.
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            
            // Load main fragment.
            if(savedInstanceState == null)
            {
                // Show main fragment.
                getSupportFragmentManager().beginTransaction().add(R.id.main, new MainFragment(), MainFragment.TAG).commit();
            }
        }
    }
}
