package com.lsjr.zizisteward.basic;

import com.lsjr.zizisteward.http.MyError;

/**
 * 
 */
public interface HttpListener {
    void onSuccess(Object o);

    void onFailure(MyError error);

    void onTotalCount(int totalCount);
}
