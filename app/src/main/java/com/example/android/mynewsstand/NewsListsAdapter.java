package com.example.android.mynewsstand;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsListsAdapter extends ArrayAdapter<News> {

public NewsListsAdapter (Activity context, ArrayList<News> news){

    super(context, 0, news);
}

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View listItemView = convertView;
        if (listItemView == null);{
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item_list_view, parent, false);}


        News currentNew = getItem(position);

        ImageView newsImage = listItemView.findViewById(R.id.newsImage);
        try {
            URL url = new URL(currentNew.getNewsImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            newsImage.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
        }

        TextView newsTitle = listItemView.findViewById(R.id.newsTitle);
        newsTitle.setText(currentNew.getNewsHeader());

        TextView newsAuthor = listItemView.findViewById(R.id.newsAuthor);
        newsAuthor.setText(currentNew.getNewsAuthor());

        TextView newsDate = listItemView.findViewById(R.id.newsDate);
        newsDate.setText(currentNew.getNewsDate());

        return listItemView;
    }
}
