package codetribe.sifiso.com.bcelibrary.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.R;

/**
 * Created by sifiso on 2015-10-14.
 */
public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.Holder> {

    private Context mCtx;
    private List<CaptionModel> mList;

    private final LayoutInflater inflater;
    private GalleryListAdapterListener mListener;

    public GalleryListAdapter(Context mCtx, List<CaptionModel> mList, GalleryListAdapterListener mListener) {
        this.mCtx = mCtx;
        this.mList = mList;
        this.mListener = mListener;
        inflater = LayoutInflater.from(mCtx);
    }

    @Override
    public GalleryListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.full_scale_view_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryListAdapter.Holder h, int position) {
        final CaptionModel model = mList.get(position);
        DateTime date = new DateTime(model.createdTime);
        h.FSC_time.setText( date.toString("HH:mm"));
        h.FSC_name.setText(model.fullName);
        h.FSC_message.setText(model.textMessage);

        ImageLoader.getInstance().displayImage(model.imageUrlStnd, h.FSC_image);
        h.FSC_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFullScaleView(model);
            }
        });
       // Picasso.with(mCtx).load(model.imageUrl).into(h.FSC_image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        ImageView FSC_image;
        TextView FSC_name, FSC_time, FSC_message;

        public Holder(View itemView) {
            super(itemView);
            FSC_image = (ImageView) itemView.findViewById(R.id.FSC_image);
            FSC_message = (TextView) itemView.findViewById(R.id.FSC_message);
            FSC_name = (TextView) itemView.findViewById(R.id.FSC_name);
            FSC_time = (TextView) itemView.findViewById(R.id.FSC_time);
        }

    }

    public interface GalleryListAdapterListener {
        public void onFullScaleView(CaptionModel captionModel);
    }
}
