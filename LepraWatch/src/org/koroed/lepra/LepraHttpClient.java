package org.koroed.lepra;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.koroed.lepra.content.parser.LepraContentParser;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 14.05.14
 * Time: 13:10
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 28.08.2014
 */
public class LepraHttpClient
{
    // Cookie store.
    private BasicCookieStore mCookieStore   = null;
    // Http client.
    private HttpClient mHttpClient          = null;
    // Http context.
    private HttpContext mContext            = null;
    
    public LepraHttpClient()
    {
        mHttpClient = new DefaultHttpClient();
        mCookieStore = new BasicCookieStore();
        
        mContext = new BasicHttpContext();
        mContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
    }
    
    public void setCookies(List<Cookie> cookies)
    {
        for(Cookie cookie : cookies)
        {
            mCookieStore.addCookie(cookie);
        }
    }
    
    public List<Cookie> getCookies()
    {
        return mCookieStore.getCookies();
    }
    
    public <T> T loadContent(URI uri, LepraContentParser<T> parser)
    {
        return loadContent(uri, null, parser);
    }
    
    public <T> T loadContent(URI uri, Map<String, String> formAttributes, LepraContentParser<T> parser)
    {
        HttpResponse response = null;
        
        try
        {
            if(formAttributes != null && formAttributes.size() > 0)
            {
                response = mHttpClient.execute(createFormRequest(uri, formAttributes), mContext);
            }
            else
            {
                HttpGet httpGet = new HttpGet(uri);
                httpGet.addHeader("Content-Type", "text/html; charset=utf-8");
                response = mHttpClient.execute(httpGet, mContext);
            }
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            //String content = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            
            return parser.parseContent(response.getEntity().getContent());
        }
        catch(IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch(LepraException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private static HttpUriRequest createFormRequest(URI uri, Map<String, String> formAttributes) throws URISyntaxException, UnsupportedEncodingException
    {
        ArrayList<NameValuePair> params = new ArrayList<>();
        
        String[] keys = new String[formAttributes.size()];
        formAttributes.keySet().toArray(keys);
        
        String[] values = new String[formAttributes.size()];
        formAttributes.values().toArray(values);
        
        for (int i = 0; i < formAttributes.size(); i++)
        {
            params.add(new BasicNameValuePair(keys[i], values[i]));
        }
        
        HttpPost request = new HttpPost(uri);
        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        
        return request;
    }
}
