package com.example.TheMovieBoxd.controller;

import com.example.TheMovieBoxd.model.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
	private static List<Movie> Movies = List.of(new Movie(1, "Stoker", "me", 2, "Thriller", 19.5),
			new Movie(2, "Oldboy", "meme", 2, "Thriller", 15));

	@GetMapping("/home")
	public String home(@RequestParam(required = false) String login, Model model) {
		model.addAttribute("login", login);
		return "home";
	}

}
