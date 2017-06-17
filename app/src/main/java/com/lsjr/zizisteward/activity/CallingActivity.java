package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.webkit.WebView;

import com.lsjr.zizisteward.R;

public class CallingActivity extends Activity {
    private MediaPlayer player;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        WebView webview_call = (WebView) findViewById(R.id.webview_call);
        webview_call.getSettings().setJavaScriptEnabled(true);
        webview_call.loadUrl("http://61.183.72.78:8000/comm.html");
        player = MediaPlayer.create(CallingActivity.this, R.raw.outgoing);
        player.start();
        player.setLooping(true);

        // SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,
        // 5);
        // soundPool.load(this, R.raw.outgoing, 1);
        // soundPool.play(1, 1, 1, 0, -1, 1);

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        player.stop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        player.stop();
    }

}
