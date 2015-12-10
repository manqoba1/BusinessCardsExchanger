package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sifiso on 2015-10-12.
 */
public class LocationModel implements Parcelable {
    public int id;
    public double latitude, longitude, distance;
    public String name, address, crossStreet,postalCode,  cc, city;

    public LocationModel() {
    }

    protected LocationModel(Parcel in) {
        id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
        postalCode = in.readString();
        cc = in.readString();
        city = in.readString();
        name = in.readString();
        address = in.readString();
        crossStreet = in.readString();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distance);
        dest.writeString(postalCode);
        dest.writeString(cc);
        dest.writeString(city);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(crossStreet);
    }
}
