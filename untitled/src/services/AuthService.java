package services;

import Storage.DataStore;
import Models.Usuario;

public class AuthService {
    private String usuarioAtual;
    private final DataStore store = DataStore.getInstance();

    public String getUsuarioAtual(){
        return usuarioAtual;
    }

    public boolean login(String email) {
        if (store.getUsuarios().containsKey(email)) {
            usuarioAtual = email;
            return true;
        }
        return false;
    }

    public void registrarUsuario(Usuario usuario) {
        store.getUsuarios().put(usuario.getEmail(), usuario);
        usuarioAtual = usuario.getEmail();
    }

    public void logout() {
        usuarioAtual = null;
    }

    public boolean isAdminAtual(){
        return store.getAdmins().containsKey(usuarioAtual.hashCode());
    }

    public boolean isLeitorAtual() {
        return store.getLeitores().containsKey(usuarioAtual.hashCode());
    }
}
