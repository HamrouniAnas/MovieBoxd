package com.TheMovieBoxd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.TheMovieBoxd.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

}