package Models;

public class Livro {
    private String title;
    private String author;
    private String isbn;
    private int id;
    private Leitor reader;
    private String available;

    public Livro(String title, String author, String isbn){
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

    public String getIsbn() {
        return isbn;
    }

    public void SetIsbn(String isbn){
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }


    public String getAvailable(){
        return available;
    }

    public void setAvailable(String available){
        this.available = available;
    }

    public Leitor getReader(){
        return reader;
    }

    public void setReader(Leitor reader){
        this.reader = reader;
    };
}
