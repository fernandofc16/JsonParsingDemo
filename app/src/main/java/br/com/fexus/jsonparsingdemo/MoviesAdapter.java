package br.com.fexus.jsonparsingdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class MoviesAdapter extends ArrayAdapter {

    private List<Movies> moviesList;
    private int resource;
    private LayoutInflater inflater;

    public MoviesAdapter(Context context, int resource, List<Movies> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        moviesList = objects;
        this.resource = resource;
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.imageViewMovie = (ImageView) convertView.findViewById(R.id.imageViewMovie);
            holder.textViewMovieName = (TextView) convertView.findViewById(R.id.textViewMovieName);
            holder.textViewMovieTagLine = (TextView) convertView.findViewById(R.id.textViewMovieTagLine);
            holder.textViewMovieYear = (TextView) convertView.findViewById(R.id.textViewMovieYear);
            holder.textViewMovieDuration = (TextView) convertView.findViewById(R.id.textViewMovieDuration);
            holder.textViewMovieDirector = (TextView) convertView.findViewById(R.id.textViewMovieDirector);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.textViewMovieCast = (TextView) convertView.findViewById(R.id.textViewCast);
            holder.textViewStory = (TextView) convertView.findViewById(R.id.textViewStory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

        ImageLoader.getInstance().displayImage(moviesList.get(position).getImageURL(), holder.imageViewMovie, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });
        holder.textViewMovieName.setText(moviesList.get(position).getMovieName());
        holder.textViewMovieTagLine.setText(moviesList.get(position).getTagLine());
        holder.textViewMovieYear.setText("Year: " + moviesList.get(position).getYear());
        holder.textViewMovieDuration.setText("Duration: " + moviesList.get(position).getMovieDuration());
        holder.textViewMovieDirector.setText("Director: " + moviesList.get(position).getDirector());
        StringBuilder castFinal = new StringBuilder("Cast: ");
        for(String cast : moviesList.get(position).getCast()) {
            castFinal.append(cast).append(", ");
        }
        castFinal.delete(castFinal.length() - 2, castFinal.length());
        castFinal.append(".");
        float rate = ((moviesList.get(position).getRating()*5)/10);
        holder.ratingBar.setRating(rate);
        Log.d("FEXUS: ", "VALUE ADAPTEEEEER : " + rate);
        holder.textViewMovieCast.setText(castFinal);
        holder.textViewStory.setText(moviesList.get(position).getStory());

        return convertView;
    }

    public class ViewHolder {
        private ImageView imageViewMovie;
        private TextView textViewMovieName;
        private TextView textViewMovieTagLine;
        private TextView textViewMovieYear;
        private TextView textViewMovieDuration;
        private TextView textViewMovieDirector;
        private RatingBar ratingBar;
        private TextView textViewMovieCast;
        private TextView textViewStory;
    }
}
