package com.lsjr.zizisteward.ymz.contrl;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.lsjr.zizisteward.ymz.bean.PcaBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/9 11:10
 */

public class AddressContrl {

    private Context context;

    public AddressContrl(Context context) {
        this.context = context;
        initListP();

    }
    private Map<Integer, Map<String, List<String>>> integerMapMap = new HashMap<>();
    private Map<Integer, Map<String, List<String>>> integerMapMap1 = new HashMap<>();
    // 省份
    private List<String> mProvinceDatas = new ArrayList<>();
    // 城市
    private List<String> mCitiesDatas = new ArrayList<>();
    // 地区
    private List<String> mAreaDatas = new ArrayList<>();
    // 存储省对应的所有市
    //private Map<String, List<String>> mProvinceDatasMap = new HashMap<>();

    // 存储省对应的所有市
    private Map<String, List<String>> mCitiesDataMap = new HashMap<>();
    // 存储市对应的所有区
    //private Map<String, List<String>> mAreaDataMap = new HashMap<>();

    public void init(){
        //final List<PcaBean.CitysBean> citys = addressContrl.getPcaBean().getCitys();

//        for (int c = 0; c < citys.size(); c++) {
//            mProvinceDatas.add(citys.get(c).getName());
//        }
//        for (int i = 0; i < citys.size(); i++) {
//            Map<String, List<String>> mProvinceDatasMap = new HashMap<>();
//            List<String> mProvinceDatas = new ArrayList<>();
//            String mProvinceName = citys.get(i).getName();
//
//            for (int j = 0; j < citys.get(i).getCity().size(); j++) {
//                String name = citys.get(i).getCity().get(j).getName();
//                mProvinceDatas.add(name);
//                mProvinceDatasMap.put(mProvinceName, mProvinceDatas);
//                // 存储市对应的所有区
//                List<String> area = citys.get(i).getCity().get(j).getArea();
//                Map<String, List<String>> mAreaDataMap = new HashMap<>();
//
//                mAreaDataMap.put(name, area);
//                Iterator<Map.Entry<String, List<String>>> iterator = mAreaDataMap.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, List<String>> next = iterator.next();
//                    Object key = next.getKey();
//                    Object val = next.getValue();
//                    Log.e("mAreaDataMap key:", "key:" + key + "------val:" + val.toString());
//                }
//
//                integerMapMap1.put(j,mAreaDataMap);
//            }
//            integerMapMap.put(i, mProvinceDatasMap);
//        }

    }

    private String readPCA(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream;
        try {
            outputStream = new ByteArrayOutputStream();
            AssetManager asset = context.getAssets();
            inputStream = asset.open("PCA.json");
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            return new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public JSONArray getJSONArray() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readPCA(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public PcaBean getPcaBean() {
        PcaBean pcaBean = new Gson().fromJson(readPCA(context), PcaBean.class);
        return pcaBean;
    }

    /**
     * 获取省列表
     */
    JSONArray jsonArrayP;
    public List<String> listP = new ArrayList<>();

    public void initListP() {
        jsonArrayP = getJSONArray();
        try {
            listP.clear();
            if (jsonArrayP != null) {
                for (int i = 0; i < jsonArrayP.length(); i++) {
                    JSONObject objP = jsonArrayP.getJSONObject(i);//获取省份对象
                    listP.add(objP.getString("name"));//获取省份名字
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据点击的省份，获取该省的城市列表
     *
     * @param provinceNum
     */
    JSONArray jsonArrayC;

    public List<String> getCity(int provinceNum) {
        List<String> listC = new ArrayList<>();
        JSONObject jsonObjP;
        listC.clear();
        try {
            jsonObjP = jsonArrayP.getJSONObject(provinceNum);//获取点击的省份对象
            jsonArrayC = jsonObjP.getJSONArray("city");//获取该省份的城市数组
            for (int i = 0; i < jsonArrayC.length(); i++) {
                JSONObject objC = jsonArrayC.getJSONObject(i);//获取城市数组的城市对象
                listC.add(objC.getString("name"));//获取城市名字
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return listC;
    }

    /**
     * 根据点击的城市获得地区列表
     *
     * @param cityNum
     */
    public List<String> getEare(int cityNum) {
        List<String> listA = new ArrayList<>();
        JSONObject jsonObjC;
        listA.clear();
        try {
            jsonObjC = jsonArrayC.getJSONObject(cityNum);//根据点击的城市对象
            JSONArray arrayA = jsonObjC.getJSONArray("area");//获取该城市的地区列表
            for (int i = 0; i < arrayA.length(); i++) {
                listA.add(arrayA.getString(i));//添加地区列表到list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listA;
    }
}
