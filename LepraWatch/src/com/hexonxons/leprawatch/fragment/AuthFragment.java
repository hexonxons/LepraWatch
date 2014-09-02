package com.hexonxons.leprawatch.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.system.Constants;

public class AuthFragment extends Fragment
{
    public static final String TAG  = "AuthFragment";
    
    private EditText mLoginEditText     = null;
    private EditText mPasswordEditText  = null;
    
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_AUTH_FAIL:
                {
                    AuthDialogFragment fragment = (AuthDialogFragment) getActivity().getFragmentManager().findFragmentByTag(AuthDialogFragment.TAG);
                    Dialog authDialog = fragment.getDialog();
                    
                    TextView authDialogMessage = (TextView) authDialog.findViewById(R.id.auth_text);
                    authDialogMessage.setText(intent.getStringExtra(Constants.BUNDLE.KEY_AUTH));
                    
                    authDialog.setCancelable(true);
                    authDialog.setCanceledOnTouchOutside(true);
                    
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_AUTH_SUCCESS:
                {
                    AuthDialogFragment fragment = (AuthDialogFragment) getActivity().getFragmentManager().findFragmentByTag(AuthDialogFragment.TAG);
                    fragment.dismiss();
                    
                    // Show auth dialog.
                    AuthDialogFragment dialog = new AuthDialogFragment();
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getFragmentManager(), AuthDialogFragment.TAG);
                    
                    // Workaround with View.setFitsSystemWindows issue.
                    // It seems not possible to request fit system windows without activity restart.
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                    
                    break;
                }
                
                default:
                {
                    break;
                }
            }
        }
    };
    
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
                // Show auth dialog.
                AuthDialogFragment dialog = new AuthDialogFragment();
                dialog.setCancelable(false);
                dialog.show(getActivity().getFragmentManager(), AuthDialogFragment.TAG);
                
                // Send auth intent.
                Intent intent = new Intent(Constants.INTENT_FILTER.ACTION_AUTH);
                intent.putExtra(Constants.BUNDLE.KEY_USERNAME, mLoginEditText.getText().toString());
                intent.putExtra(Constants.BUNDLE.KEY_PASSWORD, mPasswordEditText.getText().toString());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });
        
        return mainView;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_AUTH_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_AUTH_SUCCESS);
        
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
    }
}
