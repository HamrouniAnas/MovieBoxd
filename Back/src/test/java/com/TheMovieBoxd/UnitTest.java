package com.TheMovieBoxd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.TheMovieBoxd.controller.MovieController;
import com.TheMovieBoxd.model.Movie;
import com.TheMovieBoxd.repository.MovieRepository;

@WebMvcTest(MovieController.class)
public class UnitTest {
	
	@MockBean
	MovieRepository movieRepository;

	@Autowired
	MovieController movieController;

	@Test
	public void getMovies_success() throws Exception {
		// given a list of movies
		Movie movie1 = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);
		Movie movie2 = new Movie(2, "Stoker", "Park Chan-wook", 3, "Thriller", 27.50);
		Movie movie3 = new Movie(3, "Everything Everywhere All at Once", "Daniel Scheinert And Daniel Kwan", 7, "Fantasy", 18.90);
		List<Movie> movies = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));

		Mockito.when(movieRepository.findAll()).thenReturn(movies);

		// when we execute getMovies function
		List<Movie> result_movies = new ArrayList<Movie>();
		movieController.getMovies().forEach(result_movies::add);

		// then return the same list of movies
		assertEquals(movies.size(), result_movies.size());
		assertEquals(movies.get(1).getTitle(), result_movies.get(1).getTitle());

	}

	@Test
	public void getMovie_success() throws Exception {

		// given a movie
		Movie movie1 = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);
		Mockito.when(movieRepository.findById(movie1.getId())).thenReturn(java.util.Optional.of(movie1));

		// when we execute getMovie function
		Movie result_movie = movieController.getMovie(movie1.getId());

		// then return the same movie
		assertEquals(movie1.getTitle(), result_movie.getTitle());
		assertEquals(movie1.getQuantity(), result_movie.getQuantity());
	}

	@Test
	public void create_success() throws Exception {
		// given a movie
		Movie movie = new Movie(4, "Millennium Mambo", "Hou Hsiao-hsien", 9, "Fantasy", 16.85);

		Mockito.when(movieRepository.save(movie)).thenReturn(movie);

		// when we execute create function
		Movie result_movie = movieController.create(movie);

		// then return the same movie
		assertEquals(movie.getTitle(), result_movie.getTitle());
		assertEquals(movie.getQuantity(), result_movie.getQuantity());
	}

	@Test
	public void update_success() throws Exception {

		// given a movie
		Movie movie = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);
		Movie updated_movie = new Movie(1, "Long Day's Journey Into Night", "Bi Gan", 8, "Drama", 27);

		Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.save(updated_movie)).thenReturn(updated_movie);

		// when we execute update function
		Movie result_movie = movieController.update(updated_movie);

		// then return the same movie
		assertEquals(movie.getTitle(), result_movie.getTitle());
		assertEquals(movie.getQuantity(), result_movie.getQuantity());
	}


	@Test
	public void delete_success() throws Exception {
		// given a movie
		Movie movie = new Movie(2, "Stoker", "Park Chan-wook", 3, "Thriller", 27.50);
		Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

		// when we execute delete function
		movieController.delete(movie.getId());

		// then nothing happens, no exception is thrown
	}


}