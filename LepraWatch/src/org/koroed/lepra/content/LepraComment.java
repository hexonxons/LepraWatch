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
 * Data: 08.09.2014
 */
public class LepraComment implements Parcelable
{
    public int id               = Integer.MIN_VALUE;
    public int parentId         = Integer.MIN_VALUE;
    
    public String content       = null;
    
    public String userLogin     = null;
    public String userTitle     = null;
    public String userGender    = null;
    
    public long date            = Long.MIN_VALUE;
    public int rating           = Integer.MIN_VALUE;
    
    public static final Parcelable.Creator<LepraComment> CREATOR = new Parcelable.Creator<LepraComment>()
    {
        public LepraComment[] newArray(int size)
        {
            return new LepraComment[size];
        }
        
        @Override
        public LepraComment createFromParcel(Parcel source)
        {
            return new LepraComment(source);
        }
    };
    
    public LepraComment(){}
    
    public LepraComment(Parcel parcel)
    {
        id = parcel.readInt();
        parentId = parcel.readInt();
        content = parcel.readString();
        
        userLogin = parcel.readString();
        userTitle = parcel.readString();
        userGender = parcel.readString();
        
        date = parcel.readLong();
        rating = parcel.readInt();
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
        dest.writeInt(parentId);
        dest.writeString(content);
        
        dest.writeString(userLogin);
        dest.writeString(userTitle);
        dest.writeString(userGender);
        
        dest.writeLong(date);
        dest.writeInt(rating);
    }
}
