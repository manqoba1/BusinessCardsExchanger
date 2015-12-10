package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sifiso on 2015-10-30.
 */
public class VenueModel implements Parcelable{
    public int venueID, checkinsCount, usersCount, tipCount, venuePageID;
    public String venueName, url,formattedPhone;
    public LocationModel locationModel;
    public List<CategoriesModel> categoriesModels=new ArrayList<>();
    public VenueModel() {
    }


    protected VenueModel(Parcel in) {
        venueID = in.readInt();
        checkinsCount = in.readInt();
        usersCount = in.readInt();
        tipCount = in.readInt();
        venuePageID = in.readInt();
        venueName = in.readString();
        url = in.readString();
        formattedPhone = in.readString();
        locationModel = in.readParcelable(LocationModel.class.getClassLoader());
        categoriesModels = in.createTypedArrayList(CategoriesModel.CREATOR);
    }

    public static final Creator<VenueModel> CREATOR = new Creator<VenueModel>() {
        @Override
        public VenueModel createFromParcel(Parcel in) {
            return new VenueModel(in);
        }

        @Override
        public VenueModel[] newArray(int size) {
            return new VenueModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(venueID);
        dest.writeInt(checkinsCount);
        dest.writeInt(usersCount);
        dest.writeInt(tipCount);
        dest.writeInt(venuePageID);
        dest.writeString(venueName);
        dest.writeString(url);
        dest.writeString(formattedPhone);
        dest.writeParcelable(locationModel, flags);
        dest.writeTypedList(categoriesModels);
    }
}
