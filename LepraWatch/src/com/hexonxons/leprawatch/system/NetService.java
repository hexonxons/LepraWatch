package com.hexonxons.leprawatch.system;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.koroed.lepra.Lepra;
import org.koroed.lepra.content.LepraComment;
import org.koroed.lepra.content.LepraPost;
import org.koroed.lepra.content.LepraProfile;
import org.koroed.lepra.content.LepraUser;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class NetService extends Service
{
    private final SynchronousQueue<Runnable> mMultiThreadQueue      = new SynchronousQueue<Runnable>();
    private final ThreadPoolExecutor mMultiThreadExecutor           = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, mMultiThreadQueue);
    
    private Multimap<String, String> mPendingTasks  = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, String>create());
    private Multimap<String, String> mRunningTasks  = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, String>create());
    
    public interface NetworkInterface
    {
        public boolean isPending(String action, String id);
        public boolean isRunning(String action, String id);
    }
    
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, final Intent intent)
        {
            switch(intent.getAction())
            {
                case Constants.INTENT_FILTER.ACTION_AUTH:
                {
                    // Check if task is already queued or running
                    if(mPendingTasks.get(Constants.INTENT_FILTER.ACTION_AUTH).size() == 0 && mRunningTasks.get(Constants.INTENT_FILTER.ACTION_AUTH).size() == 0)
                    {
                        mPendingTasks.put(Constants.INTENT_FILTER.ACTION_AUTH, Constants.INTENT_FILTER.ACTION_AUTH);
                        
                        mMultiThreadExecutor.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPendingTasks.removeAll(Constants.INTENT_FILTER.ACTION_AUTH);
                                mRunningTasks.put(Constants.INTENT_FILTER.ACTION_AUTH, Constants.INTENT_FILTER.ACTION_AUTH);
                                
                                String username = intent.getStringExtra(Constants.BUNDLE.KEY_USERNAME);
                                String password = intent.getStringExtra(Constants.BUNDLE.KEY_PASSWORD);
                                
                                Intent result = null;
                                
                                try
                                {
                                    Lepra.getInstance().login(username, password, true);
                                    
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_AUTH_SUCCESS);
                                }
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_AUTH_FAIL);
                                }
                                finally
                                {
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
                                    mRunningTasks.removeAll(Constants.INTENT_FILTER.ACTION_AUTH);
                                }
                            }
                        });
                    }
                    
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_LOGOUT:
                {
                    // Check if task is already queued or running
                    if(mPendingTasks.get(Constants.INTENT_FILTER.ACTION_LOGOUT).size() == 0 && mRunningTasks.get(Constants.INTENT_FILTER.ACTION_LOGOUT).size() == 0)
                    {
                        mPendingTasks.put(Constants.INTENT_FILTER.ACTION_LOGOUT, Constants.INTENT_FILTER.ACTION_LOGOUT);
                        
                        mMultiThreadExecutor.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPendingTasks.removeAll(Constants.INTENT_FILTER.ACTION_LOGOUT);
                                mRunningTasks.put(Constants.INTENT_FILTER.ACTION_LOGOUT, Constants.INTENT_FILTER.ACTION_LOGOUT);
                                Intent result = null;
                                
                                try
                                {
                                    Lepra.getInstance().logout(getApplicationContext());
                                    
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_LOGOUT_SUCCESS);
                                }
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_LOGOUT_FAIL);
                                }
                                finally
                                {
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
                                    mRunningTasks.removeAll(Constants.INTENT_FILTER.ACTION_LOGOUT);
                                }
                            }
                        });
                    }
                    
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_POSTS:
                {
                    // Check if task is already queued or running
                    if(mPendingTasks.get(Constants.INTENT_FILTER.ACTION_GET_POSTS).size() == 0 && mRunningTasks.get(Constants.INTENT_FILTER.ACTION_GET_POSTS).size() == 0)
                    {
                        mPendingTasks.put(Constants.INTENT_FILTER.ACTION_GET_POSTS, Constants.INTENT_FILTER.ACTION_GET_POSTS);
                        
                        mMultiThreadExecutor.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPendingTasks.removeAll(Constants.INTENT_FILTER.ACTION_GET_POSTS);
                                mRunningTasks.put(Constants.INTENT_FILTER.ACTION_GET_POSTS, Constants.INTENT_FILTER.ACTION_GET_POSTS);
                                
                                Intent result = null;
                                
                                try
                                {
                                    ArrayList<LepraPost> posts = Lepra.getInstance().loadPosts(null, "last_activity");
                                    
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_SUCCESS);
                                    result.putParcelableArrayListExtra(Constants.BUNDLE.KEY_POSTS, posts);
                                }
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_POSTS_RESULT_FAIL);
                                }
                                finally
                                {
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
                                    mRunningTasks.removeAll(Constants.INTENT_FILTER.ACTION_GET_POSTS);
                                }
                            }
                        });
                    }
                    
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS:
                {
                    final LepraPost post = intent.getParcelableExtra(Constants.BUNDLE.KEY_POST);
                    
                    // Check if task is already queued or running
                    if(!mPendingTasks.containsEntry(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS, Integer.toString(post.id)) && !mRunningTasks.containsEntry(Constants.INTENT_FILTER.ACTION_GET_PROFILE, Integer.toString(post.id)))
                    {
                        mPendingTasks.put(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS, Integer.toString(post.id));
                        
                        mMultiThreadExecutor.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPendingTasks.remove(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS, Integer.toString(post.id));
                                mRunningTasks.put(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS, Integer.toString(post.id));
                                
                                Intent result = null;
                                
                                try
                                {
                                    ArrayList<LepraComment> comments = Lepra.getInstance().loadPostComments(post.id);
                                    
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS_RESULT_SUCCESS);
                                    result.putParcelableArrayListExtra(Constants.BUNDLE.KEY_COMMENTS, comments);
                                }
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS_RESULT_FAIL);
                                }
                                finally
                                {
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
                                    mRunningTasks.remove(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS, Integer.toString(post.id));
                                }
                            }
                        });
                    }
                    
                    break;
                }
                
                case Constants.INTENT_FILTER.ACTION_GET_PROFILE:
                {
                    final LepraUser user = intent.getParcelableExtra(Constants.BUNDLE.KEY_USER);
                    
                    // Check if task is already queued or running
                    if(!mPendingTasks.containsEntry(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login) && !mRunningTasks.containsEntry(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login))
                    {
                        mPendingTasks.put(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login);
                        
                        mMultiThreadExecutor.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPendingTasks.remove(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login);
                                mRunningTasks.put(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login);
                                
                                Intent result = null;
                                
                                try
                                {
                                    LepraProfile profile = Lepra.getInstance().loadProfile(user.login);
                                    
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_SUCCESS);
                                    result.putExtra(Constants.BUNDLE.KEY_USER_PROFILE, profile);
                                }
                                catch (Exception e) 
                                {
                                    e.printStackTrace();
                                    result = new Intent(Constants.INTENT_FILTER.ACTION_GET_PROFILE_RESULT_FAIL);
                                }
                                finally
                                {
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(result);
                                    mRunningTasks.remove(Constants.INTENT_FILTER.ACTION_GET_PROFILE, user.login);
                                }
                            }
                        });
                    }
                    
                    break;
                }
            }
        }
    };
    
    public void onCreate()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER.ACTION_AUTH);
        filter.addAction(Constants.INTENT_FILTER.ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POSTS);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_PROFILE);
        filter.addAction(Constants.INTENT_FILTER.ACTION_GET_POST_COMMENTS);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver, filter);
    };
    
    @Override
    public IBinder onBind(Intent arg0)
    {
        return new NwBinder();
    }
    
    private class NwBinder extends Binder implements NetworkInterface
    {
        public boolean isPending(String action, String id)
        {
            return mPendingTasks.containsEntry(action, id);
        }
        
        public boolean isRunning(String action, String id)
        {
            return mRunningTasks.containsEntry(action, id);
        }
    }
}
