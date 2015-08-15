package com.github.jackkell.mimicryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ImpersonatorSelecableAdapter extends BaseAdapter{

    private Context context;
    private List<Impersonator> impersonators;
    private static LayoutInflater inflater = null;

    public ImpersonatorSelecableAdapter(Context context, List<Impersonator> impersonators) {
        this.context = context;
        this.impersonators = impersonators;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return impersonators.size();
    }

    @Override
    public Object getItem(int position) {
        return impersonators.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.util_impersonator_selectable, null);
        }
        TextView impersonatorName = (TextView) view.findViewById(R.id.tvImpersonatorName);
        TextView postCount = (TextView) view.findViewById(R.id.tvPostCount);
        TextView favoriteCount = (TextView) view.findViewById(R.id.tvFavoriteCount);
        TextView tweetCount = (TextView) view.findViewById(R.id.tvTweetCount);
        TextView dateCreated = (TextView) view.findViewById(R.id.tvDateCreated);

        Impersonator impersonator = impersonators.get(position);

        impersonatorName.setText(impersonator.getName());
        postCount.setText(Integer.toString(impersonator.getPostCount()));
        favoriteCount.setText(Integer.toString(impersonator.getIsFavoritedPostCount()));
        tweetCount.setText(Integer.toString(impersonator.getIsTweetedPostCount()));
        dateCreated.setText(impersonator.getDateCreated());

        return view;
    }
}
