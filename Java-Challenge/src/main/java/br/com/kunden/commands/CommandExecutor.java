package br.com.kunden.commands;

import br.com.kunden.Models.User;
import br.com.kunden.repository.UsersRepository;
import br.com.kunden.services.AuthService;
import br.com.kunden.services.BibliotecaService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {
    private static final Scanner scanner = new Scanner(System.in);
    AuthService auth = new AuthService();
    BibliotecaService biblioteca = new BibliotecaService(auth);
    UsersRepository userRepo = new UsersRepository();
    String[] actions = {"new-book", "new-admin", "new-reader", "borrow", "return", "listar", "login", "showUser", "help"};


    public Map<String, String> formatHashEntry(String[] dados, Map<String, String> params){
        for(int i = 0; i < dados.length; i++ ){
            if(Arrays.asList(actions).contains(dados[i].trim())) continue;
            String[] aux = dados[i].split(" ");
            String chave = aux[0];
            String[] valoraux = Arrays.copyOfRange(aux, 1, aux.length);
            String valor = String.join(" ",valoraux);
            valor = valor.replaceAll("^\"|\"$", "");
            params.put(chave, valor);
        }
        return params;
    }

    public void exec(){


        System.out.print("Digite o email do admin: ");
        String emailAdmin = scanner.nextLine();

        System.out.print("Digite o Cpf do admin: ");
        String cpf = scanner.nextLine();

        User userToLogin = userRepo.searchByEmail(emailAdmin);
        if(userToLogin == null) System.out.println("Usuario não existe");

        if(!userToLogin.getDocument().equals(cpf)){
            System.out.println("Cpf invalido!\nfechando sistema");
            return;
        }
        auth.registerUser(userToLogin);
        System.out.println("Admin logado: " + emailAdmin);
        System.out.println("Digite help para visualizar todos os comandos disponiveis");




        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine().trim().toLowerCase();
            Map<String, String> params = new HashMap<String, String>();

            params = formatHashEntry(entrada.split("--"), params);
            String comand = entrada.split("--")[0].trim();


            switch (comand) {
                case "exit":
                    System.out.println("Saindo do sistema...");
                    return;
                case "new-book":
                    biblioteca.addNewBook(params);
                    break;
                case "new-admin":
                    biblioteca.newAdmin(params);
                    break;
                case "new-reader":
                    biblioteca.newReader(params);
                    break;
                case "borrow":
                    biblioteca.borrowBook(params);
                    break;
                case "listar":
                    biblioteca.listBooks(params);
                    break;
                case "return":
                    biblioteca.returnBook(params);
                    break;
                case "login":
                    biblioteca.Login(params);
                    break;
                case "showuser":
                    biblioteca.showUser();
                    break;
                case "help":
                    biblioteca.ShowCommands();
                    break;
                default:
                    System.out.println("Comando inválido.");
                    biblioteca.ShowCommands();
                    break;
            };
        }
    }
}
