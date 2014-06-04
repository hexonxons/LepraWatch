package com.hexonxons.lepradroid.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hexonxons.lepradroid.MainActivity;
import com.hexonxons.lepradroid.R;
import com.hexonxons.lepradroid.util.CONSTANTS;

public class AuthFragment extends Fragment
{
    public static final String TAG  = "AuthFragment";
    
    private EditText mLoginEditText     = null;
    private EditText mPasswordEditText  = null;
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        
        ((MainActivity)getActivity()).setDrawerEnabled(false);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.auth, container, false);
        
        mLoginEditText = (EditText) mainView.findViewById(R.id.auth_login_edit);
        mPasswordEditText = (EditText) mainView.findViewById(R.id.auth_password_edit);
        
        mainView.findViewById(R.id.auth_login_button).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mLoginEditText.getText().toString().length() != 0)
                {
                    SharedPreferences preferences = getActivity().getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    Editor editor = preferences.edit();
                    editor.putString(CONSTANTS.SHARED_PREFERENCES_USERNAME, mLoginEditText.getText().toString());
                    editor.commit();
                    
                    getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_wrapper, new MainFragment(), MainFragment.TAG)
                        .commit();
                }
            }
        });
        
        return mainView;
    }
}
