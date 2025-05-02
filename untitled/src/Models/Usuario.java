package Models;

public class Usuario {
    protected String Nome;
    protected String Email;
    protected String Role;
    protected String Document;

    public Usuario(String Nome, String Email, String Role, String document){
        this.Nome = Nome;
        this.Email = Email;
        this.Role = Role;
        this.Document = document;
    }

    public String getNome() {
        return Nome;
    }

    public void SetNome(String Nome){
        this.Nome = Nome;
    }

    public String getEmail() {
        return Email;
    }

    public void SetEmail(String Email){
        this.Email = Email;
    }

    public String getRole() {
        return Role;
    }

    public void SetRole(String Role){
        this.Role = Role;
    }

    public String getDocument(){
        return Document;
    }

    public void setDocument(String document){
        this.Document = document;
    }

}
