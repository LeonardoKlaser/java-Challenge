package br.com.kunden.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void inicialize() {
        //com try with resourcer a conexao é fechada automaticamento no fim do metodo
        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement()) {

            // Cria tabela de usuários caso nao exista
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS usuarios (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nome TEXT NOT NULL," +
                            "email TEXT UNIQUE NOT NULL," +
                            "tipo TEXT NOT NULL CHECK (tipo IN ('admin', 'leitor'))," +
                            "cpf TEXT NOT NULL" +
                            ");"
            );

            // Cria tabela de livros caso nao exista
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS livros (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "titulo TEXT NOT NULL," +
                            "autor TEXT NOT NULL," +
                            "isbn TEXT NOT NULL," +
                            "status TEXT NOT NULL CHECK (status IN ('disponivel', 'emprestado'))" +
                            ");"
            );

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS emprestimos(" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "livro_id INTEGER NOT NULL," +
                         "leitor_id INTEGER NOT NULL," +
                         "data_emprestimo TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                         "data_devolucao TEXT," +
                         "FOREIGN KEY (livro_id) REFERENCES livros(id)," +
                         "FOREIGN KEY (leitor_id) REFERENCES usuarios(id)" +
                         ");"
            );

            //seta o admin da biblioteca
            stmt.execute(
                    "INSERT INTO usuarios (nome, email, tipo, cpf) " +
                            "SELECT 'Admin', 'admin@gmail', 'admin', '123456789' " +
                            "WHERE NOT EXISTS (" +
                            "   SELECT 1 FROM usuarios WHERE email = 'admin@gmail'" +
                            ");"
            );

        } catch (SQLException e) {
            System.out.println("Erro ao inicialize banco: " + e.getMessage());
        }
    }
}
