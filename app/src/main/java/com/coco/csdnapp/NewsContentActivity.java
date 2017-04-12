package com.coco.csdnapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.coco.csdnapp.adapter.NewContentAdapter;
import com.coco.csdnapp.bean.CommonException;
import com.coco.csdnapp.bean.News;
import com.coco.csdnapp.bean.NewsItemBiz;
import java.util.List;

/**
 * 显示详情页面的activity.
 */

public class NewsContentActivity extends AppCompatActivity {

    private ListView mListView;
    private SwipeRefreshLayout mRefreshLayout;

    /**
     * 该页面的url
     */
    private String url;
    private NewsItemBiz mNewsItemBiz;
    private List<News> mDatas;
    private ProgressBar mProgressBar;
    private NewContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        mNewsItemBiz = new NewsItemBiz();

        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");

        mAdapter = new NewContentAdapter(this);

        mListView = (ListView) findViewById(R.id.content_listView);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_swip_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.id_newsContentPro);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLoadMore();
            }
        });

        onLoadMore();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News news = mDatas.get(position - 1);
                String imageLink = news.getImageLink();
                Intent intent = new Intent(NewsContentActivity.this, ImageShowActivity.class);
                intent.putExtra("url", imageLink);
                startActivity(intent);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        new LoadDataTask().execute();

    }

    public void onLoadMore() {
        new LoadDataTask().execute();
    }

    class LoadDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                mDatas = mNewsItemBiz.getNews(url).getNewses();
            } catch (CommonException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mDatas == null)
                return;
            mAdapter.addList(mDatas);
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }

    }

    /**
     * 点击返回按钮
     */
    public void back(View view) {
        finish();
    }

}
