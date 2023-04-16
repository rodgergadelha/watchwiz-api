package com.example.movieappbackend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/services")
public class MovieController {

	@GetMapping
	public Object listServices() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://streaming-availability.p.rapidapi.com/v2/services"))
				.header("X-RapidAPI-Key", "553dacfb9dmshdaa2584494d8cd6p1a6918jsn674950764974")
				.header("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, HttpResponse.BodyHandlers.ofString());
		
		return response.body();	
	}
	
	
}
