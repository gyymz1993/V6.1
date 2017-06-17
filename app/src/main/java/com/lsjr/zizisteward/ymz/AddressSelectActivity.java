package com.lsjr.zizisteward.ymz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.ymz.adapter.SelectAddressAdapter;
import com.lsjr.zizisteward.ymz.contrl.AddressContrl;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/9 10:49
 */

public class AddressSelectActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SelectAddressAdapter adapter;
    private AddressContrl addressContrl;
    private TextView tvHint;
    int state = 1;
    private List<String> listP;
    private String hint;
    String mCurrentProviceName, mCurrentCityName, mCurrentDistrictName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }



    @SuppressLint("LongLogTag")
    public void initView() {
        tvHint = (TextView) findViewById(R.id.it_tv_hint);
        tvHint.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.id_rv);
        addressContrl = new AddressContrl(getApplication());
        listP = addressContrl.listP;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SelectAddressAdapter(getApplication(), listP, R.layout.item_select_address);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(new SelectAddressAdapter.OnItemListener() {
            @Override
            public void onClick(int post, String item) {
                if (state == 1) {
                    tvHint.setVisibility(View.VISIBLE);
                    mCurrentProviceName = item;
                    hint = "您当前选择了：" + item;
                    tvHint.setText(hint);
                    List<String> city = addressContrl.getCity(post);
                    endNetRequest(city);
                    return;
                }
                if (state == 2) {
                    mCurrentCityName = item;
                    List<String> eare = addressContrl.getEare(post);
                    hint += "/" + item;
                    tvHint.setText(hint);
                    endNetRequest(eare);
                } else {
                    mCurrentDistrictName = item;
                    Intent intent = getIntent();
                    intent.putExtra("address", hint);
                    intent.putExtra("province", mCurrentProviceName);
                    intent.putExtra("city", mCurrentCityName);
                    intent.putExtra("area", mCurrentDistrictName);
                    intent.putExtra("zipCode", "");
                    setResult(77, intent);
                    finish();
                }
            }
        });
    }

    private void endNetRequest(List<String> eare) {
        listP.clear();
        listP.addAll(eare);
        adapter.notifyDataSetChanged();
        state++;

    }

    @Override
    public int getContainerView() {
        return R.layout.activity_add_address_select;
    }


}
