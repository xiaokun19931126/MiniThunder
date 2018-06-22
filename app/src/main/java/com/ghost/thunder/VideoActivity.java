package com.ghost.thunder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/06/22
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class VideoActivity extends AppCompatActivity
{

    public static final String KEY_VIDEO_URL = "KEY_VIDEO_URL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.ghost.thunder.R.layout.activity_videos);

        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_VIDEO_URL);

        if (url == null)
        {
            return;
        }
        Uri uri = Uri.parse(url);
        VideoView mVideoView = (VideoView) findViewById(com.ghost.thunder.R.id.video_view);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
    }
}
