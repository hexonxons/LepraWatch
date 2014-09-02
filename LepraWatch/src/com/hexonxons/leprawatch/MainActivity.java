package com.hexonxons.leprawatch;

import org.koroed.lepra.Lepra;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;

import com.hexonxons.leprawatch.fragment.AuthFragment;
import com.hexonxons.leprawatch.fragment.MainFragment;
import com.hexonxons.leprawatch.util.TypefaceSpan;

public class MainActivity extends Activity
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
            getActionBar().hide();
            
            // Load auth fragment.
            if(savedInstanceState == null)
            {
                // Load auth fragment.
                getFragmentManager().beginTransaction().add(R.id.main, new AuthFragment(), AuthFragment.TAG).commit();
            }
        }
        else
        {
            // Request view to fit system windows.
            findViewById(R.id.main).setFitsSystemWindows(true);
            // Create action bar title.
            SpannableString title = new SpannableString(getResources().getString(R.string.app_label));
            title.setSpan(new TypefaceSpan(this, "Bebas.ttf"), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Setup actionbar.
            ActionBar actionBar = getActionBar();
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            
            // Load main fragment.
            if(savedInstanceState == null)
            {
                // Show main fragment.
                getFragmentManager().beginTransaction().add(R.id.main, new MainFragment(), MainFragment.TAG).commit();
            }
        }
    }
}
