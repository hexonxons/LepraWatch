package org.koroed.lepra.content.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.koroed.lepra.content.LepraProfile;
import org.koroed.lepra.content.LepraProfileContact;
import org.koroed.lepra.content.LepraUser;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 13:02
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class LepraProfileParser extends LepraContentParser<LepraProfile>
{
    private static LepraProfileParser sInstance = null;
    private static Object sLock                 = new Object();
    
    public static LepraProfileParser getInstance()
    {
        synchronized(sLock)
        {
            if (sInstance == null)
            {
                sInstance = new LepraProfileParser();
            }
            
            return sInstance;
        }
    }
    
    private LepraProfileParser(){}
    
    @Override
    protected synchronized LepraProfile parseContent(String content)
    {
        Document doc = Jsoup.parse(content);
        
        String userText = getFirstElementHtml(doc, ".b-user_text");
        
        LepraProfile profile = new LepraProfile();
        
        profile.lepraUser = new LepraUser();
        profile.lepraUser.login = doc.select(".b-table-cell .b-user_name-link").text();
        profile.lepraUser.gender = doc.select(".b-user_parent").html().indexOf("Зарегистрирована") == -1 ? "male" : "female";
        profile.lepraUser.karma = Integer.parseInt(doc.select(".b-karma_value_inner").text());
        profile.lepraUser.id = Integer.MIN_VALUE;
        
        profile.userRegistrationDate = Long.parseLong(doc.select(".b-user_name-table span").attr("data-epoch_date")) * 1000;
        profile.userParent = doc.select(".b-user_friends .b-user_parent a").text();
        profile.userFullName = doc.select(".b-user_full_name").text();
        profile.userResidence = doc.select(".b-user_residence").text();
        profile.userPic = doc.select(".b-userpic img").first().absUrl("src");
        profile.userTotalWritten = doc.select(".b-user_stat").get(0).text().split("\\.")[0];
        profile.userTotalRating = doc.select(".b-user_stat").get(0).text().split("\\.")[1];
        profile.userTotalVotes = doc.select(".b-user_stat").get(1).text();
        profile.userText = userText;
        
        String[] userContacts = doc.select(".b-user_contacts").html().split("<br />");
        
        profile.userContacts = new LepraProfileContact[userContacts.length];
        
        for(int i = 0; i < userContacts.length; ++i)
        {
            String site = userContacts[i];
            
            LepraProfileContact lepraSite = new LepraProfileContact();
            
            if(site.contains("сайт"))
            {
                lepraSite.siteName = "Сайт";
                lepraSite.siteUrl = site.substring(site.indexOf(">") + 1, site.indexOf("</a"));
            }
            
            if(site.contains("Facebook"))
            {
                lepraSite.siteName = "Facebook";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Одноклассники"))
            {
                lepraSite.siteName = "Одноклассники";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Вконтакте"))
            {
                lepraSite.siteName = "Вконтакте";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Google Plus"))
            {
                lepraSite.siteName = "Google Plus";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Twitter"))
            {
                lepraSite.siteName = "Twitter";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("icq"))
            {
                lepraSite.siteName = "ICQ";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Skype"))
            {
                lepraSite.siteName = "Skype";
                lepraSite.siteUrl = "skype:" + site.split(":")[1].trim();
            }
            
            if(site.contains("AIM"))
            {
                lepraSite.siteName = "AIM";
                lepraSite.siteUrl = "aim://" + site.split(":")[1].trim();
            }
            
            if(site.contains("LiveJournal"))
            {
                lepraSite.siteName = "LiveJournal";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("last.fm"))
            {
                lepraSite.siteName = "Last.fm";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("flickr"))
            {
                lepraSite.siteName = "Flickr";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("del.icio.us"))
            {
                lepraSite.siteName = "del.icio.us";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("Blogger"))
            {
                lepraSite.siteName = "Blogger";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            if(site.contains("LinkedIn"))
            {
                lepraSite.siteName = "LinkedIn";
                lepraSite.siteUrl = site.substring(site.indexOf("=\"") + 2, site.indexOf("\">"));
            }
            
            profile.userContacts[i] = lepraSite;
        }
        
        return profile;
    }
    
    private String getFirstElementHtml(Document doc, String cssQuery)
    {
        Element e = doc.select(cssQuery).first();
        if(e != null)
        {
            return e.html();
        }
        return null;
    }
    
    private String getFirstElementText(Document doc, String cssQuery)
    {
        Element e = doc.select(cssQuery).first();
        if(e != null)
        {
            return e.text();
        }
        return null;
    }
}
