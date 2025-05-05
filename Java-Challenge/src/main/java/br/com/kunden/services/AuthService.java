package br.com.kunden.services;


import br.com.kunden.Models.User;

public class AuthService {
    private String usuarioAtual;

    public String getUsuarioAtual(){
        return usuarioAtual;
    }

    public void registerUser(User usuario) {
        usuarioAtual = usuario.getEmail();
    }
}
