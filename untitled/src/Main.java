import classes.Livro;
import classes.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Usuario> usuarios = new HashMap<>();
    private static final Map<Integer, Livro> livros = new HashMap<>();
    private static String usuarioAtual = null;

    private static String[] formataEntradas(String[] dados){
        for(int i = 0; i < dados.length - 1; i++ ){
            String[] chaveValor = dados[i + 1].split(" ", 2);
            dados[i] = dados[i + 1].replaceAll(chaveValor[0],"" ).replaceAll("\"", "").trim();
        }
        return dados;
    }

    public static void main(String[] args) {

        if (usuarios.isEmpty()) {
            System.out.print("Nenhum usuário cadastrado. Digite o email do admin: ");
            String emailAdmin = scanner.nextLine();
            System.out.print("Digite o Nome do admin: ");
            String Nome = scanner.nextLine();
            Usuario newUser = new Usuario(Nome, emailAdmin,  "Admin");
            usuarios.put(emailAdmin, newUser);
            usuarioAtual = emailAdmin;
            System.out.println("Admin criado: " + emailAdmin);
        }

        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine().trim().toLowerCase();
            String[] partes = entrada.split("--");
            String comando = partes[0].trim();
            System.out.println("comecou");

            formataEntradas(partes);
            for(int i = 0; i < partes.length; i++){
                System.out.println(partes[i]);
            }
            System.out.println(comando);
            switch (comando) {
                case "login":
                    login();
                    break;
                case "logout":
                    logout();
                    break;
                case "info":
                    info();
                    break;
                case "help":
                    mostrarComandos();
                    break;
                case "exit":
                    System.out.println("Saindo do sistema...");
                    return;
                case "new-book":
                    AddNewBook(partes[0], partes[1], partes[2]);
                default:
                    System.out.println("Comando inválido.");
                    mostrarComandos();
                    break;
            };
        }
    }

   //region User
    private static void login() {
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();

        if (usuarios.containsKey(email)) {
            usuarioAtual = email;
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.print("Usuário não encontrado. Deseja criar? (s/n): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                System.out.print("Qual seu nome? ");
                String nomeNewUser = scanner.nextLine();
                Usuario newUser = new Usuario(nomeNewUser, email, "user");
                usuarioAtual = newUser.getEmail();
                usuarios.put(usuarioAtual, newUser);
                System.out.print("Novo usuario criado");
            }
        }
    }

    private static void logout() {
        if (usuarioAtual == null) {
            System.out.println("Nenhum usuário está logado.");
        } else {
            System.out.println("Logout realizado: " + usuarioAtual);
            usuarioAtual = null;
        }
    }

    private static void info() {
        if (usuarioAtual == null) {
            System.out.println("Nenhum usuário logado.");
        } else {
            Usuario userToShow = usuarios.get(usuarioAtual);
            System.out.println("Usuário atual: \n" + "email: " + userToShow.getEmail() + "\nNome: "  + userToShow.getNome() + "\nRole: " + userToShow.getRole());
        }
    }

    private static void mostrarComandos() {
        System.out.println("Comandos disponíveis: login, logout, info, help, exit");
    }
    //endregion
    //region Books
    private static void AddNewBook(String title, String author, String isbn){
        Usuario userTeste = usuarios.get(usuarioAtual);
        if(!userTeste.getRole().equals("Admin") ){
            System.out.println("Somente admins pode criar livros");
            return;
        }

        String tituloFormatado = title.toUpperCase().trim();
        int hash = tituloFormatado.hashCode();
        if(!livros.containsKey(hash)) {
            Livro newBook = new Livro(title, author, isbn, hash);
            livros.put(hash, newBook);
            System.out.println("Livro adicionado");
            String json = String.format(
                    "{\n  \"title\": \"%s\",\n  \"author\": \"%s\",\n  \"isbn\": \"%s\",\n  \"id\": \"%d\"\n}",
                    title.replace("\"", "\\\""),
                    author.replace("\"", "\\\""),
                    isbn.replace("\"", "\\\""),
                    hash
            );
            System.out.println(json);
        }else{
            System.out.println("Esse livro ja existe");
        }
    }
    //endregion

}