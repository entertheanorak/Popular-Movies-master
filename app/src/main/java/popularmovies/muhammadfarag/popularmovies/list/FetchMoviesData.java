package popularmovies.muhammadfarag.popularmovies.list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import popularmovies.muhammadfarag.popularmovies.Movie;
import popularmovies.muhammadfarag.popularmovies.network.MovieDatabaseServerConnector;
import popularmovies.muhammadfarag.popularmovies.network.UnauthorizedException;

/**
 * Created by muhammadfarag on 9/27/15.
 */
public class FetchMoviesData extends AsyncTask<Void, Void, List<Movie>> {

    private static final int PAGE_NUMBER_1 = 1;
    private Activity mActivity;
    private ProgressDialog pd;
    private boolean unauthorizedExceptionOccured = false;
    private DataSetUpdateListener listener;
    private int mSortCriteria = 0;

    public FetchMoviesData(Activity activity, DataSetUpdateListener listener, int sortCriteria) {
        mActivity = activity;
        this.mSortCriteria = sortCriteria;
        this.listener = listener;
    }

    public FetchMoviesData(Activity activity, DataSetUpdateListener listener) {
        mActivity = activity;
        this.mSortCriteria = 0;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mActivity);
        pd.setTitle(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_progress_title));
        pd.setMessage(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_progress_message));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        MovieDatabaseServerConnector connector = new MovieDatabaseServerConnector(mActivity.getApplicationContext());
        List<Movie> movies;
        try {
            movies = connector.getMovies(PAGE_NUMBER_1, mActivity.getResources().getInteger(com.muhammadfarag.popularmovies.R.integer.number_of_movies_to_load), mSortCriteria);
        } catch (IOException | JSONException e) {
            // TODO: Display error message
            Log.e("", "Error occurred while parsing movies data...: " + e.toString());
            return new ArrayList<>();
        } catch (UnauthorizedException e) {
            unauthorizedExceptionOccured = true;
            return new ArrayList<>();
        }

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (unauthorizedExceptionOccured) {
            final EditText apiKeyEditText = new EditText(mActivity);

            apiKeyEditText.setHint(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_authorization_hint));

            new AlertDialog.Builder(mActivity)
                    .setTitle(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_authorization_title))
                    .setMessage(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_authorization_body))
                    .setView(apiKeyEditText)
                    .setPositiveButton(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_authorization_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String apiKey = apiKeyEditText.getText().toString();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("api-key", apiKey).commit();
                            editor.commit();
                            mActivity.finish();
                            mActivity.startActivity(mActivity.getIntent());
                        }
                    })
                    .setNegativeButton(mActivity.getString(com.muhammadfarag.popularmovies.R.string.dialog_authorization_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mActivity.finish();
                            mActivity.startActivity(mActivity.getIntent());
                        }
                    })
                    .show();
        }
        listener.onDataSetUpdated(movies);
        if (pd != null) {
            pd.dismiss();
        }
    }
}
