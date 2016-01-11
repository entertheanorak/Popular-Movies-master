package com.muhammadfarag.popularmovies;

import android.test.AndroidTestCase;

import popularmovies.muhammadfarag.popularmovies.network.DataParser;

import junit.framework.Assert;

import org.json.JSONException;

/**
 * Project: Popular Movies
 * Created by muhammad on 26/06/15.
 */
public class DataParsingTest extends AndroidTestCase {

    DataParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        parser = new DataParser(getContext().getString(R.string.server_example_payload));
    }

    public void testCanRetrievePageNumber() throws JSONException {
        int pageNumber = parser.getCurrentPageNumber();
        assertEquals("Page number", 1, pageNumber);
    }

    public void testCanRetrieveTotalPages() throws JSONException {
        int pageNumber = parser.getTotalNumberOfPages();
        assertEquals("Total number of pages", 11584, pageNumber);
    }

    public void testCanRetrieveTotalResults() throws JSONException {
        int pageNumber = parser.getTotalNumberOfResults();
        assertEquals("total number of results", 231676, pageNumber);
    }

    public void testCheckSizeOfRetrievedResults() throws JSONException {
        int pageNumber = parser.getNumberOfResultsInCurrentPage();
        assertEquals("Number of results per page", 20, pageNumber);
    }

    public void testRetrievalOfFirstMovieData() throws JSONException {
        Assert.assertEquals("Original Title", "Jurassic World", parser.getMovie(0).getOriginalTitle());
        Assert.assertEquals("Poster path", "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg", parser.getMovie(0).getPosterUrl());
        Assert.assertEquals("Plot synopsis", "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.", parser.getMovie(0).getPlotSynopsis());
        Assert.assertEquals("User Rating", 7d, parser.getMovie(0).getUserRating());
        Assert.assertEquals("Release Date", "2015-06-12", parser.getMovie(0).getReleaseDate());
    }

}
