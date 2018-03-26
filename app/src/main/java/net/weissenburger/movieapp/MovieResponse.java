package net.weissenburger.movieapp;

/**
 * Created by Jon Weissenburger on 3/26/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
{
        "id": "900bc451-e61f-48db-96c7-32b6635a3f75",
        ...
        "title": "The Shawshank Redemption",
        "overview": "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
        ...
        "has_poster": true,
        "imdb_rating": 9.3,
        ...
        }
*/

public class MovieResponse implements Parcelable {
    private String id;
    private String title;
    private String overview;

    @SerializedName("has_poster")
    private boolean poster;

    @SerializedName("imdb_rating")
    private float imdbRating;

    protected MovieResponse(Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        poster = in.readByte() != 0;
        imdbRating = in.readFloat();
    }

    public static final Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {
        @Override
        public MovieResponse createFromParcel(Parcel in) {
            return new MovieResponse(in);
        }

        @Override
        public MovieResponse[] newArray(int size) {
            return new MovieResponse[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public boolean hasPoster() {
        return poster;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeByte((byte) (poster ? 1 : 0));
        parcel.writeFloat(imdbRating);
    }
}
