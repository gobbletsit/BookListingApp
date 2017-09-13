package com.example.android.booklistingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;



/**
 * Created by gobov on 5/20/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();

    private static class ViewHolder{
        TextView titleView, descriptionView, authorView, ratingView;
    }

    public BookAdapter (Activity context, ArrayList<Book> books){
        super(context,0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        // CREATING A VIEW HOLDER
        ViewHolder viewHolder = new ViewHolder();

        // FINDING THE VIEWS
        viewHolder.titleView = (TextView) convertView.findViewById(R.id.title_text_view);
        viewHolder.descriptionView = (TextView) convertView.findViewById(R.id.description_text_view);
        viewHolder.authorView = (TextView) convertView.findViewById(R.id.author_text_view);
        viewHolder.ratingView = (TextView) convertView.findViewById(R.id.rating_text_view);

        // GETTING AND SETTING THE TEXT ON THE VIEWS
        viewHolder.titleView.setText(currentBook.getBookTitle());
        viewHolder.descriptionView.setText(currentBook.getBookDescription());
        viewHolder.authorView.setText(currentBook.getBookAuthor());
        viewHolder.ratingView.setText(currentBook.getRating());

        return convertView;

    }
}
