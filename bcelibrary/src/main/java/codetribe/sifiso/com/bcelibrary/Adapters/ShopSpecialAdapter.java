package codetribe.sifiso.com.bcelibrary.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.LocationModel;
import codetribe.sifiso.com.bcelibrary.Models.SpecialItemModel;
import codetribe.sifiso.com.bcelibrary.R;

/**
 * Created by sifiso on 2015-11-12.
 */
public class ShopSpecialAdapter extends RecyclerView.Adapter<ShopSpecialAdapter.Holder> {

    private Context mCtx;
    private List<SpecialItemModel> mList;
    private final LayoutInflater inflater;
    private ShopSpecialAdapterListeners mListeners;

    public ShopSpecialAdapter(Context mCtx, List<SpecialItemModel> mList, ShopSpecialAdapterListeners mListeners) {
        this.inflater = LayoutInflater.from(mCtx);
        this.mCtx = mCtx;
        this.mList = mList;
        this.mListeners = mListeners;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.special_card, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder h, int position) {
        final SpecialItemModel s = mList.get(position);
        h.FSC_cat_name.setText(s.venueModel.categoriesModels.get(0).name);
        h.FSC_checkins.setText("" + s.venueModel.checkinsCount);
        h.FSC_fine_print.setText(s.finePrint);
        h.FSC_message.setText(s.message);
        h.FSC_venue_name.setText(s.venueModel.venueName);
        h.FSC_users.setText("" + s.venueModel.usersCount);
        h.FSC_tips.setText("" + s.venueModel.tipCount);
        h.FSC_special.setText(s.title);
        int height = 0;
        if (s.height != null) {
            int width = Integer.parseInt(s.width);
            height = Integer.parseInt(s.height);
            h.FSC_image.setMaxWidth(width);
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(mCtx.getDrawable(R.drawable.back12)).build();
        ImageLoader.getInstance().displayImage(s.icon, h.FSC_image, options);
        h.FSC_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListeners.onDirection(s.venueModel.locationModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView FSC_venue_name, FSC_special, FSC_cat_name, FSC_message,
                FSC_fine_print, FSC_checkins, FSC_users, FSC_tips;
        ImageView FSC_direction, FSC_image;

        public Holder(View itemView) {
            super(itemView);
            FSC_venue_name = (TextView) itemView.findViewById(R.id.FSC_venue_name);
            FSC_special = (TextView) itemView.findViewById(R.id.FSC_special);
            FSC_cat_name = (TextView) itemView.findViewById(R.id.FSC_cat_name);
            FSC_message = (TextView) itemView.findViewById(R.id.FSC_message);
            FSC_fine_print = (TextView) itemView.findViewById(R.id.FSC_fine_print);
            FSC_checkins = (TextView) itemView.findViewById(R.id.FSC_checkins);
            FSC_users = (TextView) itemView.findViewById(R.id.FSC_users);
            FSC_tips = (TextView) itemView.findViewById(R.id.FSC_tips);
            FSC_direction = (ImageView) itemView.findViewById(R.id.FSC_direction);
            FSC_image = (ImageView) itemView.findViewById(R.id.FSC_image);
        }
    }

    public interface ShopSpecialAdapterListeners {
        public void onDirection(LocationModel model);
    }
}
