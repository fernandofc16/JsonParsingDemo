package br.com.fexus.jsonparsingdemo;

import java.util.ArrayList;

public class Movies {

    private String movieName, movieDuration, director, tagLine, imageURL, story;
    private int year;
    private float rating;
    private ArrayList<String> cast = new ArrayList<>();

    public Movies(String movieName, int year, float rating, String movieDuration, String director, String tagLine, ArrayList<String> cast, String imageURL, String story) {
        this.movieName = movieName;
        this.movieDuration = movieDuration;
        this.director = director;
        this.tagLine = tagLine;
        this.imageURL = imageURL;
        this.story = story;
        this.year = year;
        this.rating = rating;
        this.cast = cast;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieDuration() {
        return movieDuration;
    }

    public String getDirector() {
        return director;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStory() {
        return story;
    }

    public int getYear() {
        return year;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

}
