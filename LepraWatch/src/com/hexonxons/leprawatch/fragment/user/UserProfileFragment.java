package com.hexonxons.leprawatch.fragment.user;

import java.util.Date;

import org.koroed.lepra.content.LepraProfile;
import org.koroed.lepra.content.LepraProfileContact;
import org.koroed.lepra.content.LepraUser;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.system.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileFragment extends Fragment
{
    public static final String TAG      = "UserProfileFragment";
    
    // Lepra user.
    private LepraUser mUser             = null;
    // User profile.
    private LepraProfile mLepraProfile  = null;
    
    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_FAIL:
                {
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_SUCCESS:
                {
                    mLepraProfile = intent.getParcelableExtra(Constants.BUNDLE.KEY_USER_PROFILE);
                    
                    // Remove loading view.
                    ViewGroup wrapper = (ViewGroup) getView().findViewById(R.id.fragment_wrapper);
                    wrapper.removeAllViews();
                    
                    // Inflate profile layout.
                    inflate(getActivity().getLayoutInflater(), wrapper);
                    
                    break;
                }
            }
        }
    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(savedInstanceState == null)
        {
            mUser = getArguments().getParcelable(Constants.BUNDLE.KEY_USER);
        }
        else
        {
            mUser = savedInstanceState.getParcelable(Constants.BUNDLE.KEY_USER);
            mLepraProfile = savedInstanceState.getParcelable(Constants.BUNDLE.KEY_USER_PROFILE);
        }
        
        ViewGroup wrapper = (ViewGroup) inflater.inflate(R.layout.fragment_wrapper, container, false);
        
        if(mLepraProfile == null)
        {
            // Inflate loading layout.
            inflater.inflate(R.layout.loading, wrapper, true);
            // Send request intent.
            Intent intent = new Intent(Constants.INTENT_FILTER.ACTION_GET_PROFILE);
            intent.putExtra(Constants.BUNDLE.KEY_USER, mUser);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        else
        {
            // Inflate profile layout.
            inflate(inflater, wrapper);
        }
        
        return wrapper;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.user_profile_menu, menu);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if(mLepraProfile != null)
        {
            outState.putParcelable(Constants.BUNDLE.KEY_USER_PROFILE, mLepraProfile);
        }
        
        outState.putParcelable(Constants.BUNDLE.KEY_USER, mUser);
    }
    
    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        
        super.onPause();
    }
    
    private void inflate(LayoutInflater inflater, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.user_profile, container, true);
        
        // Async load user image.
        ImageLoader.getInstance().displayImage(mLepraProfile.userPic, (ImageView) view.findViewById(R.id.user_image));
        // Set user login.
        ((RobotoTextView) view.findViewById(R.id.user_login)).setText(mLepraProfile.lepraUser.login);
        ((RobotoTextView) view.findViewById(R.id.user_login)).setTextColor(getResources().getColor(mLepraProfile.lepraUser.gender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500));
        // Set user reg date.
        ((RobotoTextView) view.findViewById(R.id.user_registration_date)).setText("С нами с " + DateFormat.format("dd.MM.yyyy", new Date(mLepraProfile.userRegistrationDate)));
        // Set user parent.
        ((RobotoTextView) view.findViewById(R.id.user_parent)).setText(getResources().getString(mLepraProfile.lepraUser.gender.compareTo("male") == 0 ? R.string.profile_male_parent : R.string.profile_female_parent) + " " + mLepraProfile.userParent);
        // Set user karma.
        ((RobotoTextView) view.findViewById(R.id.user_karma)).setText("" + mLepraProfile.lepraUser.karma);
        // Set user name.
        ((RobotoTextView) view.findViewById(R.id.user_name)).setText(mLepraProfile.userFullName);
        // Set user residence.
        ((RobotoTextView) view.findViewById(R.id.user_residence)).setText(mLepraProfile.userResidence);
        // Set user total written posts and comments.
        ((RobotoTextView) view.findViewById(R.id.user_about_total_written)).setText(mLepraProfile.userTotalWritten);
        // Set user rating.
        ((RobotoTextView) view.findViewById(R.id.user_about_total_rating)).setText(mLepraProfile.userTotalRating);
        // Set user votes count.
        ((RobotoTextView) view.findViewById(R.id.user_about_total_votes)).setText(mLepraProfile.userTotalVotes);
        
        ViewGroup contactsWrapper = (ViewGroup) view.findViewById(R.id.user_contacts_wrapper);
        
        // Set user sites.
        for(LepraProfileContact contact : mLepraProfile.userContacts)
        {
            View contactView = inflater.inflate(R.layout.user_profile_contact_element, contactsWrapper, false);
            
            ((RobotoTextView) contactView.findViewById(R.id.user_profile_contact_element_name)).setText(contact.siteName);
            ((RobotoTextView) contactView.findViewById(R.id.user_profile_contact_element_data)).setText(contact.siteUrl);
            
            contactView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Uri uri = Uri.parse(((RobotoTextView) v.findViewById(R.id.user_profile_contact_element_data)).getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            
            contactsWrapper.addView(contactView);
        }
    }
}
