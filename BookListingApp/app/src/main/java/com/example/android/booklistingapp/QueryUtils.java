package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gobov on 5/20/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){
    }

    public static List<Book> fetchBooks(String requestUrl){

        // CREATING AN URL OBJECT FOR MAKING A REQUEST
        URL url = createUrl(requestUrl);

        // MAKING A REQUEST
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "fetchBooks ", e);
        }

        // PARSING THE JSON STRING INTO A CREATED LIST
        List<Book> bookList = extractBooks(jsonResponse);

        return bookList;

    }

    // TO EXTRACT THE BOOK DATA IN AN ARRAY LIST
    private static List<Book> extractBooks(String bookJson) {

        // TO RETURN EARLY IF THERE'S NO DATA
        if (TextUtils.isEmpty(bookJson)){
            return null;
        }

        // CREATING AN EMPTY ARRAY LIST OT ADD DATA TO
        ArrayList<Book> books = new ArrayList<>();

        // TRYING THE PARSE THE JSON OBJECT AND THROWING AN EXCEPTION IF THERE'S A PROBLEM
        try {

            // CREATING A JSON OBJECT
            JSONObject baseJsonResponse = new JSONObject(bookJson);

            // CREATING A LIST OF JSON OBJECTS
            JSONArray bookJsonArray = baseJsonResponse.getJSONArray("items");

            // GOING THRU AN ARRAY LIST OF OBJECTS TO GET THE NEEDED DATA USING KEYWORDS
            for (int i = 0; i < bookJsonArray.length(); i++){

                JSONObject currentBook = bookJsonArray.getJSONObject(i);
                JSONObject bookProperties = currentBook.getJSONObject("volumeInfo");

                String title;
                String description;
                String rating;
                String url;

                // CHECKING IF THE KEY EXISTS AND RETURNING THE VALUES
                if (bookProperties.has("title")){
                    title = bookProperties.getString("title");
                } else {
                    title = "N/A";
                }

                // CREATING A JSON ARRAY TO EXTRACT THE AUTHOR STRINGS FROM IT
                JSONArray authorsArray = null;
                String author = "";

                // CHECKING IF THE KEY EXISTS AND RETURNING THE VALUES
                if (bookProperties.has("authors")) {
                    authorsArray = bookProperties.getJSONArray("authors");

                    // GOING THRU AN ARRAY LIST OF STRINGS TO GET THE NEEDED DATA
                    for (int a = 0; a < authorsArray.length(); a++) {
                        author = authorsArray.getString(a);
                    }
                } else {
                    author = "N/A";
                }

                // CHECKING IF THE KEY EXISTS AND RETURNING THE VALUES
                if (bookProperties.has("description")){
                    description = bookProperties.getString("description");
                } else {
                    description = "N/A";
                }

                // CHECKING IF THE KEY EXISTS AND RETURNING THE VALUES
                if (bookProperties.has("averageRating")){
                    Double ratingDouble = bookProperties.getDouble("averageRating");

                    // CONVERTING THE DOUBLE VALUE TO A STRING
                    rating = String.valueOf(ratingDouble);
                } else {
                    rating = "N/A";
                }

                // RETURNING THE STRING VALUES FOR THE BOOK LINK
                url = bookProperties.getString("previewLink");

                // CREATING A BOOK OBJECT AND ADDING IT TO THE LIST
                Book book = new Book(title, description, author, rating, url);
                books.add(book);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // RETURN BOOK LIST
        return books;
    }


    // TO CREATE A URL OBJECT USING A STRING PARAM
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    // TO MAKE A REQUEST WITH A ULR PARAM AND TO RECEIVE A STRING
    private static String makeHttpRequest(URL url) throws IOException {
        // TO RETURN EARLY
        String jsonResponse = null;

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        // TO TRY TO ESTABLISH A URL CONNECTION WITH A REQUEST KEYWORD AND CATCHING THE EXCEPTION IF THERE'S A PROBLEM WITH IT
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            // IF CONNECTION IS FULFILLED READ FROM STREAM
            if (connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                jsonResponse = readStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }

            // CATCHING EXCEPTION
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results." + e);

            // TO DISCONNECT AND CLOSE IF CONNECTION IS EXECUTED
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    // TO READ THE STREAM OF BYTES AND TRANSLATE IT TO A STRING
    private static String readStream(InputStream stream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (stream != null){
            InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }


}
