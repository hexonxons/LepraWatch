package com.hexonxons.leprawatch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hexonxons.leprawatch.R;

public class EditFragment extends Fragment
{
    public static final String TAG  = "EditFragment";
    
    private EditText mEditText      = null;
    
    private ImageButton mBold           = null;
    private ImageButton mItalic         = null;
    private ImageButton mIrony          = null;
    private ImageButton mUnderline      = null;
    private ImageButton mSubscript      = null;
    private ImageButton mSuperscript    = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.edit_layout, container, false);
        
        mBold = (ImageButton) rootView.findViewById(R.id.edit_bold);
        mItalic = (ImageButton) rootView.findViewById(R.id.edit_italic);
        mIrony = (ImageButton) rootView.findViewById(R.id.edit_irony);
        mUnderline = (ImageButton) rootView.findViewById(R.id.edit_underline);
        mSubscript = (ImageButton) rootView.findViewById(R.id.edit_subscript);
        mSuperscript = (ImageButton) rootView.findViewById(R.id.edit_superscript);
        
        mEditText = (EditText) rootView.findViewById(R.id.edit_content);
        mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback()
        {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false;
            }
            
            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
                mBold.setVisibility(View.GONE);
                mItalic.setVisibility(View.GONE);
                mIrony.setVisibility(View.GONE);
                mUnderline.setVisibility(View.GONE);
                mSubscript.setVisibility(View.GONE);
                mSuperscript.setVisibility(View.GONE);
            }
            
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                mode.setTitle("");
                
                Animation expand = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
                
                mBold.setVisibility(View.VISIBLE);
                mItalic.setVisibility(View.VISIBLE);
                mIrony.setVisibility(View.VISIBLE);
                mUnderline.setVisibility(View.VISIBLE);
                mSubscript.setVisibility(View.VISIBLE);
                mSuperscript.setVisibility(View.VISIBLE);
                
                mBold.startAnimation(expand);
                mItalic.startAnimation(expand);
                mIrony.startAnimation(expand);
                mUnderline.startAnimation(expand);
                mSubscript.startAnimation(expand);
                mSuperscript.startAnimation(expand);
                
                return true;
            }
            
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                return false;
            }
        });
        
        return rootView;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                FragmentActivity activity = getActivity();
                
                // Hide keyboard.
                InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
                View focus = activity.getCurrentFocus();
                if(focus != null)
                {
                    inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                }
                
                // Go back.
                activity.getSupportFragmentManager().popBackStack();
                
                return true;
            }
            
            default:
            {
                break;
            }
        }
        
        return super.onOptionsItemSelected(item);
    }
}
