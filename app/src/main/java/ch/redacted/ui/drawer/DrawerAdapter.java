package ch.redacted.ui.drawer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;
import ch.redacted.data.local.DrawerItem;

public class DrawerAdapter extends BaseAdapter {

    Context mContext;

    public DrawerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return DrawerItem.values().length;
    }

    @Override
    public DrawerItem getItem(int position) {
        return DrawerItem.values()[position];
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_drawer_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(getItem(position).name());

        switch (position){
            case 0:
                holder.icon.setImageDrawable(
                    AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_announcement_48px));
                break;
            case 1:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_profile_48px));
                break;
            case 2:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_top_10_48px));
                break;
            case 3:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_torrents_48px));
                break;
            case 4:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_requests_48px));
                break;
            case 5:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_insert_chart_black_24px));
                break;
            case 6:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_forum_48px));
                break;
            case 7:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_users_48px));
                break;
            case 8:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_artists_48px));
                break;
            case 9:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_bookmark_24dp));
                break;
            case 10:
                holder.icon.setImageDrawable(AppCompatResources.getDrawable(convertView.getContext(), R.drawable.ic_inbox_48dp));
                break;
        }

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.textView) TextView textView;
        @BindView(R.id.icon) ImageView icon;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
