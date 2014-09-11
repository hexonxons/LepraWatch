package com.hexonxons.leprawatch.fragment.user;

import org.koroed.lepra.Lepra;
import org.koroed.lepra.content.LepraUser;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.system.Constants;
import com.hexonxons.leprawatch.view.LepraPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class UserFragment extends Fragment
{
    public static final String TAG  = "UserFragment";
    
    // Lepra user.
    private LepraUser mUser         = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(savedInstanceState == null)
        {
            mUser = Lepra.getInstance().getContext().user;//getArguments().getParcelable(Constants.BUNDLE.KEY_USER);
        }
        else
        {
            mUser = savedInstanceState.getParcelable(Constants.BUNDLE.KEY_USER);
        }
        
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.user_layout, container, false);
        
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.user_pager);
        pager.setAdapter(new UserPagerAdapter(getChildFragmentManager()));
        
        TabPageIndicator tabIndicator = (TabPageIndicator) rootView.findViewById(R.id.user_pager_titles);
        tabIndicator.setViewPager(pager);
        
        return rootView;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(Constants.BUNDLE.KEY_USER, mUser);
    }
    
    private class UserPagerAdapter extends LepraPagerAdapter
    {
        private static final int PAGER_COUNT    = 3;
        private String[] TITLES                 = getActivity().getResources().getStringArray(R.array.user_titles);
        
        public UserPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        
        @Override
        public CharSequence getPageTitle(int position)
        {
            return TITLES[position];
        }
        
        @Override
        public int getCount()
        {
            return PAGER_COUNT;
        }
        
        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                {
                    UserProfileFragment fragment = new UserProfileFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(Constants.BUNDLE.KEY_USER, mUser);
                    fragment.setArguments(args);
                    
                    return fragment;
                }
                
                case 1:
                {
                    return new UserPostsFragment();
                }
                
                case 2:
                {
                    return new UserCommentsFragment();
                }
                
                default:
                {
                    return null;
                }
            }
        }

        @Override
        public String getItemTag(int position)
        {
            switch(position)
            {
                case 0:
                {
                    return UserProfileFragment.TAG;
                }
                
                case 1:
                {
                    return UserPostsFragment.TAG;
                }
                
                case 2:
                {
                    return UserCommentsFragment.TAG;
                }
                
                default:
                {
                    return null;
                }
            }
        }
    }
}
