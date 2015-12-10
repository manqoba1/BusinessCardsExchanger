package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sifiso on 2015-10-28.
 */
public class SpecialItemModel implements Parcelable {

    public int itemID;
    public String type, message, description, icon, finePrint, title, state, entryUrl;
    public String width,height;
    public VenueModel venueModel;


    public SpecialItemModel() {
    }

    protected SpecialItemModel(Parcel in) {
        width = in.readString();
        height=in.readString();
        finePrint = in.readString();
        itemID = in.readInt();
        type = in.readString();
        message = in.readString();
        description = in.readString();
        icon = in.readString();
        title = in.readString();
        state = in.readString();
        entryUrl = in.readString();
        venueModel = in.readParcelable(VenueModel.class.getClassLoader());

    }

    public static final Creator<SpecialItemModel> CREATOR = new Creator<SpecialItemModel>() {
        @Override
        public SpecialItemModel createFromParcel(Parcel in) {
            return new SpecialItemModel(in);
        }

        @Override
        public SpecialItemModel[] newArray(int size) {
            return new SpecialItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return CREATOR.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeString(type);
        dest.writeString(message);
        dest.writeString(description);
        dest.writeString(icon);
        dest.writeString(title);
        dest.writeString(state);
        dest.writeString(entryUrl);
        dest.writeParcelable(venueModel, flags);
        dest.writeString(finePrint);
        dest.writeString(height);
        dest.writeString(width);
    }
}
