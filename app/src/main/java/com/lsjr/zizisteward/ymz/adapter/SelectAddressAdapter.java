package com.lsjr.zizisteward.ymz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.ymz.utils.BaseRecyclerAdapter;
import com.lsjr.zizisteward.ymz.utils.BaseRecyclerHolder;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/9 10:59
 */

public class SelectAddressAdapter  extends BaseRecyclerAdapter<String> {

    private Context context;
    private String city;
    public SelectAddressAdapter(Context context, List<String> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
        this.context=context;
    }

    @Override
    protected void convert(BaseRecyclerHolder holder, final String item, final int position) {
        LinearLayout linearLayout=holder.getView(R.id.id_lay);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener!=null){
                    onItemListener.onClick(position,item);
                }
            }
        });
        TextView textView=holder.getView(R.id.id_city);
        textView.setText(item);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    OnItemListener  onItemListener;
    public interface OnItemListener{
        void onClick(int post,String itme);
    }

}
