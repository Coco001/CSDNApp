package com.coco.csdnapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coco.csdnapp.NewsContentActivity;
import com.coco.csdnapp.R;
import com.coco.csdnapp.adapter.NewsItemAdapter;
import com.coco.csdnapp.bean.CommonException;
import com.coco.csdnapp.bean.NewsItem;
import com.coco.csdnapp.bean.NewsItemBiz;
import com.coco.csdnapp.dao.NewsItemDao;
import com.coco.csdnapp.utils.AppUtil;
import com.coco.csdnapp.utils.Constaint;
import com.coco.csdnapp.utils.NetUtil;
import com.coco.csdnapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面的fragment
 */

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    private static final int LOAD_FIRST = 0x100;
    private static final int LOAD_MORE = 0x110;
    private static final int LOAD_REFREASH = 0x111;

    private static final int TIP_ERROR_NO_NETWORK = 0X112;
    private static final int TIP_ERROR_SERVER = 0X113;

    private boolean isFirstIn = true;//是否是第一次进入

    private boolean isConnNet = false;//是否连接网络

    private boolean isLoadingDataFromNetWork;//当前数据是否是从网络中获取的

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

    private NewsItemDao mNewsItemDao;//与数据库交互

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
        mNewsItemDao = new NewsItemDao(getActivity());

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swip_layout);
        mListView = (ListView) getView().findViewById(R.id.id_listView);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(LOAD_REFREASH);
            }
        });

        mListView.setAdapter(mAdapter);
        if (isFirstIn) {
            refreshData(LOAD_FIRST);
            ;//进来时直接刷新
            isFirstIn = false;
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem item = mDatas.get(position);
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("url", item.getLink());
                startActivity(intent);
            }
        });
    }

    private void refreshData(int flag) {
        new LoadDatasTask().execute(flag);
    }

    class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                case LOAD_FIRST:
                    return refreashData();
                case LOAD_MORE:
                    loadMoreData();
                    break;
                case LOAD_REFREASH:
                    return refreashData();
            }
            return -1;

            /*try {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mDatas = newsItems;
            } catch (CommonException e) {
                e.printStackTrace();
            }
            return null;*/
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case TIP_ERROR_NO_NETWORK:
                    ToastUtil.toast(getActivity(), "没有网络连接！");
                    mAdapter.setDatas(mDatas);
                    mAdapter.notifyDataSetChanged();
                    break;
                case TIP_ERROR_SERVER:
                    ToastUtil.toast(getActivity(), "服务器错误！");
                    break;
                default:
                    break;
            }
            mAdapter.addAll(mDatas);
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private Integer refreashData() {
        if (NetUtil.checkNet(getActivity())) {
            isConnNet = true;
            // 获取最新数据
            try {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mAdapter.setDatas(newsItems);

                isLoadingDataFromNetWork = true;
                // 设置刷新时间
                AppUtil.setRefreashTime(getActivity(), newsType);
                // 清除数据库数据
                mNewsItemDao.deleteAll(newsType);
                // 存入数据库
                mNewsItemDao.add(newsItems);

            } catch (CommonException e) {
                e.printStackTrace();
                isLoadingDataFromNetWork = false;
                return TIP_ERROR_SERVER;
            }
        } else {
            isConnNet = false;
            isLoadingDataFromNetWork = false;
            // 从数据库中加载
            List<NewsItem> newsItems = mNewsItemDao.list(newsType, currentPage);
            mDatas = newsItems;

            return TIP_ERROR_NO_NETWORK;
        }
        return -1;
    }

    private void loadMoreData() {
        // 当前数据是从网络获取的
        if (isLoadingDataFromNetWork) {
            currentPage += 1;
            try {
                List<NewsItem> newsItems = mNewsItemBiz.getNewsItems(newsType, currentPage);
                mNewsItemDao.add(newsItems);
                mAdapter.addAll(newsItems);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        } else{
        // 从数据库加载的
            currentPage += 1;
            List<NewsItem> newsItems = mNewsItemDao.list(newsType, currentPage);
            mAdapter.addAll(newsItems);
        }
    }

}
