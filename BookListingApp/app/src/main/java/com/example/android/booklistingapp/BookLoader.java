package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by gobov on 5/20/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    // CONSTRUCTOR WITH THE CLASS CONTEXT AND A STRING URL TO LOAD DATA
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        // GETTING THE BOOKS (URL)
        List<Book> result = QueryUtils.fetchBooks(mUrl);

        return result;
    }
}
