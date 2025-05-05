package repository;


import db.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BorrowRepository {
    public void borrowBook(int bookId, int readerId){
        String sql = "INSERT INTO emprestimos(livro_id, leitor_id, data_emprestimo) VALUES(?,?,?)";
        try(Connection conn = SQLiteConnection.connect()){

            if (!livroExiste(bookId, conn)) {
                System.out.println("Livro com ID " + bookId + " não encontrado.");
                return;
            }

            if (!leitorExiste(readerId, conn)) {
                System.out.println("Leitor com ID " + readerId + " não encontrado ou não é um leitor.");
                return;
            }

            if (!livroDisponivel(bookId, conn)) {
                System.out.println("Livro já está emprestado.");
                return;
            }

            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,bookId);
                stmt.setInt(2, readerId);
                stmt.setString(3, LocalDateTime.now().toString());
                stmt.executeUpdate();
                System.out.println("Emprestimo realizado com sucesso");

                atualizarStatusLivro(bookId, "emprestado", conn);
            }

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

    public void returnBook(int bookId){
        String updateSql = "UPDATE emprestimos SET data_devolucao = ? WHERE livro_id = ? AND data_devolucao IS NULL";

        try (Connection conn = SQLiteConnection.connect()) {

            if (!livroExiste(bookId, conn)) {
                System.out.println("Livro com ID " + bookId + " não encontrado.");
                return;
            }

            if (livroDisponivel(bookId, conn)) {
                System.out.println("Livro não foi emprestado.");
                return;
            }

            // Atualiza data de devolução
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, LocalDateTime.now().toString());
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();
                atualizarStatusLivro(bookId, "disponivel", conn);
                System.out.println("Livro devolvido com sucesso.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao devolver o livro: " + e.getMessage());
        }
    }

    private boolean livroExiste(int bookId, Connection conn) throws SQLException {
        String sql = "SELECT id FROM livros WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean leitorExiste(int readerId, Connection conn) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean livroDisponivel(int bookId, Connection conn) throws SQLException {
        String sql = "SELECT status FROM livros WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getString("status").equalsIgnoreCase("disponivel");
        }
    }

    private void atualizarStatusLivro(int bookId, String status, Connection conn) throws SQLException {
        String sql = "UPDATE livros SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }
}
