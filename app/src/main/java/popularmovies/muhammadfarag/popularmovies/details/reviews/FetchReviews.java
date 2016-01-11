package popularmovies.muhammadfarag.popularmovies.details.reviews;

import android.app.Activity;
import android.support.annotation.NonNull;

import popularmovies.muhammadfarag.popularmovies.details.DataSetUpdateListener;
import popularmovies.muhammadfarag.popularmovies.details.FetchMovieElements;
import popularmovies.muhammadfarag.popularmovies.details.MovieServerConnector;

/**
 * Created by muhammadfarag on 10/3/15.
 */
public class FetchReviews extends FetchMovieElements {


    public FetchReviews(Activity activity, DataSetUpdateListener listener) {
        super(activity, listener);
    }

    @NonNull
    @Override
    protected MovieServerConnector setServerConnector(Integer[] params) {
        return new MovieReviewsServerConnector(mActivity, params[0]);
    }

}
