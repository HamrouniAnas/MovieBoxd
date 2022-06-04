package com.example.TheMovieBoxd.model;


import java.util.Objects;

public class Movie {

    private int id;

    private String title;
    private String Director;
    private int quantity;
    private String genre;
    private double price;
    public Movie(){

    }
    public Movie(int id, String title, String Director, int quantity, String genre, double price) {
        super();
        this.id = id;
        this.title = title;
        this.Director = Director;
        this.quantity = quantity;
        this.genre = genre;
        this.price = price;
    }

    public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", Director=" + Director + ", quantity=" + quantity + ", genre="
                + genre + "]";
    }
    @Override
    public int hashCode()
    {return Objects.hash(id,title,Director,quantity,genre);
    }

}