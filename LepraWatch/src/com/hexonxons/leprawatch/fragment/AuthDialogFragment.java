package com.hexonxons.leprawatch.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hexonxons.leprawatch.R;

public class AuthDialogFragment extends DialogFragment
{
    public static final String TAG  = "AuthDialogFragment";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        
        return inflater.inflate(R.layout.auth_dialog, container, false);
    }
    
    @Override
    public void onDestroyView()
    {
        if(getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
      
        super.onDestroyView();
    }
}
