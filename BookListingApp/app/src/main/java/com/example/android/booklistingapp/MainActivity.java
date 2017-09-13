package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    // DEFAULT SEARCH QUERY
    private static final String API_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";

    private String editTextInput;

    private EditText editTextView;

    private ImageButton searchButton;

    private TextView emptyStateTextView;

    private ProgressBar progressBar;

    // FINAL SEARCH QUERY WITH THE SEARCH KEYWORD
    private String searchQuery = API_QUERY;

    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CHECKING THE STATE OF NETWORK CONNECTIVITY WITH MANAGER
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // GETTING THE INFO OF THE DATA NETWORK
        final NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        // ADAPTER FOR THE LIST OF BOOKS
        adapter = new BookAdapter(this, new ArrayList<Book>());

        // FINDING THE VIEWS
        editTextView = (EditText) findViewById(R.id.search_text_view);
        emptyStateTextView = (TextView) findViewById(R.id.empty_state_view);
        final ListView bookListView = (ListView) findViewById(R.id.list);

        // SETTING THE ADAPTER, EMPTY VIEW AND BLOCKING THE CHILD VIEWS FOCUS FOR ITEM CLICKS ON LIST VIEW
        bookListView.setAdapter(adapter);
        bookListView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        bookListView.setEmptyView(emptyStateTextView);

        // FINDING THE SEARCH BUTTON AND SETTING THE LISTENER ON IT
        searchButton = (ImageButton) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextView != null){

                    // (FIXED) TO GET THE DATA IF THERE'S CONNECTION WITH INITIALIZING THE LOADER AND (RESTARTING IF LIST VIEW != NULL)
                    if (netInfo != null && netInfo.isConnected()) {
                        LoaderManager loaderManager = getLoaderManager();
                        if (bookListView == null){
                            loaderManager.initLoader(0, null, MainActivity.this);
                        } else {
                            loaderManager.restartLoader(0, null, MainActivity.this);
                        }

                    } else {
                        progressBar = (ProgressBar) findViewById(R.id.progress);
                        progressBar.setVisibility(View.GONE);

                        // TO SHOW THE ERROR IF THERE'S NO CONNECTION
                        emptyStateTextView.setText(R.string.no_internet_connection);
                    }

                    // GETTING THE USERS SEARCH INPUT AND SETTING THE NEW QUERY
                    editTextInput = editTextView.getText().toString();
                    searchQuery = API_QUERY + editTextInput + "&maxResults=10";
                    Log.v(LOG_TAG, searchQuery);

                    // TO RELOAD FOR THE SECOND OR THIRD SEARCH


                } else {
                    // IF THERE'S NO USER INPUT
                    emptyStateTextView.setText("No search input");
                    searchButton.setEnabled(false);
                }
            }
        });

        // SETTING THE LISTENER ON A LIST VIEW TO OPEN URL
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // GET THE SELECTED BOOK
                Book currentBook = adapter.getItem(position);

                // URI OBJECT FROM STRING TO USE IN INTENT
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // INTENT TO OPEN A WEBSITE
                Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(webIntent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        // CREATING A NEW LOADER TO RETURN
        return new BookLoader(this, searchQuery);

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // CLEAR FROM PREVIOUS DATA
        adapter.clear();

        // TO ADD A LIST TO THE ADAPTER IF THERE IS ONE
        if (books != null && !books.isEmpty()){
            adapter.addAll(books);
        }

        // SETTING THE TEXT ON A VIEW IF THERE'S NO RESULTS AND REMOVING THE PROGRESS BAR
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_books_found);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        // CLEARING THE PREVIOUS DATA
        adapter.clear();
    }
}
