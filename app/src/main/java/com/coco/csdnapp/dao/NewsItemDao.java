package com.coco.csdnapp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coco.csdnapp.bean.CommonException;
import com.coco.csdnapp.bean.News;
import com.coco.csdnapp.bean.NewsItem;
import com.coco.csdnapp.utils.DataUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作数据库.
 */

public class NewsItemDao {
    private DBHelper dbHelper;

    public NewsItemDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void add(NewsItem newsItem) {
        String sql = "insert into tb_newsItem (title,link,date,imgLink,content,newstype) values(?,?,?,?,?,?) ;";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, new Object[]{newsItem.getTitle(), newsItem.getLink(), newsItem.getDate(), newsItem.getImgLink(),
                newsItem.getContent(), newsItem.getNewsType()});
        db.close();
    }

    public void deleteAll(int newsType) {
        String sql = "delete from tb_newsItem where newstype = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, new Object[]{newsType});
        db.close();
    }

    public void add(List<NewsItem> newsItems) {
        for (NewsItem newsItem : newsItems) {
            add(newsItem);
        }
    }

    /**
     * 根据newsType和currentPage从数据库中取数据
     *
     * @param newsType
     * @param currentPage
     * @return
     */
    public List<NewsItem> list(int newsType, int currentPage) {

        // 0 -9 , 10 - 19 ,
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        try {
            int offset = 10 * (currentPage - 1);
            String sql = "select title,link,date,imgLink,content,newstype from tb_newsItem where newstype = ? limit ?,? ";
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(sql, new String[]{newsType + "", offset + "", "" + (offset + 10)});

            NewsItem newsItem = null;

            while (c.moveToNext()) {
                newsItem = new NewsItem();

                String title = c.getString(0);
                String link = c.getString(1);
                String date = c.getString(2);
                String imgLink = c.getString(3);
                String content = c.getString(4);
                Integer newstype = c.getInt(5);

                newsItem.setTitle(title);
                newsItem.setLink(link);
                newsItem.setImgLink(imgLink);
                newsItem.setDate(date);
                newsItem.setNewsType(newstype);
                newsItem.setContent(content);

                newsItems.add(newsItem);

            }
            c.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsItems;

    }
}
