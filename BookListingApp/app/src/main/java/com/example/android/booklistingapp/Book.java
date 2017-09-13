package com.example.android.booklistingapp;



/**
 * Created by gobov on 5/20/2017.
 */

public class Book {

    private String mBookTitle;

    private String mBookDescription;

    private String mBookAuthor;

    private String mRating;

    private String mUrl;




    public Book (String bookTitle, String bookDescription, String bookAuthor, String rating, String url){

        mBookTitle = bookTitle;

        mBookDescription = bookDescription;

        mBookAuthor = bookAuthor;

        mRating = rating;

        mUrl = url;
    }

    public String getBookTitle(){
        return mBookTitle;
    }

    public String getBookDescription(){
        return mBookDescription;
    }

    public String getBookAuthor(){
        return mBookAuthor;
    }

    public String getRating(){
        return mRating;
    }

    public String getUrl(){
        return mUrl;
    }

}
