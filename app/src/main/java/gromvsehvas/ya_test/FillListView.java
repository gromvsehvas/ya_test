package gromvsehvas.ya_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by gromvsehvas on 4/22/2016.
 */
public class FillListView extends BaseAdapter {

    private final LayoutInflater mInflater;
    private TitleArr[] clTitleArr;
    private final Context mContext;


    public FillListView(Context context, TitleArr[] mTArr ) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clTitleArr = mTArr;
        mContext = context;
    }

    @Override
    public int getCount() {
        return clTitleArr == null ? 0 : clTitleArr.length;
    }

    @Override
    public Object getItem(int position) {
        return clTitleArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = convertView != null ? convertView
                : mInflater.inflate(R.layout.table_line, parent, false);

        final TitleArr LineTitle = clTitleArr[position];
        if (LineTitle == null)
            return view;

        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(LineTitle.strTitle);

        final TextView tvGenres = (TextView) view.findViewById(R.id.tvGenres);
        tvGenres.setText(LineTitle.strGenres);

        String strSong = String.format("%s, %s",
                LineTitle.strAlbums,
                LineTitle.strTracks);

        final TextView tvAlbumsAndTracks = (TextView) view.findViewById(R.id.tvAlbumsAndTracks);
        tvAlbumsAndTracks.setText(strSong);

        final ImageView ivCoverSmall = (ImageView) view.findViewById(R.id.ivCoverSmall);
        Picasso.with(mContext)
                .load(LineTitle.strCoverSmall)
                .placeholder(R.drawable.ic_download_cover)
                .error(R.drawable.ic_error_download)
                .into(ivCoverSmall);

        return view;
    }
}



