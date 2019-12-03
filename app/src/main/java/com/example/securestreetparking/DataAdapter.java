package com.example.securestreetparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataAdapter extends ArrayAdapter<DataClass> {

    public DataAdapter(Context activity, int resource, ArrayList<DataClass> arrayList) {
        super(activity, resource, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);

        }

        DataClass dataClass = getItem(position);
        TextView textView = convertView.findViewById(R.id.time);
        TextView textView2 = convertView.findViewById(R.id.date);
        ImageView imageView = convertView.findViewById(R.id.picture);
        View threat = convertView.findViewById(R.id.threat);

        imageView.setClipToOutline(true);
        textView.setText(dataClass.getTime());
        textView2.setText(dataClass.getDate());
        Glide.with(imageView.getContext())
                .load(dataClass.getUrl())
                .thumbnail(Glide.with(imageView.getContext()).load(R.drawable.loading_data))
//                .placeholder(R.drawable.icon2)
                .into(imageView);

        if(dataClass.getThreat().equals("no"))
            threat.setVisibility(View.GONE);
        else
            threat.setVisibility(View.VISIBLE);

        return convertView;
    }
}
