package com.hexonxons.leprawatch.system;

public class Constants
{
    public static class INTENT_FILTER
    {
        public static final String ACTION_AUTH          = "ACTION_AUTH";
        public static final String ACTION_AUTH_FAIL     = "ACTION_AUTH_FAIL";
        public static final String ACTION_AUTH_SUCCESS  = "ACTION_AUTH_SUCCESS";
        
        public static final String ACTION_LOGOUT            = "ACTION_LOGOUT";
        public static final String ACTION_LOGOUT_FAIL       = "ACTION_LOGOUT_FAIL";
        public static final String ACTION_LOGOUT_SUCCESS    = "ACTION_LOGOUT_SUCCESS";
        
        public static final String ACTION_GET_POSTS                 = "ACTION_GET_POSTS";
        public static final String ACTION_GET_POSTS_RESULT_FAIL     = "ACTION_GET_POSTS_RESULT_FAIL";
        public static final String ACTION_GET_POSTS_RESULT_SUCCESS  = "ACTION_GET_POSTS_RESULT_SUCCESS";
        
        public static final String ACTION_GET_POST_COMMENTS                 = "ACTION_GET_POST_COMMENTS";
        public static final String ACTION_GET_POST_COMMENTS_RESULT_FAIL     = "ACTION_GET_POST_COMMENTS_RESULT_FAIL";
        public static final String ACTION_GET_POST_COMMENTS_RESULT_SUCCESS  = "ACTION_GET_POST_COMMENTS_RESULT_SUCCESS";
        
        public static final String ACTION_GET_PROFILE                   = "ACTION_GET_PROFILE";
        public static final String ACTION_GET_PROFILE_RESULT_FAIL       = "ACTION_GET_PROFILE_RESULT_FAIL";
        public static final String ACTION_GET_PROFILE_RESULT_SUCCESS    = "ACTION_GET_PROFILE_RESULT_SUCCESS";
    }
    
    public static class SHARED_PREFERENCES
    {
        // Shared preferences name.
        public static final String NAME             = "SHARED_PREFERENCES";
        
        // Is user authorized.
        public static final String USER_AUTHORIZED  = "USER_AUTHORIZED";
        
        // User data.
        public static final String USER_ID          = "USER_ID";
        public static final String USER_LOGIN       = "USER_LOGIN";
        public static final String USER_GENDER      = "USER_GENDER";
        public static final String USER_KARMA       = "USER_KARMA";
        
        // User context.
        public static final String USER_CONTEXT_CREATED     = "USER_CONTEXT_CREATED";
        public static final String USER_CONTEXT_INVITED_BY  = "USER_CONTEXT_INVITED_BY";
        public static final String USER_CONTEXT_CSRF_TOKEN  = "USER_CONTEXT_CSRF_TOKEN";
        
        // 2 cookies keys
        public static final String COOKIE_1_VERSION = "COOKIE_1_VERSION";
        public static final String COOKIE_1_NAME    = "COOKIE_1_NAME";
        public static final String COOKIE_1_VALUE   = "COOKIE_1_VALUE";
        public static final String COOKIE_1_DOMAIN  = "COOKIE_1_DOMAIN";
        public static final String COOKIE_1_PATH    = "COOKIE_1_PATH";
        public static final String COOKIE_1_EXPIRY  = "COOKIE_1_EXPIRY";
        
        public static final String COOKIE_2_VERSION = "COOKIE_2_VERSION";
        public static final String COOKIE_2_NAME    = "COOKIE_2_NAME";
        public static final String COOKIE_2_VALUE   = "COOKIE_2_VALUE";
        public static final String COOKIE_2_DOMAIN  = "COOKIE_2_DOMAIN";
        public static final String COOKIE_2_PATH    = "COOKIE_2_PATH";
        public static final String COOKIE_2_EXPIRY  = "COOKIE_2_EXPIRY";
        
    }
    
    public static class BUNDLE
    {
        public static final String KEY_AUTH         = "KEY_AUTH";
        public static final String KEY_USERNAME     = "KEY_USERNAME";
        public static final String KEY_PASSWORD     = "KEY_PASSWORD";
        public static final String KEY_USER         = "KEY_USER";
        public static final String KEY_POSTS        = "KEY_POSTS";
        public static final String KEY_POST         = "KEY_POST";
        public static final String KEY_COMMENTS     = "KEY_COMMENTS";
        public static final String KEY_USER_PROFILE = "KEY_USER_PROFILE";
    }
}
