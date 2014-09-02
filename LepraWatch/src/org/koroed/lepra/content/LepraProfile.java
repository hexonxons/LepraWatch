package org.koroed.lepra.content;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 14.05.14
 * Time: 14:25
 */
/**
 * Author: Hexonxons
 * E-mail: killgamesh666@gmail.com
 * Date: 01.09.14
 */
public class LepraProfile implements Parcelable
{
    public LepraUser lepraUser                  = null;
    public String userFullName                  = null;
    public String userResidence                 = null;
    public String userText                      = null;
    public String userPic                       = null;
    
    public long userRegistrationDate            = Long.MIN_VALUE;
    public String userParent                    = null;
    public String userTotalWritten              = null;
    public String userTotalRating               = null;
    public String userTotalVotes                = null;
    public LepraProfileContact[] userContacts   = null;
    
    public static final Parcelable.Creator<LepraProfile> CREATOR = new Parcelable.Creator<LepraProfile>()
    {
        public LepraProfile[] newArray(int size)
        {
            return new LepraProfile[size];
        }
        
        @Override
        public LepraProfile createFromParcel(Parcel source)
        {
            return new LepraProfile(source);
        }
    };
    
    public LepraProfile(){}
    
    public LepraProfile(Parcel parcel)
    {
        lepraUser = parcel.readParcelable(LepraUser.class.getClassLoader());
        userFullName = parcel.readString();
        userResidence = parcel.readString();
        userText = parcel.readString();
        userPic = parcel.readString();
        
        userRegistrationDate = parcel.readLong();
        userParent = parcel.readString();
        userTotalWritten = parcel.readString();
        userTotalRating = parcel.readString();
        userTotalVotes = parcel.readString();
        
        Parcelable[] array = parcel.readParcelableArray(LepraProfileContact.class.getClassLoader());
        userContacts = Arrays.copyOf(array, array.length, LepraProfileContact[].class);
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(lepraUser, 0);
        dest.writeString(userFullName);
        dest.writeString(userResidence);
        dest.writeString(userText);
        dest.writeString(userPic);
        
        dest.writeLong(userRegistrationDate);
        dest.writeString(userParent);
        dest.writeString(userTotalWritten);
        dest.writeString(userTotalRating);
        dest.writeString(userTotalVotes);
        dest.writeParcelableArray(userContacts, 0);
    }
}
