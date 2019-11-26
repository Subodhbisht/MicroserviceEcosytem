package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.fallbacks.FallbackMapping;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
	
	Logger logger = LoggerFactory.getLogger(CatalogResource.class);

	@Autowired
	WebClient.Builder webClientBuilder;
	
	@Autowired
	FallbackMapping fallbackMapping;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating = fallbackMapping.getUserRating(userId);

		System.out.println("Inside getCatalog method");
		return userRating.getRatings().stream().map(rating -> {
			return fallbackMapping.getCatalogItem(rating);
		}).collect(Collectors.toList());

	}
	
	@RequestMapping("/iot")
	public void Authenticate() {
		 String MANAGEMENT_TOKEN= authenticate();
		 getEvents(MANAGEMENT_TOKEN);
	}

	private String authenticate() {
		try {

			URL url = new URL("http://gateway.och.40.122.144.129.nip.io/api/authenticate");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			String jsonInputString = "{\"username\": \"office-snacks-api-user\", \"password\": \"welc0me2\"}";
			System.out.println(jsonInputString);
			
			String token="";
			
			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
			
			 if (con.getResponseCode() != 200) {
			        throw new RuntimeException("Failed : HTTP error code : "
			                + con.getResponseCode());
			    }
			 
			 token= readResponse(con);

			Gson g = new Gson();
			Token t = g.fromJson(token, Token.class); 

			return t.getId_token();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private void getEvents(String MANAGEMENT_TOKEN) {
		try {
			URL url = new URL("http://gateway.och.40.122.144.129.nip.io/services/rack/api/events");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			con.setRequestProperty("authorization", "Bearer " + MANAGEMENT_TOKEN);
			String jsonInputString = "{\n" + 
					"        \"evnetId\": \"00112\",\n" + 
					"        \"type\": \"INVENTORY\",\n" + 
					"        \"dateTimestamp\": \"2019-08-13T17:13:00Z\",\n" + 
					"        \"value\": \"50\"\n" + 
					"    }\n" + 
					"";
			System.out.println(jsonInputString);
			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
			
			if (con.getResponseCode() != 201) {
		        throw new RuntimeException("Failed : HTTP error code : "
		                + con.getResponseCode());
		    }
			
			String response = readResponse(con);
			Gson g = new Gson();
		    EventResponse event = g.fromJson(response.toString(), EventResponse.class); 
			System.out.println("Event value is "+event.getId()+" "+event.getDateTimestamp());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String readResponse(HttpURLConnection con) {
		try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    System.out.println(response.toString());
				    return response.toString();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
	}
}

/*
 * Alternative WebClient way Movie movie =
 * webClientBuilder.build().get().uri("http://localhost:8082/movies/"+
 * rating.getMovieId()) .retrieve().bodyToMono(Movie.class).block();
 */