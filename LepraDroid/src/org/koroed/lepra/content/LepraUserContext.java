package org.koroed.lepra.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: hexonxons.
 * E-mail: killgamesh666@gmail.com
 * Data: 27.08.2014
 */
public class LepraUserContext implements Parcelable
{
    public long created     = Long.MIN_VALUE;
    public int invitedById  = Integer.MIN_VALUE;
    public String csrfToken = null;
    
    public static final Parcelable.Creator<LepraUserContext> CREATOR = new Parcelable.Creator<LepraUserContext>()
    {
        public LepraUserContext[] newArray(int size)
        {
            return new LepraUserContext[size];
        }
        
        @Override
        public LepraUserContext createFromParcel(Parcel source)
        {
            return new LepraUserContext(source);
        }
    };
    
    public LepraUserContext(){}
    
    public LepraUserContext(Parcel parcel)
    {
        created = parcel.readLong();
        invitedById = parcel.readInt();
        csrfToken = parcel.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(created);
        dest.writeInt(invitedById);
        dest.writeString(csrfToken);
    }
}
