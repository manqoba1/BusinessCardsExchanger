package codetribe.sifiso.com.bcelibrary.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.BCEApp;
import codetribe.sifiso.com.bcelibrary.Models.DrawerItem;
import codetribe.sifiso.com.bcelibrary.R;

/**
 * Created by sifiso on 2015-09-24.
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.Holder> {
    private final LayoutInflater inflater;

    private List<DrawerItem> mList = Collections.emptyList();
    DrawerItemAdapterListener mListener;
    Context mCtx;

    public interface DrawerItemAdapterListener {
        public void onDrawerItemClick(int position);
    }

    public DrawerItemAdapter(Context ctx, List<DrawerItem> list, DrawerItemAdapterListener listener) {
        inflater = LayoutInflater.from(ctx);
        mList = list;
        mListener = listener;
        mCtx = ctx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        DrawerItem item = mList.get(position);
        holder.drawerIcon.setImageResource(item.iconId);
        holder.drawerTitle.setText(item.title);
        holder.drawerTitle.setTypeface(BCEApp.getNeutonItalic(mCtx));
        holder.itemContainer.setOnClickListener(new OnItemClick(position));
        if (item.count > 0) {
            holder.drawerCount.setText(item.count);
            holder.drawerCount.setVisibility(View.VISIBLE);
        } else {
            holder.drawerCount.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView drawerIcon;
        TextView drawerTitle, drawerCount;
        RelativeLayout itemContainer;

        public Holder(View itemView) {
            super(itemView);
            drawerCount = (TextView) itemView.findViewById(R.id.drawerCount);
            drawerTitle = (TextView) itemView.findViewById(R.id.drawerTitle);
            drawerIcon = (ImageView) itemView.findViewById(R.id.drawerIcon);
            itemContainer = (RelativeLayout) itemView.findViewById(R.id.itemContainer);
        }
    }

    private class OnItemClick implements View.OnClickListener {
        int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mListener.onDrawerItemClick(position);
        }
    }
}
