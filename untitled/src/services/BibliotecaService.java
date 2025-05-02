package services;

import Storage.DataStore;
import Models.Admin;
import Models.Leitor;
import Models.Livro;
import Models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.UsersRepository;

import java.awt.print.Book;
import java.util.Set;

public class BibliotecaService {
    private final DataStore store = DataStore.getInstance();
    private final AuthService auth;
    private UsersRepository userRepo = new UsersRepository();
    private BookRepository bookRepo = new BookRepository();
    private BorrowRepository borrowRepo = new BorrowRepository();

    public BibliotecaService(AuthService auth) {
        this.auth = auth;
    }

    //region Books
    public void AddNewBook(String title, String author, String isbn){

        if(!userRepo.isAdmin(auth.getUsuarioAtual())){
            System.out.println("Somente admins pode criar livros");
            return;
        }

        if(bookRepo.buscarLivro(title) == null) {
            //Long bn = Long.parseLong(isbn);
            Livro newBook = new Livro(title, author, isbn);
            bookRepo.adicionar(newBook);
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

        if(!userRepo.isAdmin(auth.getUsuarioAtual())){
            System.out.println("Somente admins podem criar outro Admin");
            return;
        }

        Usuario newAdmin = new Usuario(nome, email, "admin", document);
        //store.getAdmins().put(email.hashCode(), newAdmin);
        userRepo.adicionar(newAdmin);

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

        if (!userRepo.isAdmin(auth.getUsuarioAtual())) {
            System.out.println("Somente admins podem criar um leitor");
            return;
        }

        Usuario newReader = new Usuario(nome, email, "Admin", document);
        userRepo.adicionar(newReader);

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
//        if(userRepo.buscarPorEmail()){
//            System.out.println("Leitor não encontrado");
//            return;
//        }

//        if(bookRepo.buscarLivro()){
//            System.out.println("Este livro nao existe");
//        }

        Leitor reader = store.getLeitores().get(readerId);
        Livro bookToChange = store.getLivros().get(bookId);
        borrowRepo.borrowBook(bookId, readerId);
        bookToChange.setAvailable("emprestado");
        bookToChange.setReader(reader);
        System.out.println("Livro emprestado!");
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

    //region devolverLivro
    public void DevolveLivro(int bookId){
        Livro returnedBook = store.getLivros().get(bookId);
        if(returnedBook.getAvailable().equals("disponivel")){
            System.out.println("Livro nao foi emprestado");
        }else{
            returnedBook.setAvailable("disponivel");
            System.out.println("Livro " + returnedBook.getTitle() + " foi devolvido!");
        }

    }
    //endregion

}
