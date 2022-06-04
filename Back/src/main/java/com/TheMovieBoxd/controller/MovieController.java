package com.TheMovieBoxd.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.TheMovieBoxd.model.Movie;
import com.TheMovieBoxd.repository.MovieRepository;

@RestController
@RequestMapping("movies")
public class MovieController {

	@Autowired
	MovieRepository repository;

	@GetMapping("")
	public Iterable<Movie> getMovies() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Movie getMovie(@PathVariable int id) {
		return repository.findById(id).get();
	}

	@PostMapping("/create")
	public Movie create(@RequestBody Movie movie) {
		return repository.save(movie);

	}

	@PostMapping("/total")
	public double calculateTotal(@RequestBody Iterable<Movie> movies) {
		double total = 0;
		for (Movie movie : movies) {
			total += movie.getPrice();
		}
		return total;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public class InvalidRequestException extends RuntimeException {
		public InvalidRequestException(String s) {
			super(s);
		}
	}

	@PutMapping()
	public Movie update(@RequestBody Movie movie) throws InvalidRequestException {

		if (movie == null) {
			throw new InvalidRequestException("Movie or ID must not be null!");
		}
		Optional<Movie> optionalMovie = repository.findById(movie.getId());
		if (optionalMovie == null || optionalMovie.isEmpty()) {
			throw new InvalidRequestException("Movie with ID " + movie.getId() + " does not exist.");
		}
		Movie existingMovie = optionalMovie.get();

		existingMovie.setTitle(movie.getTitle());
		existingMovie.setDirector(movie.getDirector());
		existingMovie.setQuantity(movie.getQuantity());
		existingMovie.setGenre(movie.getGenre());
		existingMovie.setPrice(movie.getPrice());

		return repository.save(existingMovie);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) throws InvalidRequestException {

		Optional<Movie> optionalMovie = repository.findById(id);
		if (optionalMovie == null || optionalMovie.isEmpty()) {
			throw new InvalidRequestException("Movie with ID " + id + " does not exist.");
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
