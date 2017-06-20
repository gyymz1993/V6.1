package com.lsjr.zizisteward.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.utils.NetUtils;
import com.shove.Convert;
import com.shove.security.Encrypt;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpClientGet {
    protected static final String USERID_ERROR = "解析用户id有误";
    protected static final String ERROR = "error";
    protected static final String MSG = "msg";
    private static RequestQueue requestQueue = null;
    private StringRequest request;

    private RequestQueue getIntances(Context context) {
        if (requestQueue == null) {
            return Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    @SuppressWarnings({"rawtypes", "unused"})
    public HttpClientGet(final Context context, Object tag, Map<String, String> map, final boolean isCache,
                         final CallBacks callBack) {
        try {
            requestQueue = getIntances(context);
            String url = buildUrl(HttpConfig.HOST, "TNNYF17DbevNyxVv", map);
            final String requestData = new Gson().toJson(map);
            final String cacheUrl = url;
            Log.i("test", " " + cacheUrl);
            request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    response = response.replace("null", "\"\"");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.isNull(ERROR)) {
                            String error = jsonObject.getString(ERROR);
                            String msg = jsonObject.getString(MSG);
                            if (error.equals("1")) {
                                if (response != null) {
                                    callBack.onSuccess(response);
                                } else {
                                    callBack.onFailure(
                                            new MyError(ErrorType.backgroundError, "result value is empty!"));
                                }

                            } else {
                                if (msg.contains(USERID_ERROR)) {

                                } else if (msg.contains("javax.net")) {

                                } else {
                                    callBack.onFailure(new MyError(ErrorType.backgroundError, msg));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callBack.onFailure(new MyError(ErrorType.localError, error.getMessage()));
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(500 * 1000, 1, 1.0f));
            if (NetUtils.isNetworkAvailable(context)) {
                addRequest(requestQueue, request, tag);
            } else {
                callBack.onFailure(new MyError(ErrorType.netError, "网络未连接"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void addRequest(RequestQueue mQueue, Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);

        }
        mQueue.add(request);
    }

    public static void cancelRequest(Object tag) {
        requestQueue.cancelAll(tag);
    }

    public static abstract class CallBacks<T> {
        Type type;

        public CallBacks() {
            type = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onSuccess(String result);

        public void onFailure(MyError myError) {
            Toast.makeText(App.getContext(), myError.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }

        public void onTotalCount(int totalCount) {
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String buildUrl(String urlBase, String key, Map<String, String> parameters)
            throws UnsupportedEncodingException {
        String urlParam = "";
        if ((parameters.containsKey("_s")) || (parameters.containsKey("_t"))) {
            throw new RuntimeException("在使用 buildUrl 方法构建通用 REST 接口 Url 时，不能使用 _s, _t 此保留字作为参数名");
        }

        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("在使用 buildUrl 方法构建通用 REST 接口 Url 时，必须提供一个用于摘要签名用的 key (俗称 MD5 加盐)");
        }

        parameters.put("_t", Convert.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));

        List parameterNames = new ArrayList(parameters.keySet());
        Collections.sort(parameterNames);

        if ((!urlBase.endsWith("?")) && (!urlBase.endsWith("&"))) {
            urlBase = urlBase + (urlBase.indexOf("?") == -1 ? "?" : "&");
        }

        String signData = "";

        for (int i = 0; i < parameters.size(); i++) {
            String _key = (String) parameterNames.get(i);
            String _value = (String) parameters.get(_key);

            signData = signData + _key + "=" + _value;
            urlParam = urlParam + _key + "=" + URLEncoder.encode(_value, "utf-8");

            if (i < parameters.size() - 1) {
                signData = signData + "&";
                urlParam = urlParam + "&";
            }
        }

        urlBase = urlBase + "_t=" + URLEncoder.encode(parameters.get("_t"), "utf-8") + "&_p="
                + Encrypt.encrypt3DES(urlParam, key) + "&_s="
                + Encrypt.MD5(new StringBuilder(String.valueOf(signData)).append(key).toString(), "utf-8");

        return urlBase;
    }

}
