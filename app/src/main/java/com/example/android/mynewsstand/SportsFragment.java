package com.example.android.mynewsstand;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SportsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = SportsFragment.class.getName();
    private static final String GUARDIAN_QUERY_SPORTS = "https://content.guardianapis.com/search?q=sports&show-elements=image&api-key=f809e8e1-c6c7-43c4-96ab-be193527023e&show-fields=thumbnail&show-tags=contributor";
    public TextView mEmptyStateTextView;
    /**
     * Adapter for the list of news
     */
    private NewsListsAdapter mAdapter;

    public SportsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_item_list, container, false);
        final ArrayList<News> news = new ArrayList<News>();

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsListsAdapter(getActivity(), news);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current news story that was clicked on
                News currentNew = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNew.getNewsUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
         getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(0, null, this).forceLoad();
        } else {

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        return rootView;
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG, "test: onCreateLoader() called...");


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());


        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minStories = sharedPrefs.getString(
                getString(R.string.settings_min_articles_key),
                getString(R.string.settings_min_articles_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_QUERY_SPORTS);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("page-size", minStories);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        Log.i(LOG_TAG, "test: url is " +uriBuilder.toString());
        return new NewsLoader(getActivity().getApplication(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Clear the adapter of previous news data
        Log.i(LOG_TAG, "test onLoadFinished() called...");
        mAdapter.clear();

        // Hide loading indicator because the data has been loaded

        View loadingIndicator = getView().findViewById(R.id.loading_indicator);

        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No newss found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i(LOG_TAG, "test: onLoaderReset() called...");
        mAdapter.clear();
    }


}
