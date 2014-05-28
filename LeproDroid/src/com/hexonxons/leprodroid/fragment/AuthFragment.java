package com.hexonxons.leprodroid.fragment;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.hexonxons.leprodroid.R;

public class AuthFragment extends SherlockFragment
{
    public static final String TAG  = "AuthFragment";
    
    private EditText mLoginEditText     = null;
    private EditText mPasswordEditText  = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.auth, container, false);
        
        mLoginEditText = (EditText) mainView.findViewById(R.id.auth_login_edit);
        mPasswordEditText = (EditText) mainView.findViewById(R.id.auth_password_edit);
        
        ((CheckBox)mainView.findViewById(R.id.auth_show_password_check)).setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    mPasswordEditText.setTransformationMethod(null);
                }
                else
                {
                    mPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        
        mainView.findViewById(R.id.auth_login_button).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSherlockActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .add(new MainFragment(), MainFragment.TAG)
                    .addToBackStack(MainFragment.TAG)
                    .commit();
            }
        });
        
        return mainView;
    }
}
