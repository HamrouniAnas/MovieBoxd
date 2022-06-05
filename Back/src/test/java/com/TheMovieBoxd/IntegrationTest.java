package com.TheMovieBoxd;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.TheMovieBoxd.model.Movie;
import com.TheMovieBoxd.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	private static MySQLContainer mySQLContainer;

	@BeforeAll
	static void init() {
		mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:5.7").withDatabaseName("test")
				.withUsername("Cineradio").withPassword("pwd");
		mySQLContainer.start();
	}

	@DynamicPropertySource
	public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
	}

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void getMovies_success() throws Exception {
		// given a list of Movies
		Movie movie1 = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);
		Movie movie2 = new Movie(2, "Stoker", "Park Chan-wook", 3, "Thriller", 27.50);
		Movie movie3 = new Movie(3, "Everything Everywhere All at Once", "Daniel Scheinert And Daniel Kwan", 7, "Fantasy", 18.90);
		List<Movie> movies = List.of(movie1, movie2, movie3);
		movieRepository.saveAll(movies);

		// when we perform GET request /movies
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/movies"));

		// then list all movies
		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(movies.size())));
	}

	@Test
	public void getMovie_success() throws Exception {
		// given a movie
		Movie movie1 = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);
		movieRepository.save(movie);

		// when we perform GET request /movie/1
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/movies/1").contentType(MediaType.APPLICATION_JSON));

		// then show movie with id=1
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Kaili Blues"))).andExpect(jsonPath("$.quantity", is(4)));
	}

	@Test
	public void create_success() throws Exception {
		// given a movie
		Movie movie1 = new Movie(1, "Kaili Blues", "Bi Gan", 4, "Drama", 17);

		// when we perform a POST request /movies/create
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/movies/create")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(movie));
		ResultActions response = mockMvc.perform(mockRequest);

		// then create the movie object
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Kaili Blues")));
	}

	@Test
	public void update_success() throws Exception {
		// given an update movie
		Movie updated_movie = new Movie(1, "Long Day's Journey Into Night", "Bi Gan", 8, "Drama", 27);

		// when we perform a PUT request /movies with the body containing the updated
		// movie
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updated_movie));
		ResultActions response = mockMvc.perform(mockRequest);

		// then update the movie object
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Long Day's Journey Into Night")));
	}

	// test what to do in case the movie is not found
	@Test
	public void update_movieNotFound() throws Exception {
		// given a non existing movie
		Movie movie = new Movie(11, "Millennium Mambo", "Hou Hsiao-hsien", 9, "Fantasy", 16.85);

		// when we perform a PUT request /movies with the body containing the non
		// existing movie
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/movies")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updated_movie));
		ResultActions response = mockMvc.perform(mockRequest);

		// then return a bad request response with message
		response.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception))
				.andExpect(result -> assertEquals("Movie with ID 11 does not exist.",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void delete_success() throws Exception {

		// given a movie
		Movie movie = new Movie(2, "Stoker", "Park Chan-wook", 3, "Thriller", 27.50);

		// when we perform a DELETE request /movies/id
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.delete("/movies/" + movie.getId()).contentType(MediaType.APPLICATION_JSON));
		// then return no content response
		// response.andExpect(status().isNoContent());
	}

	@Test
	public void delete_notFound() throws Exception {

		// given a non existing movie id
		int movie_id = 147;

		// when we perform a DELETE request /movies/id
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.delete("/movies/" + movie_id).contentType(MediaType.APPLICATION_JSON));

		// then return bad request with error message
		response.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception))
				.andExpect(result -> assertEquals("Movie with ID " + movie_id + " does not exist.",
						result.getResolvedException().getMessage()));
	}

}
