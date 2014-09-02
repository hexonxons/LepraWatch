package org.koroed.lepra.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Nikita Koroed
 * E-mail: nikita@koroed.org
 * Date: 26.05.2014
 * Time: 11:22
 */
/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class LepraUser implements Parcelable
{
    public int id           = Integer.MIN_VALUE;
    public String login     = null;
    public String gender    = null;
    public int karma        = Integer.MIN_VALUE;
    
    public static final Parcelable.Creator<LepraUser> CREATOR = new Parcelable.Creator<LepraUser>()
    {
        public LepraUser[] newArray(int size)
        {
            return new LepraUser[size];
        }
        
        @Override
        public LepraUser createFromParcel(Parcel source)
        {
            return new LepraUser(source);
        }
    };
    
    public LepraUser(){}
    
    public LepraUser(Parcel parcel)
    {
        id = parcel.readInt();
        login = parcel.readString();
        gender = parcel.readString();
        karma = parcel.readInt();
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
        dest.writeString(login);
        dest.writeString(gender);
        dest.writeInt(karma);
    }
}
