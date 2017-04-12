package com.coco.csdnapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.coco.csdnapp.utils.FileUtil;
import com.coco.csdnapp.utils.Http;
import com.coco.csdnapp.view.GestureImageView;

/**
 * 跳转显示的activity.
 */

public class ImageShowActivity extends AppCompatActivity {
    private String url;
    private ProgressBar mLoading;
    private GestureImageView mGestureImageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);

        // 拿到图片的链接
        url = getIntent().getExtras().getString("url");
        mLoading = (ProgressBar) findViewById(R.id.loading);
        mGestureImageView = (GestureImageView) findViewById(R.id.image);

        new DownloadImgTask().execute();

    }

    /**
     * 点击返回按钮
     */
    public void back(View view) {
        finish();
    }

    /**
     * 点击下载按钮
     */
    public void downloadImg(View view) {
        mGestureImageView.setDrawingCacheEnabled(true);
        if (FileUtil.writeSDcard(url, mGestureImageView.getDrawingCache())) {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
        mGestureImageView.setDrawingCacheEnabled(false);
    }

    class DownloadImgTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mBitmap = Http.HttpGetBmp(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mGestureImageView.setImageBitmap(mBitmap);
            mLoading.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }
}
