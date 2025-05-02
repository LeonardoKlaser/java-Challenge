package repository;

import Models.Livro;
import db.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BorrowRepository {
    public void borrowBook(int bookId, int readerId){
        String sql = "INSERT INTO emprestimos(livro_id, leitor_id, data_emprestimo) VALUES(?,?,?)";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,bookId);
            stmt.setInt(2, readerId);
            stmt.setString(3, LocalDateTime.now().toString());
            stmt.executeUpdate();
            System.out.println("Emprestimo realizado com sucesso");

        }catch (SQLException e){
            System.out.println("Erro ao pegar livro emprestado" + e.getMessage());
        }
    }

    public int findBorrowBook(int bookId){
        String sql = "SELECT * FROM emprestimos WHERE livro_id = ?";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("id");
            }
        }catch (SQLException e){
            System.out.println("Pesquisa por livro falhou");
        }

        return 0;
    }

    public void returnBook(int bookId, int readerId){
        String selectSql = "SELECT leitor_id, data_devolucao FROM emprestimos WHERE livro_id = ? AND data_devolucao IS NULL";
        String updateSql = "UPDATE emprestimos SET data_devolucao = ? WHERE livro_id = ? AND data_devolucao IS NULL";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Verifica se o livro está emprestado e quem é o leitor
            selectStmt.setInt(1, bookId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Este livro não está atualmente emprestado.");
                return;
            }

            int leitorQuePegou = rs.getInt("leitor_id");
            if (leitorQuePegou != readerId) {
                System.out.println("Este livro foi emprestado para outro usuário.");
                return;
            }

            // Atualiza data de devolução
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, LocalDateTime.now().toString());
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();
                System.out.println("Livro devolvido com sucesso.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao devolver o livro: " + e.getMessage());
        }
    }
}
