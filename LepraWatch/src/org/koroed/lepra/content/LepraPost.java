package org.koroed.lepra.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 14.05.14
 * Time: 14:24
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class LepraPost implements Parcelable
{
    public int id                   = Integer.MIN_VALUE;
    public String link              = null;
    public boolean isGold           = false;
    
    public String userLogin         = null;
    public String userTitle         = null;
    public String userGender        = null;
    
    public long date                = Long.MIN_VALUE;
    public String totalCommentCnt   = null;
    public String newCommentCnt     = null;
    public int rating               = Integer.MIN_VALUE;
    
    public String content           = null;
    
    public static final Parcelable.Creator<LepraPost> CREATOR = new Parcelable.Creator<LepraPost>()
    {
        public LepraPost[] newArray(int size)
        {
            return new LepraPost[size];
        }
        
        @Override
        public LepraPost createFromParcel(Parcel source)
        {
            return new LepraPost(source);
        }
    };
    
    public LepraPost(){}
    
    public LepraPost(Parcel parcel)
    {
        id = parcel.readInt();
        link = parcel.readString();
        isGold = parcel.readInt() == 1;
        
        userLogin = parcel.readString();
        userTitle = parcel.readString();
        userGender = parcel.readString();
        
        date = parcel.readLong();
        totalCommentCnt = parcel.readString();
        newCommentCnt = parcel.readString();
        rating = parcel.readInt();
        
        content = parcel.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(link);
        dest.writeInt(isGold ? 1 : 0);
        
        dest.writeString(userLogin);
        dest.writeString(userTitle);
        dest.writeString(userGender);
        
        dest.writeLong(date);
        dest.writeString(totalCommentCnt);
        dest.writeString(newCommentCnt);
        dest.writeInt(rating);
        
        dest.writeString(content);
    }
}
