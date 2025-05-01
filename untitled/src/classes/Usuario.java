package classes;

public class Usuario {
    protected String Nome;
    protected String Email;
    protected String Role;

    public Usuario(String Nome, String Email, String Role){
        this.Nome = Nome;
        this.Email = Email;
        this.Role = Role;
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



}
