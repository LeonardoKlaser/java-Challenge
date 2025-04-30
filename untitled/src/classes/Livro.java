package classes;

public class Livro {
    private String title;
    private String author;
    private String isbn;
    private int id;

    public Livro(String title, String author, String isbn, int id){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void SetTitle(String title){
        this.title = title;
    }

    public String get () {
        return author;
    }

    public void SetAuthor(String author){
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void SetIsbn(String isbn){
        this.isbn = isbn;
    }
}
