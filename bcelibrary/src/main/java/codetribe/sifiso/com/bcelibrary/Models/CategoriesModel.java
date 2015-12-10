package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sifiso on 2015-10-30.
 */
public class CategoriesModel implements Parcelable {
    public int categoriesID;
    public String name, pluralName, shortName,icon;

    public CategoriesModel() {
    }

    protected CategoriesModel(Parcel in) {
        categoriesID = in.readInt();
        name = in.readString();
        pluralName = in.readString();
        shortName = in.readString();
        icon = in.readString();
    }

    public static final Creator<CategoriesModel> CREATOR = new Creator<CategoriesModel>() {
        @Override
        public CategoriesModel createFromParcel(Parcel in) {
            return new CategoriesModel(in);
        }

        @Override
        public CategoriesModel[] newArray(int size) {
            return new CategoriesModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoriesID);
        dest.writeString(name);
        dest.writeString(pluralName);
        dest.writeString(shortName);
        dest.writeString(icon);
    }
}
