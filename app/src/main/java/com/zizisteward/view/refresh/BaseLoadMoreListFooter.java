/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zizisteward.view.refresh;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;


public abstract class BaseLoadMoreListFooter extends LinearLayout {
    private State state; // ״
    private View contentView;

    public BaseLoadMoreListFooter(Context context) {
        super(context);
        setClickable(true);
        setLayoutParams(new AbsListView.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(contentView = onGetContentView(),
                new LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        toggleToNormalState();
    }


    public void toggleToNormalState() {
        setState(State.NOMRAL);
        onToggleToNormalState();
    }


    public void toggleToLoadingState() {
        setState(State.LOADING);
        onToggleToLoadingState();
    }


    public abstract View onGetContentView();


    public abstract void onToggleToNormalState();

    public abstract void onToggleToLoadingState();


    public abstract void onToggleToLoadAllState();


    public enum State {

        NOMRAL,

        LOADING,


        LOADAll;
    }


    public State getState() {
        return state;
    }


    public void setState(State state) {
        this.state = state;
    }

    /**
     * ��ȡ������ͼ
     *
     * @return ������ͼ
     */
    public View getContentView() {
        return contentView;
    }

    /**
     * ����������ͼ
     *
     * @param contentView ������ͼ
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
}