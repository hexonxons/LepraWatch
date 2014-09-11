package org.koroed.lepra;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.koroed.lepra.content.LepraComment;
import org.koroed.lepra.content.LepraContext;
import org.koroed.lepra.content.LepraPost;
import org.koroed.lepra.content.LepraProfile;
import org.koroed.lepra.content.LepraUser;
import org.koroed.lepra.content.LepraUserContext;
import org.koroed.lepra.content.parser.CurrentUserInfoParser;
import org.koroed.lepra.content.parser.LepraEmptyContentParser;
import org.koroed.lepra.content.parser.LepraLoginResponseParser;
import org.koroed.lepra.content.parser.LepraCommentsListParser;
import org.koroed.lepra.content.parser.LepraPostListParser;
import org.koroed.lepra.content.parser.LepraProfileParser;
import org.koroed.lepra.content.parser.LepraStatusParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hexonxons.leprawatch.system.Constants;


/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 14.05.14
 * Time: 11:28
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class Lepra
{
    // Lepra instance.
    private static Lepra sInstance      = null;
    // Mutex.
    private static Object sLock         = new Object();
    // Lepra context.
    private LepraContext mLepraContext  = null;
    // App context.
    private Context mAppContext         = null;
    
    
    private int mDefOffset              = 0;
    
    private LepraHttpClient mHttpClient = new LepraHttpClient();
    
    public static Lepra getInstance()
    {
        synchronized (sLock)
        {
            if (sInstance == null)
            {
                sInstance = new Lepra();
            }
            
            return sInstance;
        }
    }
    
    // Hide default cont.
    private Lepra(){}
    
    /**
     * Init lepra class. Make sure init was called before any usage of this class.
     * 
     * @param context Application context.
     */
    public void init(Context context)
    {
        // Save application context.
        mAppContext = context.getApplicationContext();
        
        // Init context.
        mLepraContext = new LepraContext();
        mLepraContext.user = new LepraUser();
        mLepraContext.userContext = new LepraUserContext();
        
        SharedPreferences preferences = mAppContext.getSharedPreferences(Constants.SHARED_PREFERENCES.NAME, Context.MODE_PRIVATE);
        
        // Don't load data if not authorized.
        if(!preferences.getBoolean(Constants.SHARED_PREFERENCES.USER_AUTHORIZED, false))
            return;
        
        // Load lepra user.
        mLepraContext.user.id = preferences.getInt(Constants.SHARED_PREFERENCES.USER_ID, Integer.MIN_VALUE);
        mLepraContext.user.login = preferences.getString(Constants.SHARED_PREFERENCES.USER_LOGIN, null);
        mLepraContext.user.gender = preferences.getString(Constants.SHARED_PREFERENCES.USER_GENDER, null);
        mLepraContext.user.karma = preferences.getInt(Constants.SHARED_PREFERENCES.USER_KARMA, Integer.MIN_VALUE);
        // Load lepra user context.
        mLepraContext.userContext.created = preferences.getLong(Constants.SHARED_PREFERENCES.USER_CONTEXT_CREATED, Long.MIN_VALUE);
        mLepraContext.userContext.invitedById = preferences.getInt(Constants.SHARED_PREFERENCES.USER_CONTEXT_INVITED_BY, Integer.MIN_VALUE);
        mLepraContext.userContext.csrfToken = preferences.getString(Constants.SHARED_PREFERENCES.USER_CONTEXT_CSRF_TOKEN, null);
        
        // We need 2 cookies. Load it from shared prefs - not rly nice idea, but fast and not clean. Just like it.
        List<Cookie> cookies = new ArrayList<>();
        
        BasicClientCookie cookie1 = new BasicClientCookie(
                preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_1_NAME, null),
                preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_1_VALUE, null));
        cookie1.setVersion(preferences.getInt(Constants.SHARED_PREFERENCES.COOKIE_1_VERSION, Integer.MIN_VALUE));
        cookie1.setDomain(preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_1_DOMAIN, null));
        cookie1.setPath(preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_1_PATH, null));
        cookie1.setExpiryDate(new Date(preferences.getLong(Constants.SHARED_PREFERENCES.COOKIE_1_EXPIRY, Long.MIN_VALUE)));
        
        BasicClientCookie cookie2 = new BasicClientCookie(
                preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_2_NAME, null),
                preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_2_VALUE, null));
        cookie2.setVersion(preferences.getInt(Constants.SHARED_PREFERENCES.COOKIE_2_VERSION, Integer.MIN_VALUE));
        cookie2.setDomain(preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_2_DOMAIN, null));
        cookie2.setPath(preferences.getString(Constants.SHARED_PREFERENCES.COOKIE_2_PATH, null));
        cookie2.setExpiryDate(new Date(preferences.getLong(Constants.SHARED_PREFERENCES.COOKIE_2_EXPIRY, Long.MIN_VALUE)));
        
        cookies.add(cookie1);
        cookies.add(cookie2);
        
        mHttpClient.setCookies(cookies);
    }
    
    public LepraContext getContext()
    {
        return mLepraContext;
    }
    
    public void login(String username, String password, boolean forever)
    {
        login(username, password, forever, null, null);
    }
    
    public void login(String username, String password, boolean forever, String recaptchaChallengeField, String recaptchaResponseField)
    {
        if(isAuthorized())
            throw new RuntimeException("Already authorized.");
        
        // Set params.
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("forever", forever ? "1" : "0");
        
        if(!StringUtils.isBlank(recaptchaChallengeField))
        {
            parameters.put("recaptcha_challenge_field", recaptchaChallengeField);
        }
        
        if(!StringUtils.isBlank(recaptchaResponseField))
        {
            parameters.put("recaptcha_response_field", recaptchaResponseField);
        }
        
        // Create context.
        mLepraContext = new LepraContext();
        mLepraContext.user = mHttpClient.loadContent(LepraURI.LOGIN, parameters, LepraLoginResponseParser.getInstance());
        mLepraContext.userContext = mHttpClient.loadContent(LepraURI.getProfileURI(mLepraContext.user.login), CurrentUserInfoParser.getInstance());
        
        Editor editor = mAppContext.getSharedPreferences(Constants.SHARED_PREFERENCES.NAME, Context.MODE_PRIVATE).edit();
        // Save user.
        editor.putInt(Constants.SHARED_PREFERENCES.USER_ID, mLepraContext.user.id);
        editor.putString(Constants.SHARED_PREFERENCES.USER_LOGIN, mLepraContext.user.login);
        editor.putString(Constants.SHARED_PREFERENCES.USER_GENDER, mLepraContext.user.gender);
        editor.putInt(Constants.SHARED_PREFERENCES.USER_KARMA, mLepraContext.user.karma);
        // Save user context.
        editor.putLong(Constants.SHARED_PREFERENCES.USER_CONTEXT_CREATED, mLepraContext.userContext.created);
        editor.putInt(Constants.SHARED_PREFERENCES.USER_CONTEXT_INVITED_BY, mLepraContext.userContext.invitedById);
        editor.putString(Constants.SHARED_PREFERENCES.USER_CONTEXT_CSRF_TOKEN, mLepraContext.userContext.csrfToken);
        
        // We need 2 cookies. Save it in shared prefs - not rly nice idea, but fast and not clean. Just like it.
        List<Cookie> cookies = mHttpClient.getCookies();
        
        Cookie cookie1 = cookies.get(0);
        editor.putInt(Constants.SHARED_PREFERENCES.COOKIE_1_VERSION, cookie1.getVersion());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_1_NAME, cookie1.getName());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_1_VALUE, cookie1.getValue());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_1_DOMAIN, cookie1.getDomain());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_1_PATH, cookie1.getPath());
        editor.putLong(Constants.SHARED_PREFERENCES.COOKIE_1_EXPIRY, cookie1.getExpiryDate().getTime());
        
        Cookie cookie2 = cookies.get(1);
        editor.putInt(Constants.SHARED_PREFERENCES.COOKIE_2_VERSION, cookie2.getVersion());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_2_NAME, cookie2.getName());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_2_VALUE, cookie2.getValue());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_2_DOMAIN, cookie2.getDomain());
        editor.putString(Constants.SHARED_PREFERENCES.COOKIE_2_PATH, cookie2.getPath());
        editor.putLong(Constants.SHARED_PREFERENCES.COOKIE_2_EXPIRY, cookie2.getExpiryDate().getTime());
        
        // Set authorized
        editor.putBoolean(Constants.SHARED_PREFERENCES.USER_AUTHORIZED, true);
        
        // Commit changes.
        editor.commit();
    }
    
    public boolean isAuthorized()
    {
        return mLepraContext.user.id != Integer.MIN_VALUE;
    }
    
    public LepraStatus getLepraStatus()
    {
        return mHttpClient.loadContent(LepraURI.LEPROPANEL, LepraStatusParser.getInstance());
    }
    
    public void logout(Context context)
    {
        // Set params.
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("csrf_token", mLepraContext.userContext.csrfToken);
        
        // Call site logout.
        mHttpClient.loadContent(LepraURI.LOGOUT, parameters, LepraEmptyContentParser.getInstance());
        
        // Remove saved data from shared preferences
        Editor editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES.NAME, Context.MODE_PRIVATE).edit();
        editor.remove(Constants.SHARED_PREFERENCES.USER_ID);
        editor.remove(Constants.SHARED_PREFERENCES.USER_LOGIN);
        editor.remove(Constants.SHARED_PREFERENCES.USER_GENDER);
        editor.remove(Constants.SHARED_PREFERENCES.USER_KARMA);
        
        editor.remove(Constants.SHARED_PREFERENCES.USER_CONTEXT_CREATED);
        editor.remove(Constants.SHARED_PREFERENCES.USER_CONTEXT_INVITED_BY);
        editor.remove(Constants.SHARED_PREFERENCES.USER_CONTEXT_CSRF_TOKEN);
        
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_VERSION);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_NAME);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_VALUE);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_DOMAIN);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_PATH);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_1_EXPIRY);
        
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_VERSION);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_NAME);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_VALUE);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_DOMAIN);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_PATH);
        editor.remove(Constants.SHARED_PREFERENCES.COOKIE_2_EXPIRY);
        
        editor.remove(Constants.SHARED_PREFERENCES.USER_AUTHORIZED);
        
        editor.commit();
    }
    
    public LepraProfile loadProfile(String login)
    {
        return mHttpClient.loadContent(LepraURI.getProfileURI(login), LepraProfileParser.getInstance());
    }
    
    public ArrayList<LepraPost> loadPosts(String leprosorium, String sorting)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("csrf_token", mLepraContext.userContext.csrfToken);
        parameters.put("offset", Integer.toString(mDefOffset));
        parameters.put("sorting", sorting);
        
        return mHttpClient.loadContent(LepraURI.getPostListURI(leprosorium), parameters, LepraPostListParser.getInstance());
    }
    
    public ArrayList<LepraComment> loadPostComments(int postId)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("csrf_token", mLepraContext.userContext.csrfToken);
        
        return mHttpClient.loadContent(LepraURI.getPostCommentsURI(postId), parameters, LepraCommentsListParser.getInstance());
    }
}
