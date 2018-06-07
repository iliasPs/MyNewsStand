package com.example.android.mynewsstand;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class EconomyFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    /** Adapter for the list of earthquakes */
    private NewsListsAdapter mAdapter;
    private static final String GUARDIAN_QUERY = "https://content.guardianapis.com/search?q=sports&show-elements=image&api-key=test&show-fields=thumbnail&show-tags=contributor&order-by=newest";
    public static final String LOG_TAG = SportsFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_item_list, container, false);
        final ArrayList<News> news = new ArrayList<News>();

        mAdapter = new NewsListsAdapter(getActivity(), news);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(mAdapter);


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
        }
        return rootView;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG, "test: onCreateLoader() called...");
        return new NewsLoader(getActivity().getApplication(), GUARDIAN_QUERY);
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Clear the adapter of previous earthquake data
        Log.i(LOG_TAG, "test onLoadFinished() called...");
        mAdapter.clear();

//        // Hide loading indicator because the data has been loaded
//        View loadingIndicator = findViewById(R.id.loading_indicator);
//        loadingIndicator.setVisibility(View.GONE);
//
//        // Set empty state text to display "No earthquakes found."
//        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        //mAdapter.clear();

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