package codetribe.sifiso.com.bcelibrary.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.R;
import codetribe.sifiso.com.bcelibrary.utils.Util;

/**
 * Created by sifiso on 2015-10-12.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Holder> {

    private Context mCtx;
    private List<CaptionModel> mList;
    private final LayoutInflater inflater;
    private LocationAdapterListeners mListeners;

    public LocationAdapter(Context ctx, List<CaptionModel> mList, LocationAdapterListeners mListeners) {
        this.inflater = LayoutInflater.from(ctx);
        this.mCtx = ctx;
        this.mList = mList;
        this.mListeners = mListeners;
        Log.d("LOG Adapter", new Gson().toJson(mList));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallery_name_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final CaptionModel model = mList.get(position);
        holder.GNC_locationName.setText(model.fullName);
        holder.bsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(holder.bsCard, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListeners.onMoreImages(model);
                    }
                });
            }
        });

        DateTime date = new DateTime(model.createdTime);
        holder.GNC_date_time.setText(date.toString("HH:mm"));
        holder.GNC_message.setText(model.textMessage);
        ImageLoader.getInstance().displayImage(model.imageUrlStnd,holder.GNC_map);
}

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView GNC_locationName,GNC_date_time,GNC_message;
        private ImageView GNC_map;
        private CardView bsCard;

        public Holder(View itemView) {
            super(itemView);
            GNC_map = (ImageView) itemView.findViewById(R.id.GNC_map);
            GNC_locationName = (TextView) itemView.findViewById(R.id.GNC_locationName);
            GNC_message = (TextView) itemView.findViewById(R.id.GNC_message);
            GNC_date_time= (TextView) itemView.findViewById(R.id.GNC_date_time);
            bsCard = (CardView) itemView.findViewById(R.id.bsCard);
        }
    }

    public interface LocationAdapterListeners {
        public void onMoreImages(CaptionModel locationModel);
        public void onMapImagesView(CaptionModel locationModel);
    }
}
