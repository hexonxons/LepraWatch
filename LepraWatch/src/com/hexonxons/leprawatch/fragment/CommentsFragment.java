package com.hexonxons.leprawatch.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.koroed.lepra.content.LepraComment;
import org.koroed.lepra.content.LepraPost;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.robototextview.util.RobotoTextViewUtils;
import com.devspark.robototextview.util.RobotoTypefaceManager;
import com.devspark.robototextview.widget.RobotoTextView;
import com.hexonxons.leprawatch.R;
import com.hexonxons.leprawatch.system.Constants;
import com.hexonxons.leprawatch.view.CommentElementView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class CommentsFragment extends Fragment
{
    public static final String TAG  = "CommentsFragment";
    
    // Comments list.
    private ArrayList<LepraComment> mComments   = null;
    // Today begin time.
    private long mTodayBegin                    = Long.MIN_VALUE;
    // Yesterday begin time.
    private long mYesterdayBegin                = Long.MIN_VALUE;
    // DateFormatter
    private SimpleDateFormat mSimpleDateFormat  = null;
    // Post.
    private LepraPost mLepraPost                = null;
    // Action mode.
    private ActionMode mActionMode              = null;
    // Comments listview
    private ListView mListView                  = null;
    // Current checked position
    private int mCurrentCheckedPosition         = ListView.INVALID_POSITION;
    // Loading view
    private View mLoadingView                   = null;
    // Map offset level to parent comment.
    private SparseIntArray mOffsetMap           = new SparseIntArray();
    
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }
        
        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            // Remove listview selection
            mListView.setItemChecked(ListView.INVALID_POSITION, true);
            // Clear selected position
            mCurrentCheckedPosition = ListView.INVALID_POSITION;
            // Remove action mode.
            mActionMode = null;
        }
        
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.comments_menu, menu);
            return true;
        }
        
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            return false;
        }
    };
    
    private OnItemClickListener mItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            if(mActionMode != null && mCurrentCheckedPosition == position)
            {
                mActionMode.finish();
            }
            else
            {
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                mListView.setItemChecked(position, true);
                mCurrentCheckedPosition = position;
            }
        }
    };
    
    private final BroadcastReceiver mBroadcastReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS:
                {
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS_RESULT_SUCCESS:
                {
                    mComments = intent.getParcelableArrayListExtra(Constants.BUNDLE.KEY_COMMENTS);
                    
                    Calendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    
                    mTodayBegin = calendar.getTime().getTime();
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    mYesterdayBegin = calendar.getTime().getTime();
                    
                    // Remove loading view.
                    mListView.removeFooterView(mLoadingView);
                    mLoadingView = null;
                    
                    // Update listview.
                    ((CommentsAdapter)((HeaderViewListAdapter)mListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
                    
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
        
        mSimpleDateFormat = new SimpleDateFormat("HH:mm");
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Russia/Moscow"));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(savedInstanceState == null)
        {
            mLepraPost = getArguments().getParcelable(Constants.BUNDLE.KEY_POST);
        }
        else
        {
            mLepraPost = savedInstanceState.getParcelable(Constants.BUNDLE.KEY_POST);
            mComments = savedInstanceState.getParcelableArrayList(Constants.BUNDLE.KEY_COMMENTS);
        }
        
        mListView = (ListView) inflater.inflate(R.layout.comments_list, container, false);
        mListView.setAdapter(new CommentsAdapter());
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(mItemClickListener);
        
        mListView.addHeaderView(createPostView());
        
        if(mComments == null)
        {
            // Inflate loading layout.
            mLoadingView = inflater.inflate(R.layout.loading, mListView, false);
            // Add loadind view to listview bottom.
            mListView.addFooterView(mLoadingView, null, false);
            
            // Send request intent.
            Intent intent = new Intent(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS);
            intent.putExtra(Constants.BUNDLE.KEY_POST, mLepraPost);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        
        return mListView;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS_RESULT_FAIL);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS_RESULT_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
        
        if(mListView.getCheckedItemPosition() != ListView.INVALID_POSITION)
        {
            mActionMode = getActivity().startActionMode(mActionModeCallback);
            mCurrentCheckedPosition = mListView.getCheckedItemPosition();
        }
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }
    
    /**
     * {@link https://code.google.com/p/android/issues/detail?id=25994} workaround.
     */
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim)
    {
        if(enter)
        {
            return AnimatorInflater.loadAnimator(getActivity(), android.R.animator.fade_in);
        }
        else
        {
            return AnimatorInflater.loadAnimator(getActivity(), android.R.animator.fade_out);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if(mComments != null)
        {
            outState.putParcelableArrayList(Constants.BUNDLE.KEY_COMMENTS, mComments);
        }
        
        outState.putParcelable(Constants.BUNDLE.KEY_POST, mLepraPost);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }
    
    public View createPostView()
    {
        CommentElementView view = (CommentElementView) getActivity().getLayoutInflater().inflate(R.layout.comments_header, mListView, false);
        
        // Build author text.
        SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
        // Gender text. (Написал/Написала)
        authorBuilder.append((mLepraPost.userGender.compareTo("male") == 0 ? getResources().getText(R.string.write_man) : getResources().getText(R.string.write_woman)) + " ");
        // Rank.
        if(mLepraPost.userTitle.length() != 0)
        {
            authorBuilder.append(mLepraPost.userTitle + " ");
        }
        int authorStart = authorBuilder.length();
        // Nickname text.
        authorBuilder.append(mLepraPost.userLogin);
        int authorEnd = authorBuilder.length();
        // Nickname color.
        authorBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(mLepraPost.userGender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Bold style.
        authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        // Date.
        Date date = new Date(mLepraPost.date);
        if(mLepraPost.date >= mTodayBegin)
        {
            authorBuilder.append(", " + getResources().getText(R.string.date_today) + " " + DateFormat.format("kk:mm", date));
        }
        else if(mLepraPost.date >= mYesterdayBegin)
        {
            authorBuilder.append(", " + getResources().getText(R.string.date_yesterday) + " " + DateFormat.format("kk:mm", date));
        }
        else
        {
            authorBuilder.append(", " + DateFormat.format("yyyy-MM-dd", date) + " в " + DateFormat.format("kk:mm", date));
        }
        
        view.author.setText(authorBuilder);
        view.rating.setText("" + mLepraPost.rating);
        
        TextView text = new TextView(getActivity());
        text.setText(mLepraPost.content);
        view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        
        return view;
    }
    
    private class CommentsAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mComments == null ? 0 : mComments.size();
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
            CommentElementView view = (CommentElementView) convertView;
            
            if(view == null)
            {
                view = (CommentElementView) getActivity().getLayoutInflater().inflate(R.layout.comment_element, parent, false);
            }
            
            view.messageWrapper.removeAllViews();
            
            Resources res = getResources();
            
            LepraComment comment = mComments.get(position);
            
            int offsetLevel = 0;
            
            if(comment.parentId == Integer.MIN_VALUE)
            {
                mOffsetMap.append(comment.id, offsetLevel);
            }
            else
            {
                offsetLevel = mOffsetMap.get(comment.parentId) + 1;
                mOffsetMap.append(comment.id, offsetLevel);
            }
            
            view.offset.setCommentOffset(offsetLevel);
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((comment.userGender.compareTo("male") == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            if(comment.userTitle.length() != 0)
            {
                authorBuilder.append(comment.userTitle + " ");
            }
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(comment.userLogin);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(comment.userGender.compareTo("male") == 0 ? R.color.light_blue_500 : R.color.pink_500)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            // Date.
            Date date = new Date(comment.date);
            if(comment.date >= mTodayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.date_today) + " " + DateFormat.format("kk:mm", date));
            }
            else if(comment.date >= mYesterdayBegin)
            {
                authorBuilder.append(", " + res.getText(R.string.date_yesterday) + " " + DateFormat.format("kk:mm", date));
            }
            else
            {
                authorBuilder.append(", " + DateFormat.format("yyyy-MM-dd", date) + " в " + DateFormat.format("kk:mm", date));
            }
            view.author.setText(authorBuilder);
            
            if(comment.rating > 0)
            {
                view.rating.setText("+" + comment.rating);
                view.rating.setTextColor(getResources().getColor(R.color.green_500));
            }
            else if(comment.rating < 0)
            {
                view.rating.setText("" + comment.rating);
                view.rating.setTextColor(getResources().getColor(R.color.red_500));
            }
            else
            {
                view.rating.setText("" + comment.rating);
                view.rating.setTextColor(getResources().getColor(R.color.lepra_gray));
            }
            
            RobotoTextView text = new RobotoTextView(getActivity());
            RobotoTextViewUtils.setTypeface(text, RobotoTypefaceManager.obtainTypeface(getActivity(), RobotoTypefaceManager.Typeface.ROBOTO_REGULAR));
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            text.setText(comment.content);
            
            view.messageWrapper.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            
            return view;
        }
    }
}
