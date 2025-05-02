package services;

import Storage.DataStore;
import Models.Admin;
import Models.Leitor;
import Models.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Set;

public class BibliotecaService {
    private final DataStore store = DataStore.getInstance();
    private final AuthService auth;

    public BibliotecaService(AuthService auth) {
        this.auth = auth;
    }

    //region Books
    public void AddNewBook(String title, String author, String isbn){

        if(!store.getAdmins().containsKey(auth.getUsuarioAtual().hashCode())){
            System.out.println("Somente admins pode criar livros");
            return;
        }

        String tituloFormatado = title.toUpperCase().trim();
        int hash = tituloFormatado.hashCode();
        if(!store.getLivros().containsKey(hash)) {
            Long bn = Long.parseLong(isbn);
            Livro newBook = new Livro(title, author, bn, hash);
            store.getLivros().put(hash, newBook);
            System.out.println("Livro adicionado");
            //retornar json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "";
            try
            {
                json  = ow.writeValueAsString(newBook);
            }catch(Exception ex){
                System.out.println(ex);
            }
            System.out.println(json);
        }else{
            System.out.println("Esse livro ja existe");
        }
    }
    //endregion

    //region Admin
    public void newAdmin(String nome, String email, String document){

        if(!store.getAdmins().containsKey(auth.getUsuarioAtual().hashCode()) ){
            System.out.println("Somente admins podem criar outro Admin");
            return;
        }

        Admin newAdmin = new Admin(nome, email, "Admin", document, email.hashCode());
        store.getAdmins().put(email.hashCode(), newAdmin);

        //retornar json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try
        {
            json  = ow.writeValueAsString(newAdmin);
        }catch(Exception ex){
            System.out.println(ex);
        }
        System.out.println(json);
    }
    //endregion

    //region leitor
    public void newReader(String nome, String email, String document) {

        if (!store.getAdmins().containsKey(auth.getUsuarioAtual().hashCode())) {
            System.out.println("Somente admins podem criar um leitor");
            return;
        }

        Leitor newReader = new Leitor(nome, email, "Admin", document, email.hashCode());
        store.getLeitores().put(email.hashCode(), newReader);

        //retornar json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(newReader);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.out.println(json);
    }
    //endregion

    //region emprestimoLivro
    public void borrowBook(int bookId, int readerId){
        if(!store.getLeitores().containsKey(readerId)){
            System.out.println("Leitor não encontrado");
            return;
        }

        if(!store.getLivros().containsKey(bookId)){
            System.out.println("Este livro nao existe");
        }

        Livro bookToChange = store.getLivros().get(bookId);
        bookToChange.setAvailable(false);
    }
    //endregion

    //region listarLivros
    public void ListaLivros(){
        Set<Integer> chaves = store.getLivros().keySet();
        for(Integer chave : chaves){
            Livro valor = store.getLivros().get(chave);

            //retornar json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "";
            try
            {
                json  = ow.writeValueAsString(valor);
            }catch(Exception ex){
                System.out.println(ex);
            }
            System.out.println(json);
        }
    }
    //endregion

}
