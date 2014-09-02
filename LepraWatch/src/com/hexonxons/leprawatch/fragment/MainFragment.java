package com.hexonxons.leprawatch.fragment;

import java.util.Random;

import org.koroed.lepra.Lepra;
import org.koroed.lepra.content.LepraContext;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.fragment.user.UserFragment;
import com.hexonxons.leprawatch.system.Constants;
import com.hexonxons.leprawatch.view.DrawerElementView;

public class MainFragment extends Fragment implements OnItemClickListener
{
    public static final String TAG              = "MainFragment";
    // Drawer panel.
    private DrawerLayout mDrawer                = null;
    // Drawer toggle.
    private ActionBarDrawerToggle mDrawerToggle = null;
    // Welcome messages.
    private String[] mWelcomeMessages           = null;
    // Welcome textview
    private TextView mWelcomeText               = null;
    
    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_LOGOUT_FAIL:
                {
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_LOGOUT_SUCCESS:
                {
                    // Workaround with View.setFitsSystemWindows issue.
                    // It seems not possible to request fit system windows without activity restart.
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                    
                    break;
                }
            }
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Get welcome messages list.
        mWelcomeMessages = getResources().getStringArray(R.array.welcome_text);
        
        // Inflate drawer.
        mDrawer = (DrawerLayout) inflater.inflate(R.layout.main_layout, container, false);
        // Create drawer toggle.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, R.drawable.ic_drawer, R.string.main_drawer_open, R.string.main_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                generateWelcomeMessage();
            }
        };
        // Set drawer toggle.
        mDrawer.setDrawerListener(mDrawerToggle);
        
        // Find drawer list.
        ListView drawerList = (ListView) mDrawer.findViewById(R.id.main_drawer_list);
        
        // Inflate header.
        View header = inflater.inflate(R.layout.drawer_header, drawerList, false);
        // Find welcome text.
        mWelcomeText = (TextView) header.findViewById(R.id.drawer_header_text);
        // Set header.
        drawerList.addHeaderView(header);
        // Set click listener.
        drawerList.setOnItemClickListener(this);
        // Set adapter.
        drawerList.setAdapter(new DrawerAdapter());
        
        // Generate welcome message.
        generateWelcomeMessage();
        
        // Load posts fragment.
        if(savedInstanceState == null)
        {
            getChildFragmentManager().beginTransaction().add(R.id.main_wrapper, new PostsFragment(), PostsFragment.TAG).commit();
        }
        
        return mDrawer;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        
        // Sync drawer state. 
        mDrawerToggle.syncState();
        // Fragment have options menu.
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_LOGOUT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_LOGOUT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
    }
    
    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        
        super.onPause();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch(position)
        {
            // About me.
            case 0:
            {
                mDrawer.closeDrawers();
                
                UserFragment fragment = new UserFragment();
                Bundle args = new Bundle();
                args.putParcelable(Constants.BUNDLE.KEY_USER, Lepra.getInstance().getContext().user);
                fragment.setArguments(args);
                
                getChildFragmentManager().beginTransaction().replace(R.id.main_wrapper, fragment, UserFragment.TAG).addToBackStack(UserFragment.TAG).commit();
                break;
            }
            
            // Main.
            case 2:
            {
                mDrawer.closeDrawers();
                
                // Open posts fragment.
                getChildFragmentManager().beginTransaction().replace(R.id.main_wrapper, new PostsFragment(), PostsFragment.TAG).commit();
                
                break;
            }
            
            // Logout.
            case 8:
            {
                mDrawer.closeDrawers();
                
                // Send request intent.
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Constants.INTENT_FILTER.ACTION_LOGOUT));
                
                break;
            }
            
            default:
                break;
        }
    }
    
    private void generateWelcomeMessage()
    {
        // Build welcome message.
        SpannableStringBuilder builder = new SpannableStringBuilder(mWelcomeMessages[new Random().nextInt(mWelcomeMessages.length)]);
        
        LepraContext lepraContext = Lepra.getInstance().getContext();
        
        while(true)
        {
            int start = builder.toString().indexOf("lerpousername");
            int end = start + lepraContext.user.login.length();
                    
            if(start < 0)
                break;
            
            builder.replace(start, start + 13, lepraContext.user.login);
            
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(lepraContext.user.gender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        mWelcomeText.setText(builder);
    }
    
    private class DrawerAdapter extends BaseAdapter
    {
        private String[] mTexts     = getResources().getStringArray(R.array.drawer_list_labels);
        private TypedArray mIcons   = getResources().obtainTypedArray(R.array.drawer_list_icons);
        
        @Override
        public int getCount()
        {
            return mTexts.length;
        }
        
        @Override
        public Object getItem(int position)
        {
            return null;
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            DrawerElementView view = (DrawerElementView) convertView;
            
            if(view == null)
            {
                view = (DrawerElementView) getActivity().getLayoutInflater().inflate(R.layout.drawer_element, parent, false);
            }
            
            switch(position)
            {
                case 3:
                {
                    view.secondaryText.setVisibility(View.VISIBLE);
                    view.secondaryText.setText("125/2236");
                    view.secondaryText.setBackgroundResource(R.color.green_500);
                    break;
                }
                
                case 5:
                {
                    view.secondaryText.setVisibility(View.VISIBLE);
                    view.secondaryText.setText(R.string.navigation_election_live);
                    view.secondaryText.setBackgroundResource(R.color.red_500);
                    break;
                }
                
                default:
                {
                    view.secondaryText.setVisibility(View.GONE);
                    
                    break;
                }
            }
            
            view.icon.setImageDrawable(mIcons.getDrawable(position));
            view.primaryText.setText(mTexts[position]);
            
            return view;
        }
    }
}
