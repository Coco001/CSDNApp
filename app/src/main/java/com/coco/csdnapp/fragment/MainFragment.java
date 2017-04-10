package com.coco.csdnapp.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.coco.csdnapp.R;
import com.coco.csdnapp.adapter.NewsItemAdapter;
import com.coco.csdnapp.bean.CommonException;
import com.coco.csdnapp.bean.NewsItem;
import com.coco.csdnapp.bean.NewsItemBiz;
import com.coco.csdnapp.utils.Constaint;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页面的fragment
 */

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    private int newsType = Constaint.NEWS_TYPE_YEJIE;//默认的newsType
    private int currentPage = 1;//当前页
    private NewsItemBiz mNewsItemBiz;//处理新闻的业务类
    private NewsItemAdapter mAdapter;//数据适配器
    //swip_layout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //listview
    private ListView mListView;
    //数据
    private List<NewsItem> mDatas = new ArrayList<>();

    public MainFragment(int newsType) {
        this.newsType = newsType;
        mNewsItemBiz = new NewsItemBiz();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_item_fragment_main, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new NewsItemAdapter(getActivity(), mDatas);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swip_layout);
        mListView = (ListView) getView().findViewById(R.id.id_listView);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mListView.setAdapter(mAdapter);
        refreshData();//进来时直接刷新
    }

    //数显数据
    private void refreshData() {
        new LoadDatasTask().execute();
    }

    class LoadDatasTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mDatas = newsItems;
            } catch (CommonException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.addAll(mDatas);
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
