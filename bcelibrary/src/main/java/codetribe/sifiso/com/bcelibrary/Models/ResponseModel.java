package codetribe.sifiso.com.bcelibrary.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sifiso on 2015-10-17.
 */
public class ResponseModel implements Parcelable {
    public List<CaptionModel> captionModels;
    private long lastCacheDate;


    public ResponseModel() {
    }

    protected ResponseModel(Parcel in) {
        captionModels = in.createTypedArrayList(CaptionModel.CREATOR);
        lastCacheDate = in.readLong();
    }

    public static final Creator<ResponseModel> CREATOR = new Creator<ResponseModel>() {
        @Override
        public ResponseModel createFromParcel(Parcel in) {
            return new ResponseModel(in);
        }

        @Override
        public ResponseModel[] newArray(int size) {
            return new ResponseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(captionModels);
        dest.writeLong(lastCacheDate);
    }

    public List<CaptionModel> getCaptionModels() {
        return captionModels;
    }

    public void setCaptionModels(List<CaptionModel> captionModels) {
        this.captionModels = captionModels;
    }

    public long getLastCacheDate() {
        return lastCacheDate;
    }

    public void setLastCacheDate(long lastCacheDate) {
        this.lastCacheDate = lastCacheDate;
    }
}
