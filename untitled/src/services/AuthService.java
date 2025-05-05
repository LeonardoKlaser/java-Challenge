package services;


import Models.Usuario;

public class AuthService {
    private String usuarioAtual;

    public String getUsuarioAtual(){
        return usuarioAtual;
    }

    public void registrarUsuario(Usuario usuario) {
        usuarioAtual = usuario.getEmail();
    }
}
