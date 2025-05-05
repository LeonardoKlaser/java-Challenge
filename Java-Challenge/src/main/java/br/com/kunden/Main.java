package br.com.kunden;

import br.com.kunden.commands.CommandExecutor;
import br.com.kunden.db.DBInitializer;

public class Main {

    public static void main(String[] args) {
        DBInitializer.inicialize();

        CommandExecutor commands = new CommandExecutor();
        commands.exec();
    }

}