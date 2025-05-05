package commands;

import Models.Usuario;
import repository.UsersRepository;
import services.AuthService;
import services.BibliotecaService;

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


    public Map<String, String> formataEntradasHash(String[] dados, Map<String, String> params){
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

    public void executar(){


        System.out.print("Digite o email do admin: ");
        String emailAdmin = scanner.nextLine();

        System.out.print("Digite o Cpf do admin: ");
        String cpf = scanner.nextLine();

        Usuario userToLogin = userRepo.buscarPorEmail(emailAdmin);
        if(userToLogin == null) System.out.println("Usuario não existe");

        if(!userToLogin.getDocument().equals(cpf)){
            System.out.println("Cpf invalido!\nfechando sistema");
            return;
        }
        auth.registrarUsuario(userToLogin);
        System.out.println("Admin logado: " + emailAdmin);
        System.out.println("Digite help para visualizar todos os comandos disponiveis");




        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine().trim().toLowerCase();
            Map<String, String> params = new HashMap<String, String>();

            params = formataEntradasHash(entrada.split("--"), params);
            String[] partes = entrada.split("--");
            String comando = partes[0].trim();


            switch (comando) {
                case "exit":
                    System.out.println("Saindo do sistema...");
                    return;
                case "new-book":
                    biblioteca.AddNewBook(params);
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
                    biblioteca.ListaLivros(params);
                    break;
                case "return":
                    biblioteca.DevolveLivro(Integer.parseInt(partes[1]));
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
