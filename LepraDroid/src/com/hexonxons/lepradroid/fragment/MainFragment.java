package com.hexonxons.lepradroid.fragment;

import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexonxons.lepradroid.MainActivity;
import com.hexonxons.lepradroid.R;
import com.hexonxons.lepradroid.structures.Post;

public class MainFragment extends Fragment
{
    public static final String TAG  = "MainFragment";
    
    private Post[] mPosts = null;
    
    private PopupWindow mActivePostMenu   = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
        ((MainActivity)getActivity()).setDrawerEnabled(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mPosts = mapper.readValue(getResources().getAssets().open("posts.json"), Post[].class);
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        ListView view = (ListView) inflater.inflate(R.layout.main_post_list, container, false);
        view.setAdapter(new MainAdapter());
        
        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        
        if(mActivePostMenu != null)
            mActivePostMenu.dismiss();
    }
    
    private class MainAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mPosts.length;
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
            ViewGroup view = (ViewGroup) convertView;
            
            if(view == null)
            {
                view = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.post_element, parent, false);
                
                ViewHolder holder = new ViewHolder();
                holder.author = (TextView) view.findViewById(R.id.post_element_author);
                holder.date = (TextView) view.findViewById(R.id.post_element_date);
                holder.commentsCount = (TextView) view.findViewById(R.id.post_element_comments_count);
                holder.commentsIcon = (ImageView) view.findViewById(R.id.post_element_comments_icon);
                holder.rating = (TextView) view.findViewById(R.id.post_element_rating);
                holder.text = (TextView) view.findViewById(R.id.post_element_text);
                holder.actions = (ImageView) view.findViewById(R.id.post_element_overflow);
                holder.gold = view.findViewById(R.id.post_element_gold);
                
                view.setTag(holder);
            }
            
            Resources res = getResources();
            
            Post post = mPosts[position];
            
            ViewHolder holder = (ViewHolder) view.getTag();
            
            // Build author text.
            SpannableStringBuilder authorBuilder = new SpannableStringBuilder();
            // Gender text. (Написал/Написала)
            authorBuilder.append((post.user.userGender == 0 ? res.getText(R.string.write_man) : res.getText(R.string.write_woman)) + " ");
            // Rank.
            if(post.user.userRank.length() != 0)
            {
                authorBuilder.append(post.user.userRank + " ");
            }
            int authorStart = authorBuilder.length();
            // Nickname text.
            authorBuilder.append(post.user.userName);
            int authorEnd = authorBuilder.length();
            // Nickname color.
            authorBuilder.setSpan(new ForegroundColorSpan(res.getColor(post.user.userGender == 0 ? R.color.lepra_blue : R.color.lepra_magneta)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Bold style.
            authorBuilder.setSpan(new StyleSpan(Typeface.BOLD), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.author.setText(authorBuilder);
            
            // Date.
            holder.date.setText(post.date);
            
            // Build comments text.
            SpannableStringBuilder commentsBuilder = new SpannableStringBuilder();
            // Amount of comments.
            commentsBuilder.append("" + post.comments);
            // "Comments" text.
            if(post.comments % 10 == 1 && post.comments % 100 != 11)
                commentsBuilder.append(" " + res.getText(R.string.comments_one));
            else if(
                    (post.comments % 10 == 2 || post.comments % 10 == 3 || post.comments % 10 == 4) && 
                    (post.comments % 100 != 12 || post.comments % 100 != 13 || post.comments % 100 != 14))
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_few));
            }
            else
            {
                commentsBuilder.append(" " + res.getText(R.string.comments_text_other));
            }
            
            if(post.newComments > 0)
            {
                holder.commentsIcon.setImageResource(R.drawable.ic_comments_new);
                
                // Build new comments text.
                commentsBuilder.append(" (");
                int commentsStart = commentsBuilder.length();
                commentsBuilder.append("+ " + post.newComments);
                
                if(post.newComments % 10 == 1 && post.newComments % 100 != 11)
                    commentsBuilder.append(" " + res.getText(R.string.comments_one));
                else
                    commentsBuilder.append(" " + res.getText(R.string.comments_other));
                
                int commentsEnd = commentsBuilder.length();
                // New comments color.
                commentsBuilder.setSpan(new ForegroundColorSpan(res.getColor(R.color.lepra_green)), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Bold style.
                commentsBuilder.setSpan(new StyleSpan(Typeface.BOLD), commentsStart, commentsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                commentsBuilder.append(")");
            }
            else
            {
                holder.commentsIcon.setImageResource(R.drawable.ic_comments);
            }
            holder.commentsCount.setText(commentsBuilder);
            
            if(post.isGold)
            {
                holder.rating.setBackgroundColor(res.getColor(R.color.lepra_gold));
                holder.gold.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.rating.setBackgroundColor(res.getColor(R.color.lepra_gray));
                holder.gold.setVisibility(View.GONE);
            }
            holder.rating.setText("" + post.rating);
            
            // TODO: build that too.
            holder.text.setText(post.text);
            
            holder.actions.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mActivePostMenu = new PopupWindow(
                            getActivity().getLayoutInflater().inflate(R.layout.post_menu, null),
                            (int) getResources().getDimension(R.dimen.post_menu_width),
                            LayoutParams.WRAP_CONTENT);
                    
                    mActivePostMenu.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navigation_drawer_background)));
                    mActivePostMenu.setOutsideTouchable(true);
                    mActivePostMenu.showAsDropDown(v, 0, 0, Gravity.RIGHT | Gravity.TOP);
                    
                    mActivePostMenu.setOnDismissListener(new OnDismissListener()
                    {
                        @Override
                        public void onDismiss()
                        {
                            mActivePostMenu = null;
                        }
                    });
                }
            });
            
            return view;
        }
        
        private class ViewHolder
        {
            TextView author         = null;
            TextView date           = null;
            TextView text           = null;
            ImageView commentsIcon  = null;
            TextView commentsCount  = null;
            TextView rating         = null;
            ImageView actions       = null;
            View gold               = null;
        }
    }
}
