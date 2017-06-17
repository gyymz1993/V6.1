package com.lsjr.zizisteward.adapter;

import java.util.List;

import com.lsjr.zizisteward.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




public class AreasAdapter extends BaseAdapter {

    private Context context;
    private List<String> texts;
    private int checkItemPosition = 0;
    private TextView textView;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public AreasAdapter(Context context, List<String> texts) {
        this.context = context;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("null")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
        	viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area, null);
            viewHolder.mText = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(texts.get(position));
    }

    static class ViewHolder {
        TextView mText;
    }
}
