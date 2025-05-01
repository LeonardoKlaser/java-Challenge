package classes;

public class Livro {
    private String title;
    private String author;
    private Long isbn;
    private int id;
    private boolean available;

    public Livro(String title, String author, Long isbn, int id){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.id = id;
        this.available = true;
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


    public boolean getAvailable(){
        return available;
    }
}
