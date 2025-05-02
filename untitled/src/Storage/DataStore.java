package Storage;

import Models.*;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static final DataStore instance = new DataStore();

    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<Integer, Admin> admins = new HashMap<>();
    private final Map<Integer, Leitor> leitores = new HashMap<>();
    private final Map<Integer, Livro> livros = new HashMap<>();

    private DataStore() {}

    public static DataStore getInstance() {
        return instance;
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Map<Integer, Admin> getAdmins() {
        return admins;
    }

    public Map<Integer, Leitor> getLeitores() {
        return leitores;
    }

    public Map<Integer, Livro> getLivros() {
        return livros;
    }
}