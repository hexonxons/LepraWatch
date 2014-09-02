package org.koroed.lepra.content;

import android.os.Parcel;
import android.os.Parcelable;

public class LepraProfileContact implements Parcelable
{
    public String siteName  = null;
    public String siteUrl   = null;
    
    public static final Parcelable.Creator<LepraProfileContact> CREATOR = new Parcelable.Creator<LepraProfileContact>()
    {
        public LepraProfileContact[] newArray(int size)
        {
            return new LepraProfileContact[size];
        }
        
        @Override
        public LepraProfileContact createFromParcel(Parcel source)
        {
            return new LepraProfileContact(source);
        }
    };
    
    public LepraProfileContact(){}
    
    public LepraProfileContact(Parcel parcel)
    {
        siteName = parcel.readString();
        siteUrl = parcel.readString();
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(siteName);
        dest.writeString(siteUrl);
    }

}
