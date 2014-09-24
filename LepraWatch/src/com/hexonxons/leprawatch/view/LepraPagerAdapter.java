package com.hexonxons.leprawatch.view;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class LepraPagerAdapter extends PagerAdapter
{
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction     = null;
    private Fragment mCurrentPrimaryItem            = null;
    
    public LepraPagerAdapter(FragmentManager fm)
    {
        mFragmentManager = fm;
    }
    
    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);
    
    /**
     * Return the Fragment's tag associated with a specified position.
     */
    public abstract String getItemTag(int position);
    
    @Override
    public void startUpdate(ViewGroup container){}
    
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        if (mCurTransaction == null)
        {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        
        String tag = getItemTag(position);
        
        // Do we already have this fragment?
        Fragment fragment = mFragmentManager.findFragmentByTag(getItemTag(position));
        if(fragment != null)
        {
            mCurTransaction.attach(fragment);
        }
        else
        {
            fragment = getItem(position);
            mCurTransaction.add(container.getId(), fragment, tag);
        }
        if(fragment != mCurrentPrimaryItem)
        {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        
        return fragment;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        if (mCurTransaction == null)
        {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        
        mCurTransaction.detach((Fragment)object);
    }
    
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        Fragment fragment = (Fragment) object;
        
        if (fragment != mCurrentPrimaryItem)
        {
            if (mCurrentPrimaryItem != null)
            {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null)
            {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            
            mCurrentPrimaryItem = fragment;
        }
    }
    
    @Override
    public void finishUpdate(ViewGroup container)
    {
        if (mCurTransaction != null)
        {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return ((Fragment)object).getView() == view;
    }
    
    @Override
    public Parcelable saveState()
    {
        return null;
    }
    
    @Override
    public void restoreState(Parcelable state, ClassLoader loader){}
}