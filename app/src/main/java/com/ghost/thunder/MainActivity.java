package com.ghost.thunder;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ghost.thunder.demo.R;

import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

public class MainActivity extends AppCompatActivity
{
    EditText inputUrl;
    Button btnDownload;
    TextView tvStatus;
    Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == 0)
            {
                long taskId = (long) msg.obj;
                XLTaskInfo taskInfo = XLTaskHelper.instance(getApplicationContext()).getTaskInfo(taskId);
                tvStatus.setText(
                        "fileSize:" + taskInfo.mFileSize
                                + " downSize:" + taskInfo.mDownloadSize
                                + " speed:" + convertFileSize(taskInfo.mDownloadSpeed)
                                + "/s dcdnSpeed:" + convertFileSize(taskInfo.mAdditionalResDCDNSpeed)
                                + "/s filePath:" + "/sdcard/" + XLTaskHelper.instance(getApplicationContext())
                                .getFileName(inputUrl.getText().toString())
                );
                handler.sendMessageDelayed(handler.obtainMessage(0, taskId), 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.ghost.thunder.R.layout.activity_main);
        XLTaskHelper.init(getApplicationContext());
        inputUrl = (EditText) findViewById(com.ghost.thunder.R.id.input_url);
        inputUrl.setText("ed2k://|file|kbjs.1080p" +
                ".BD%E4%B8%AD%E8%8B%B1%E5%8F%8C%E5%AD%97[%E6%9C%80%E6%96%B0%E7%94%B5%E5%BD%B1www.hao6v.com]" +
                ".mp4|2492910142|2998F2B2335958FA007855DEF047A12F|h=MP3HALS6LXHX3RQVMU35ZDVL4GTKVLXM|/");
        btnDownload = (Button) findViewById(com.ghost.thunder.R.id.btn_down);
        tvStatus = (TextView) findViewById(com.ghost.thunder.R.id.tv_status);
        btnDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(inputUrl.getText()))
                {
                    long taskId = 0;
                    try
                    {
                        taskId = XLTaskHelper.instance(getApplicationContext()).addThunderTask(inputUrl.getText()
                                .toString(), "/sdcard/", null);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    handler.sendMessage(handler.obtainMessage(0, taskId));
                    String loclUrl = XLTaskHelper.instance(getApplicationContext()).getLoclUrl("/sdcard/" +
                            XLTaskHelper.instance(getApplicationContext()).getFileName(inputUrl.getText().toString()));

                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra(VideoActivity.KEY_VIDEO_URL, loclUrl);
                    startActivity(intent);
                }
            }
        });
    }

    public static String convertFileSize(long size)
    {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f M" : "%.1f M", f);
        } else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f K" : "%.1f K", f);
        } else
            return String.format("%d B", size);
    }
}
