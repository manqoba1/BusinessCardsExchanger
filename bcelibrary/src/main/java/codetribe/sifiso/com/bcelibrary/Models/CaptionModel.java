package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sifiso on 2015-10-14.
 */
public class CaptionModel implements Parcelable{
    public long createdTime;
    public String textMessage;
    public String imageUrlThumb;
    public String imageUrlStnd;
    public String imageUrlLow;
    public int width,height;
    public int id;
    public int locationID;

    public String fullName;
    public double latitude;
    public double longitude;

    public CaptionModel() {
    }


    protected CaptionModel(Parcel in) {
        createdTime = in.readLong();
        textMessage = in.readString();
        imageUrlThumb = in.readString();
        imageUrlStnd = in.readString();
        imageUrlLow = in.readString();
        width = in.readInt();
        height = in.readInt();
        id = in.readInt();
        locationID = in.readInt();
        fullName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<CaptionModel> CREATOR = new Creator<CaptionModel>() {
        @Override
        public CaptionModel createFromParcel(Parcel in) {
            return new CaptionModel(in);
        }

        @Override
        public CaptionModel[] newArray(int size) {
            return new CaptionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createdTime);
        dest.writeString(textMessage);
        dest.writeString(imageUrlThumb);
        dest.writeString(imageUrlStnd);
        dest.writeString(imageUrlLow);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(id);
        dest.writeInt(locationID);
        dest.writeString(fullName);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
