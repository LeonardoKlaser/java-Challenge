package classes;

public class Admin extends Usuario {
    private String document;
    private int id;
    public Admin(String Nome, String Email, String Role, String document, int id) {
        super(Nome, Email, Role);
        this.document = document;
        this.id = id;
    }
    public String getDocument(){
        return document;
    }

    public int getId(){
        return id;
    }

}
