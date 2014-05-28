package com.hexonxons.leprodroid;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class MainActivity extends SherlockActivity
{
    private DrawerLayout mDrawer                        = null;
    private SherlockActionBarDrawerToggle mDrawerToggle = null;
    private ExpandableListView mDrawerList              = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerToggle = new SherlockActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer_light, R.string.main_drawer_open, R.string.main_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        
        mDrawerList = (ExpandableListView) mDrawer.findViewById(R.id.main_drawer_panel_list);
        mDrawerList.setAdapter(new NavigationDrawerAdapter());
        mDrawerList.setOnGroupClickListener(new OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                return true;
            }
        });
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
    
    public class NavigationDrawerAdapter extends BaseExpandableListAdapter
    {
        private static final int GROUP_ITEM_COUNT       = 5;
        
        private final int[] ICONS       = {R.drawable.ic_star, R.drawable.ic_message, R.drawable.ic_subsite, R.drawable.ic_settings, R.drawable.ic_logout};
        private final String[] TEXTS    = {"Избранное",        "Инбокс",              "Подлепры",            "Настройки",            "Выход"};
        
        private LayoutInflater mInflater    = null;
        
        public NavigationDrawerAdapter()
        {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        @Override
        public int getGroupCount()
        {
            return GROUP_ITEM_COUNT;
        }
        
        @Override
        public int getChildrenCount(int groupPosition)
        {
            return groupPosition == 2 ? TEXTS.length : 0;
        }
        
        @Override
        public Object getGroup(int groupPosition)
        {
            return null;
        }
        
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            return null;
        }
        
        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }
        
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return groupPosition;
        }
        
        @Override
        public boolean hasStableIds()
        {
            return true;
        }
        
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            ViewGroup view = (ViewGroup) convertView;
            
            if(view == null)
            {
                view = (ViewGroup) mInflater.inflate(R.layout.main_drawer_list_group_element, parent, false);
                
                GroupViewHolder holder = new GroupViewHolder();
                holder.image = (ImageView) view.findViewById(R.id.main_drawer_list_group_element_image);
                holder.text = (TextView) view.findViewById(R.id.main_drawer_list_group_element_text);
                holder.indicator = (ImageView) view.findViewById(R.id.main_drawer_list_group_element_indicator);
                
                view.setTag(holder);
            }
            
            GroupViewHolder holder = (GroupViewHolder) view.getTag();
            
            holder.image.setImageResource(ICONS[groupPosition]);
            holder.text.setText(TEXTS[groupPosition]);
            
            if(groupPosition == 2)
            {
                holder.indicator.setVisibility(View.VISIBLE);
                if(isExpanded)
                    holder.indicator.setImageResource(R.drawable.ic_indicator_hide);
                else
                    holder.indicator.setImageResource(R.drawable.ic_indicator_show);
            }
            else
            {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
            
            return view;
        }
        
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            ViewGroup view = (ViewGroup) convertView;
            
            if(view == null)
            {
                view = (ViewGroup) mInflater.inflate(R.layout.main_drawer_list_child_element, parent, false);
                
                ChildViewHolder holder = new ChildViewHolder();
                holder.text = (TextView) view.findViewById(R.id.main_drawer_list_child_element_text);
                
                view.setTag(holder);
            }
            
            ChildViewHolder holder = (ChildViewHolder) view.getTag();
            
            holder.text.setText(TEXTS[groupPosition]);
            
            return view;
        }
        
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }
        
        private class GroupViewHolder
        {
            public ImageView image      = null;
            public TextView text        = null;
            public ImageView indicator  = null;
        }
        
        private class ChildViewHolder
        {
            public TextView     text = null;
        }
    }
}
