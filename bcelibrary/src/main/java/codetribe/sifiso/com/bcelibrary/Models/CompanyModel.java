package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sifiso on 2015-11-24.
 */
public class CompanyModel implements Parcelable {

    public int id;
    public String name;
    public String website;
    public String fax;
    public String cardURI;

    protected CompanyModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        website = in.readString();
        fax = in.readString();
        cardURI = in.readString();
    }

    public static final Creator<CompanyModel> CREATOR = new Creator<CompanyModel>() {
        @Override
        public CompanyModel createFromParcel(Parcel in) {
            return new CompanyModel(in);
        }

        @Override
        public CompanyModel[] newArray(int size) {
            return new CompanyModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(website);
        dest.writeString(fax);
        dest.writeString(cardURI);
    }
}
