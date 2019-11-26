package io.javabrains.moviecatalogservice.fallbacks;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;

@Service
public class FallbackMapping {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value="5"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
			})
	public CatalogItem getCatalogItem(Rating rating) {
		System.out.println("This will get invoked"+rating.getMovieId());
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		System.out.println("Movie name "+movie.getName());
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}

	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
	}

	@SuppressWarnings("unused")
	private CatalogItem getFallbackCatalogItem(Rating rating) {
		System.out.println("Inside getFallbackCatalogItem ---------");
		return new CatalogItem("Movie Not Found", "No Description", rating.getRating());
	}
	
	@SuppressWarnings("unused")
	private UserRating getFallbackUserRating(String userId) {
		System.out.println("Inside getFallbackUserRating for user id "+userId);
		UserRating userRating = new UserRating();
		userRating.setUserId(userId);
		//default rating is 100
		userRating.setRatings(Arrays.asList(new Rating("100", 0)));
		return userRating;
	}
}
