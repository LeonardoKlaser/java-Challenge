package br.com.kunden.services;

import br.com.kunden.Models.Book;
import br.com.kunden.Models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import br.com.kunden.repository.BookRepository;
import br.com.kunden.repository.BorrowRepository;
import br.com.kunden.repository.UsersRepository;

import java.util.*;

public class BibliotecaService {
    private final AuthService auth;
    private UsersRepository userRepo = new UsersRepository();
    private BookRepository bookRepo = new BookRepository();
    private BorrowRepository borrowRepo = new BorrowRepository();

    public BibliotecaService(AuthService auth) {
        this.auth = auth;
    }

    //region userInfo
    public void showUser(){
        try{
            User userToShow = userRepo.searchByEmail(auth.getUsuarioAtual());
            //retornar json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "";
            json = ow.writeValueAsString(userToShow);
            System.out.println(json);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    //endregion

    //region login
    public void Login(Map<String, String> params){
        try{
            // Verifica se as chaves obrigatórias existem e se não há chaves extras
            Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("email"));

            if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
                System.out.println("Parâmetros inválidos! Informe apenas: --email.");
                return;
            }


            String email = params.get("email");

            User userLogin = userRepo.searchByEmail(email);
            if(userLogin != null) auth.registerUser(userLogin);
            System.out.println("Usuario " + userLogin.getNome() + " logado");
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //region Books
    public void addNewBook(Map<String, String> params){

        if(!userRepo.isAdmin(auth.getUsuarioAtual())){
            System.out.println("Somente admins pode criar livros");
            return;
        }

        // Verifica se as chaves obrigatórias existem e se não há chaves extras
        Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("title", "author", "isbn"));

        if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
            System.out.println("Parâmetros inválidos! Informe apenas: --title, --author e --isbn.");
            return;
        }


        String title = params.get("title");
        String author = params.get("author");
        String isbn = params.get("isbn");

        Long bn = Long.parseLong(isbn);

        Book newBook = new Book(title, author, bn);
        bookRepo.addNewBook(newBook);

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

    }
    //endregion

    //region Admin
    public void newAdmin(Map<String, String> params){
        //verifica se o usuario logado é adm
        if(!userRepo.isAdmin(auth.getUsuarioAtual())){
            System.out.println("Somente admins podem criar outro Admin");
            return;
        }

        // Verifica se as chaves obrigatórias existem e se não há chaves extras
        Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("name", "email", "document"));

        if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
            System.out.println("Parâmetros inválidos! Informe apenas: --name, --email e --document.");
            return;
        }


        String nome = params.get("name");
        String email = params.get("email");
        String document = params.get("document");

        User newAdmin = new User(nome, email, "admin", document);
        userRepo.addUser(newAdmin);

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
    public void newReader(Map<String, String> params) {

        if (!userRepo.isAdmin(auth.getUsuarioAtual())) {
            System.out.println("Somente admins podem criar um leitor");
            return;
        };

        // Verifica se as chaves obrigatórias existem e se não há chaves extras
        Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("name", "email", "document"));

        if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
            System.out.println("Parâmetros inválidos! Informe apenas: --name, --email e --document.");
            return;
        }


        String nome = params.get("name");
        String email = params.get("email");
        String document = params.get("document");

        User newReader = new User(nome, email, "leitor", document);
        userRepo.addUser(newReader);

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
    public void borrowBook(Map<String, String> params){
        try{
            // Verifica se as chaves obrigatórias existem e se não há chaves extras
            Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("book-id"));

            if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
                System.out.println("Parâmetros inválidos! Informe apenas: --book-id, caso seja adm pode informar reader-id também.");
                return;
            }


            Integer bookId = Integer.parseInt(params.get("book-id"));
            Integer readerId = params.containsKey("reader-id") ? Integer.parseInt(params.get("reader-id")) : null;

            if(readerId != null && !userRepo.isAdmin(auth.getUsuarioAtual())) System.out.println("Somente admins podem enviar readerId no parametro!");
            User user = userRepo.searchByEmail(auth.getUsuarioAtual());
            if(readerId == null) readerId = user.getId();

            borrowRepo.borrowBook(bookId, readerId);
        }catch (Exception e){
            System.out.println("Erro ao emprestar livro: " +  e.getMessage());
        }

    }
    //endregion

    //region listBooks
    public void listBooks(Map<String, String> params){

        // Definir as chaves válidas
        Set<String> chavesValidas = new HashSet<>(Arrays.asList("is-borrowed", "is-available", "book-id", "reader-id"));

        // Verifica se há chaves invalidas
        for (String chave : params.keySet()) {
            if (!chavesValidas.contains(chave)) {
                System.out.println("Parâmetros inválidos! Permitir somente: --isBorrowed, --isAvailable, --book-id, --reader-id.");
                return;
            }
        }


        boolean isBorrowed = params.containsKey("is-borrowed");
        boolean isAvailable = params.containsKey("is-available");
        Integer bookId = params.containsKey("book-id") ? Integer.parseInt(params.get("book-id")) : null;
        Integer readerId = params.containsKey("reader-id") ? Integer.parseInt(params.get("reader-id")) : null;

        List<Book> livros = bookRepo.listBooks(isBorrowed, isAvailable, bookId, readerId);
        for(Book livro : livros){

            //retornar json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "";
            try
            {
                json  = ow.writeValueAsString(livro);
            }catch(Exception ex){
                System.out.println(ex);
            }
            System.out.println(json);
        }
    }
    //endregion

    //region devolverLivro
    public void returnBook(Map<String, String> params){
        try{
            // Verifica se as chaves obrigatórias existem e se não há chaves extras
            Set<String> chavesObrigatorias = new HashSet<>(Arrays.asList("book-id"));

            if (!params.keySet().containsAll(chavesObrigatorias) || params.size() != chavesObrigatorias.size()) {
                System.out.println("Parâmetros inválidos! Informe apenas: --book-id");
                return;
            }


            Integer bookId = Integer.parseInt(params.get("book-id"));
            borrowRepo.returnBook(bookId);
        }catch (Exception e){
            System.out.println("Erro ao devolver livro: " + e.getMessage());
        }
    }
    //endregion

    public void ShowCommands(){
        Map<String, String[]> comandos = new HashMap<>();

        comandos.put("new-book", new String[]{"--title", "--author", "--isbn"});
        comandos.put("new-reader", new String[]{"--name", "--email", "--document"});
        comandos.put("new-admin", new String[]{"--name", "--email", "--document"});
        comandos.put("borrow", new String[]{"--book-id", "--reader-id"});
        comandos.put("return", new String[]{"--book-id", "--reader-id"});
        comandos.put("listar", new String[]{"--is-borrowed", "--is-available", "--book-id", "--reader-id"});
        comandos.put("login", new String[]{"--email"});
        comandos.put("showuser", new String[]{});

        System.out.println("Comandos disponíveis e seus parâmetros necessários:");
        for (Map.Entry<String, String[]> entry : comandos.entrySet()) {
            String comando = entry.getKey();
            String[] parametros = entry.getValue();
            System.out.print(comando + ": ");
            for (String param : parametros) {
                System.out.print(param + " ");
            }
            System.out.println(); // Quebra de linha
        }
    }

}
