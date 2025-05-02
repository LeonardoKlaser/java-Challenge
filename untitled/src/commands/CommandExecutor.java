package commands;

import Storage.DataStore;
import Models.Admin;
import Models.Leitor;
import services.AuthService;
import services.BibliotecaService;

import java.util.Scanner;

public class CommandExecutor {
    private static final Scanner scanner = new Scanner(System.in);
    AuthService auth = new AuthService();
    BibliotecaService biblioteca = new BibliotecaService(auth);

    private static String[] formataEntradas(String[] dados){
        for(int i = 0; i < dados.length; i++ ){
            String[] chaveValor = dados[i].split(" ", 2);
            dados[i] = dados[i].replaceAll(chaveValor[0],"" ).replaceAll("\"", "").trim();
        }
        return dados;
    }

    public void executar(){

        if (DataStore.getInstance().getUsuarios().isEmpty()) {
            System.out.print("Nenhum usuário cadastrado. Digite o email do admin: ");
            String emailAdmin = scanner.nextLine();

            System.out.print("Digite o Nome do admin: ");
            String Nome = scanner.nextLine();

            System.out.print("Digite o Cpf do admin: ");
            String cpf = scanner.nextLine();

            Admin newUser = new Admin(Nome, emailAdmin,  "Admin", cpf, emailAdmin.hashCode());
            DataStore.getInstance().getAdmins().put(emailAdmin.hashCode(), newUser);
            auth.registrarUsuario(newUser);
            System.out.println("Admin criado: " + emailAdmin);
        }

        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine().trim().toLowerCase();

            String[] partes = entrada.split("--");
            String comando = partes[0].trim();
            formataEntradas(partes);


            switch (comando) {
                case "exit":
                    System.out.println("Saindo do sistema...");
                    return;
                case "new-book":
                    biblioteca.AddNewBook(partes[1], partes[2], partes[3]);
                    break;
                case "new-admin":
                    biblioteca.newAdmin(partes[1], partes[2], partes[3]);
                    break;
                case "new-reader":
                    biblioteca.newReader(partes[1], partes[2], partes[3]);
                    break;
                case "borrow":
                    if(partes.length == 2){
                        if(!DataStore.getInstance().getLeitores().containsKey(auth.getUsuarioAtual().hashCode())) {
                            System.out.println("Necessário ser um leitor para retirar livro sem reader-id informado");
                            break;
                        }
                        Leitor readerAtual = DataStore.getInstance().getLeitores().get(auth.getUsuarioAtual().hashCode());
                        biblioteca.borrowBook(Integer.parseInt(partes[1]), readerAtual.getId());
                        break;
                    }
                    if(!DataStore.getInstance().getAdmins().containsKey(auth.getUsuarioAtual().hashCode())){
                        System.out.println("Necessário ser Admin para chamar este comando passando reader-id");
                        break;
                    }
                    biblioteca.borrowBook(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                    break;
                case "teste":
                    for(int i=0; i< partes.length; i++){
                        System.out.println(partes[i] + " - " + i);
                    }
                    break;
                case "listar":
                    biblioteca.ListaLivros();
                    break;
                default:
                    System.out.println("Comando inválido.");
                    //mostrarComandos();
                    break;
            };
        }
    }
}
