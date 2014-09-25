package com.hexonxons.leprawatch.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import org.koroed.lepra.Lepra;
import org.koroed.lepra.content.LepraContext;
import org.koroed.lepra.content.LepraPost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.fragment.user.UserFragment;
import com.hexonxons.leprawatch.system.Constants;
import com.hexonxons.leprawatch.view.DrawerElementView;
import com.hexonxons.leprawatch.view.PostElementView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class MainFragment extends Fragment
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
    // Posts list.
    private ArrayList<LepraPost> mPosts         = null;
    // Today begin time.
    private long mTodayBegin                    = Long.MIN_VALUE;
    // Yesterday begin time.
    private long mYesterdayBegin                = Long.MIN_VALUE;
    // DateFormatter
    private SimpleDateFormat mSimpleDateFormat  = null;
    // Drawer runnable.
    private Runnable mDrawerRunnable            = null;
    
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
                
                case Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_FAIL:
                {
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_SUCCESS:
                {
                    mPosts = intent.getParcelableArrayListExtra(Constants.BUNDLE.KEY_POSTS);
                    
                    Calendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    
                    mTodayBegin = calendar.getTime().getTime();
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    mYesterdayBegin = calendar.getTime().getTime();
                    
                    // Remove loading view.
                    ViewGroup wrapper = (ViewGroup) getView().findViewById(R.id.posts_wrapper);
                    wrapper.removeAllViews();
                    
                    // Inflate posts layout.
                    getActivity().getLayoutInflater().inflate(R.layout.posts_list, wrapper, true);
                    
                    ListView listView = (ListView) wrapper.findViewById(R.id.posts_list);
                    listView.setAdapter(new PostsAdapter());
                    listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
                    listView.setOnItemClickListener(mPostItemClickListener);
                    
                    // Invalidate ab menu after posts is loaded.
                    getActivity().supportInvalidateOptionsMenu();
                    
                    break;
                }
            }
        }
    };
    
    private OnItemClickListener mPostItemClickListener  = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // Disable toggle icon.
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            
            // Run comments fragment.
            Bundle args = new Bundle();
            args.putParcelable(Constants.BUNDLE.KEY_POST, mPosts.get(position));
            
            CommentsFragment fragment = new CommentsFragment();
            fragment.setArguments(args);
            
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment, CommentsFragment.TAG)
                .addToBackStack(CommentsFragment.TAG)
                .commit();
        }
    };
    
    private OnItemClickListener mDrawerItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            switch(position)
            {
                // About me.
                case 0:
                {
                    // Close drawer panel.
                    mDrawer.closeDrawers();
                    // Create drawer action to run after drawer close.
                    mDrawerRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Disable toggle icon.
                            mDrawerToggle.setDrawerIndicatorEnabled(false);
                            
                            // Open user profile fragment.
                            getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main, new UserFragment(), UserFragment.TAG)
                                .addToBackStack(UserFragment.TAG)
                                .commit();
                        }
                    };
                    
                    break;
                }
                
                // Logout.
                case 7:
                {
                    // Close drawer panel.
                    mDrawer.closeDrawers();
                    // Create drawer action to run after drawer close.
                    mDrawerRunnable = new Runnable()
                    {
                        public void run()
                        {
                            // Disable toggle icon.
                            mDrawerToggle.setDrawerIndicatorEnabled(false);
                            
                            // Send request intent.
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Constants.INTENT_FILTER.ACTION_LOGOUT));
                        }
                    };
                    
                    break;
                }
                
                default:
                    break;
            }
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Fragment have options menu.
        setHasOptionsMenu(true);
        
        // Load posts list.
        if(savedInstanceState != null)
        {
            mPosts = savedInstanceState.getParcelableArrayList(Constants.BUNDLE.KEY_POSTS);
        }
        
        mSimpleDateFormat = new SimpleDateFormat("HH:mm");
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Russia/Moscow"));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Get welcome messages list.
        mWelcomeMessages = getResources().getStringArray(R.array.welcome_text);
        
        // Inflate drawer.
        mDrawer = (DrawerLayout) inflater.inflate(R.layout.posts_layout, container, false);
        // Create drawer toggle.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, R.drawable.ic_drawer, R.string.main_drawer_open, R.string.main_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                generateWelcomeMessage();
                
                if(mDrawerRunnable != null)
                {
                    mDrawerRunnable.run();
                    mDrawerRunnable = null;
                }
            }
            
            @Override
            public void onDrawerStateChanged(int newState)
            {
                // Drop runnable if user intercept drawer before it has been closed.
                if(newState == DrawerLayout.STATE_DRAGGING)
                {
                    mDrawerRunnable = null;
                }
            }
        };
        // Set drawer toggle.
        mDrawer.setDrawerListener(mDrawerToggle);
        
        // Find drawer list.
        ListView drawerList = (ListView) mDrawer.findViewById(R.id.drawer_list);
        
        // Inflate header.
        View header = inflater.inflate(R.layout.drawer_header, drawerList, false);
        // Find welcome text.
        mWelcomeText = (TextView) header.findViewById(R.id.drawer_header_text);
        // Set header.
        drawerList.addHeaderView(header);
        // Set click listener.
        drawerList.setOnItemClickListener(mDrawerItemClickListener);
        // Set adapter.
        drawerList.setAdapter(new DrawerAdapter());
        
        // Generate welcome message.
        generateWelcomeMessage();
        
        ViewGroup wrapper = (ViewGroup) mDrawer.findViewById(R.id.posts_wrapper);
        
        if(mPosts == null)
        {
            // Inflate loading layout.
            inflater.inflate(R.layout.loading, wrapper, true);
            
            // TODO: Fix that.
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    // Send request intent.
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Constants.INTENT_FILTER.ACTION_GET_POSTS));
                }
            });
            
        }
        else
        {
            // Inflate posts list.
            inflater.inflate(R.layout.posts_list, wrapper, true);
            
            ListView listView = (ListView) wrapper.findViewById(R.id.posts_list);
            listView.setAdapter(new PostsAdapter());
            listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
            listView.setOnItemClickListener(mPostItemClickListener);
        }
        
        return mDrawer;
    }
    
    /**
     * {@link https://code.google.com/p/android/issues/detail?id=25994} workaround.
     */
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim)
    {
        if(enter)
        {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        else
        {
            return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        }
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        // Sync drawer state. 
        mDrawerToggle.syncState();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_LOGOUT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_LOGOUT_SUCCESS);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if(mPosts != null)
        {
            inflater.inflate(R.menu.main_menu, menu);
        }
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
        
        switch (item.getItemId())
        {
            case R.id.action_new_post:
            {
                // Disable toggle icon.
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                
                // Run edit fragment.
                EditFragment fragment = new EditFragment();
                
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, fragment, EditFragment.TAG)
                    .addToBackStack(EditFragment.TAG)
                    .commit();
                
                return true;
            }
            
            default:
            {
                break;
            }
        }
        
        return false;
    }
    
    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        
        super.onPause();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if(mPosts != null)
        {
            outState.putParcelableArrayList(Constants.BUNDLE.KEY_POSTS, mPosts);
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
    
    private class PostsAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPosts.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return position;
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Resources res = getResources();
            
            final LepraPost post = mPosts.get(position);
            
            PostElementView view = (PostElementView) convertView;
            
            if(view == null)
            {
                view = (PostElementView) getActivity().getLayoutInflater().inflate(R.layout.post_element, parent, false);
            }
            
            view.messageWrapper.removeAllViews();
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((post.userGender.compareTo("male") == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            if(post.userTitle.length() != 0)
            {
                authorBuilder.append(post.userTitle + " ");
            }
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(post.userLogin);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(post.userGender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Date.
            Date date = new Date(post.date);
            if(post.date >= mTodayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.date_today) + " " + DateFormat.format("kk:mm", date));
            }
            else if(post.date >= mYesterdayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.date_yesterday) + " " + DateFormat.format("kk:mm", date));
            }
            else
            {
                authorBuilder.append(", " + DateFormat.format("yyyy-MM-dd", date) + " в " + DateFormat.format("kk:mm", date));
            }
            
            view.author.setText(authorBuilder);
            
            // Build comments text.
            SpannableStringBuilder commentsBuilder = new SpannableStringBuilder();
            // Amount of comments.
            commentsBuilder.append("" + post.totalCommentCnt);
            // "Comments" text.
            /*if(post.totalCommentCnt % 10 == 1 && post.totalCommentCnt % 100 != 11)
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_one));
            }
            else if(
                    (post.totalCommentCnt % 10 == 2 || post.totalCommentCnt % 10 == 3 || post.totalCommentCnt % 10 == 4) && 
                    (post.totalCommentCnt % 100 != 12 || post.totalCommentCnt % 100 != 13 || post.totalCommentCnt % 100 != 14))
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_few));
            }
            else
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_other));
            }*/
            
            //if(post.newCommentCnt > 0)
            if(post.newCommentCnt != null && post.newCommentCnt.length() != 0)
            {
                view.commentsIcon.setImageResource(R.drawable.ic_comments_new);
                
                // Build new comments text.
                commentsBuilder.append(" (");
                int commentsStart = commentsBuilder.length();
                commentsBuilder.append("+ " + post.newCommentCnt);
                
                /*if(post.newCommentCnt % 10 == 1 && post.newCommentCnt % 100 != 11)
                    commentsBuilder.append(" " + res.getText(R.string.comments_one));
                else
                    commentsBuilder.append(" " + res.getText(R.string.comments_other));
                */
                int commentsEnd = commentsBuilder.length();
                // New comments color.
                commentsBuilder.setSpan(new ForegroundColorSpan(res.getColor(R.color.green_500)), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Bold style.
                commentsBuilder.setSpan(new StyleSpan(Typeface.BOLD), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                commentsBuilder.append(")");
            }
            else
            {
                view.commentsIcon.setImageResource(R.drawable.ic_comments);
            }
            view.commentsCount.setText(commentsBuilder);
            
            if(post.isGold)
            {
                view.rating.setBackgroundColor(res.getColor(R.color.amber_500));
                view.infoWrapper.setBackgroundResource(R.drawable.post_gold);
            }
            else
            {
                view.rating.setBackgroundColor(res.getColor(R.color.lepra_gray));
                view.infoWrapper.setBackgroundResource(R.drawable.post_default);
            }
            view.rating.setText("" + post.rating);
            
            /*SpannableStringBuilder messageBuilder = new SpannableStringBuilder();
            
            // Build post message.
            for(final Message message : post.message)
            {
                if(message.image == null)
                {
                    int messageStart = messageBuilder.length();
                    messageBuilder.append(message.text);
                    int messageEnd = messageBuilder.length();
                    
                    if(message.link != null)
                    {
                        messageBuilder.setSpan(new URLSpan(message.link), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    
                    if(message.color != Integer.MAX_VALUE)
                    {
                        messageBuilder.setSpan(new ForegroundColorSpan(message.color), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    
                    if(message.typeface != null)
                    {
                        if(message.typeface.compareTo("italic") == 0)
                        {
                            messageBuilder.setSpan(new StyleSpan(Typeface.ITALIC), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        else if(message.typeface.compareTo("bold") == 0)
                        {
                            messageBuilder.setSpan(new StyleSpan(Typeface.BOLD), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                else
                {
                    // Add text.
                    if(messageBuilder.length() != 0)
                    {
                        TextView text = new TextView(getActivity());
                        text.setText(messageBuilder);
                        view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        
                        messageBuilder = new SpannableStringBuilder();
                    }
                    
                    // Add image
                    final ImageView image = new ImageView(getActivity());
                    image.setScaleType(ScaleType.CENTER_INSIDE);
                    view.messageWrapper.addView(image, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    view.messageWrapper.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ImageLoader.getInstance().displayImage("assets://" + message.image, image);
                        }
                    });
                }
            }
            
            // Add text.
            if(messageBuilder.length() != 0)
            {
                TextView text = new TextView(getActivity());
                text.setText(messageBuilder);
                view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }*/
            
            TextView text = new TextView(getActivity());
            text.setText(post.content);
            view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            
            return view;
        }
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
