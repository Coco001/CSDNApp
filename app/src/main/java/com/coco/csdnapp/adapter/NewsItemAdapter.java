package com.coco.csdnapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coco.csdnapp.R;
import com.coco.csdnapp.bean.NewsItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.csdn.DataUtil;

import java.util.List;

/**
 * NewsItem的adapter.
 */

public class NewsItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<NewsItem> mDatas;

    /**
     * 使用了github开源的ImageLoad进行了数据加载
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public NewsItemAdapter(Context context, List<NewsItem> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.images)
                .showImageForEmptyUri(R.mipmap.images)
                .showImageOnFail(R.mipmap.images)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.news_item_yidong, null);
            holder = new ViewHolder();

            holder.mContent = (TextView) convertView.findViewById(R.id.id_content);
            holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
            holder.mDate = (TextView) convertView.findViewById(R.id.id_date);
            holder.mImg = (ImageView) convertView.findViewById(R.id.id_newsImg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsItem newsItem = mDatas.get(position);
        holder.mTitle.setText(DataUtil.ToDBC(newsItem.getTitle()));
        holder.mContent.setText(newsItem.getContent());
        holder.mDate.setText(newsItem.getDate());
        if (newsItem.getImgLink() != null) {
            holder.mImg.setVisibility(View.VISIBLE);
            imageLoader.displayImage(newsItem.getImgLink(), holder.mImg, options);
        } else {
            holder.mImg.setVisibility(View.GONE);
        }

        return convertView;
    }

    private final class ViewHolder {
        TextView mTitle;
        TextView mContent;
        ImageView mImg;
        TextView mDate;
    }

    public void addAll(List<NewsItem> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<NewsItem> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }
}
