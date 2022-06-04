package com.example.TheMovieBoxd.controller;

import com.example.TheMovieBoxd.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class MovieController {

	private static ArrayList<Movie> Movies = new ArrayList<Movie>();

	private static ArrayList<Movie> manageMovies(Movie Movie) {
		init();
		if (Movie != null)
			Movies.add(Movie);
		return Movies;
	}

	private static ArrayList<Movie> init() {
		Movies = new ArrayList<>();
		final String uri = "http://backend:8080/Movies/";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(uri, Object[].class);
		Object[] objects = responseEntity.getBody();
		ObjectMapper mapper = new ObjectMapper();
		for (Object o : objects) {
			Movie Movie = mapper.convertValue(o, Movie.class);
			Movies.add(Movie);
		}
		
		return Movies;
	}

	@GetMapping("/Movies")
	public String Movies(Model model) {
		model.addAttribute("Movies", manageMovies(null));
		model.addAttribute("Movie", new Movie());
		return "Movies_page";
	}

	@PostMapping("createMovie")
	public String createMovie(@ModelAttribute Movie Movie, BindingResult result) {
		if (result.hasErrors()) {
			return "redirect:/Movies";
		}
		final String uri = "http://backend:8080/Movies/create";
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(uri, Movie, Movie.class);
		return "redirect:/Movies";

	}

	@RequestMapping(value = "/deleteMovie", method = RequestMethod.GET)
	public String handleDeleteMovie(@RequestParam(name = "MovieId") String MovieId) {
		final String uri = "http://backend:8080/Movies";
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(uri + "/" + MovieId);
		return "redirect:/Movies";
	}

	@GetMapping("/edit/{id}")
	public String editMovie(@PathVariable("id") int id, Model model) {
		
		final String uri = "http://backend:8080/Movies/"+id;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Movie> responseEntity = restTemplate.getForEntity(uri, Movie.class);
		Movie editingMovie = responseEntity.getBody();

		//Movie editingMovie = new Movie();
		editingMovie.setId(id);
		model.addAttribute("Movie", editingMovie);
		return "edit_Movie_page";
	}

	@PostMapping("/update/{id}")
	public String updateMovie(@PathVariable("id") int id, Movie Movie, BindingResult result, Model model) {
		if (result.hasErrors()) {
			Movie.setId(id);
			return "edit_Movie_page";
		}
		final String uri = "http://backend:8080/Movies";
		Movie editedMovie = (Movie) model.getAttribute("Movie");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(uri, editedMovie, Movie.class);
		return "redirect:/Movies";
	}

}
