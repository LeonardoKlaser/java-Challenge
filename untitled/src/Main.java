import commands.CommandExecutor;
import db.DBInitializer;

public class Main {

    public static void main(String[] args) {
        DBInitializer.inicializar();

        CommandExecutor commands = new CommandExecutor();
        commands.executar();
    }

}