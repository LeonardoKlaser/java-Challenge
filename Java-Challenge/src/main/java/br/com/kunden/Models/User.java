package br.com.kunden.Models;

public class User {
    protected String Nome;
    protected String Email;
    protected String Role;
    protected String Document;
    protected Integer id;

    public User(String Nome, String Email, String Role, String document){
        this.Nome = Nome;
        this.Email = Email;
        this.Role = Role;
        this.Document = document;
        this.id = null;
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

    public void setId (Integer id){
        this.id = id;
    }

    public Integer getId(){
        return this.id;
    }
    public void setDocument(String document){
        this.Document = document;
    }

}
