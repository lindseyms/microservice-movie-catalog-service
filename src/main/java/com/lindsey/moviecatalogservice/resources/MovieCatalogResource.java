package com.lindsey.moviecatalogservice.resources;

import com.lindsey.moviecatalogservice.models.CatalogItem;
import com.lindsey.moviecatalogservice.models.Movie;
import com.lindsey.moviecatalogservice.models.Rating;
import com.lindsey.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private RestTemplate restTemplate;

    @Autowired
    public MovieCatalogResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId){
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
        List<CatalogItem> items = new ArrayList<>();
        for (Rating rating : ratings.getUserRating()) {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            items.add(new CatalogItem(movie.getName(), "description", rating.getRating()));
        }
        return items;
    }

}
