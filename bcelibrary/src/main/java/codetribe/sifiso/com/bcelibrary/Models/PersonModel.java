package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sifiso on 2015-11-24.
 */
public class PersonModel implements Parcelable {
    public int id;
    public String name, surname;
    public CompanyModel companyModel;
    public String email;
    public String cell;
    public String tell;
    public List<String> titlesModel;

    public PersonModel() {
    }

    public PersonModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        companyModel = in.readParcelable(CompanyModel.class.getClassLoader());
        email = in.readString();
        cell = in.readString();
        tell = in.readString();
        titlesModel = in.createStringArrayList();
    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel in) {
            return new PersonModel(in);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
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
        dest.writeString(surname);
        dest.writeParcelable(companyModel, flags);
        dest.writeString(email);
        dest.writeString(cell);
        dest.writeString(tell);
        dest.writeStringList(titlesModel);
    }
}
