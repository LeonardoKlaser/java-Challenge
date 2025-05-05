package br.com.kunden.Models;

public class Book {
    private String title;
    private String author;
    private Long isbn;
    private int id;
    private User reader;
    private String available;

    public Book(String title, String author, Long isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = "disponivel";
        this.reader = null;
    }

    public String getTitle() {
        return title;
    }

    public void SetTitle(String title){
        this.title = title;
    }

    public String getAuthor () {
        return author;
    }

    public void SetAuthor(String author){
        this.author = author;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void SetIsbn(Long isbn){
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getAvailable(){
        return available;
    }

    public void setAvailable(String available){
        this.available = available;
    }

    public User getReader(){
        return reader;
    }

    public void setReader(User reader){
        this.reader = reader;
    };
}
