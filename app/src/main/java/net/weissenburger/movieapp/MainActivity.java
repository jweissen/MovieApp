package net.weissenburger.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class MainActivity extends AppCompatActivity implements ServiceFragment.ServiceResponseHandler {

    private TextView movieTitle;
    private TextView movieDesc;
    private TextView movieRating;
    private NetworkImageView moviePoster;

    private final static String SERVICE_FRAGMENT_TAG = "SFT";
    private final static String MOVIE_DATA = "movie_data";

    private final String IMAGE_URL_ROOT = "https://img.reelgood.com/content/movie/";
    private final String IMAGE_URL_FILENAME = "/poster-780.jpg";

    private MovieResponse response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieTitle = findViewById(R.id.movie_title);
        movieDesc = findViewById(R.id.movie_desc);
        movieRating = findViewById(R.id.movie_rating);

        moviePoster = findViewById(R.id.movie_poster);

        if (getServiceFragment() == null) {
            getSupportFragmentManager().beginTransaction().add(new ServiceFragment(), SERVICE_FRAGMENT_TAG).commitNow();
        }

        if (savedInstanceState == null) {
            getServiceFragment().getMovieData();
        } else {
            response = savedInstanceState.getParcelable(MOVIE_DATA);
            if (response != null) {
                onResponse(response);
            } else {
                // assume its loading?
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_DATA, response);
    }

    @Override
    public void onResponse(MovieResponse response) {
        this.response = response;

        movieTitle.setText(response.getTitle());
        movieDesc.setText(response.getOverview());
        movieRating.setText(Float.toString(response.getImdbRating()));

        if (response.hasPoster()) {
            moviePoster.setImageUrl(IMAGE_URL_ROOT + response.getId() + IMAGE_URL_FILENAME, getServiceFragment().getImageLoader());
        }

    }

    private ServiceFragment getServiceFragment() {
        ServiceFragment f = (ServiceFragment) getSupportFragmentManager().findFragmentByTag(SERVICE_FRAGMENT_TAG);
        return f;
    }

    @Override
    public void onError() {

    }
}
