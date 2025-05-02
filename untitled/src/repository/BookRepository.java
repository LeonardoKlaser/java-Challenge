package repository;

import Models.Usuario;
import db.SQLiteConnection;
import Models.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRepository {
    public void adicionar(Livro livro){
        if(buscarLivro(livro.getTitle()) != null){
            System.out.println("Livro com este titulo já existe!");
            return;
        }
        String sql = "INSERT INTO livros(titulo, autor, isbn, status) VALUES(?, ?, ?, ?)";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, livro.getTitle());
            stmt.setString(2, livro.getAuthor());
            stmt.setString(3, livro.getIsbn());
            stmt.setString(4, livro.getAvailable());
            stmt.executeUpdate();
            System.out.println("Livro adicionado ao banco de dados");
        }catch (SQLException e){
            System.out.println("Erro ao adicionar Livro: " + e.getMessage());
        }
    }

    public Livro buscarLivro(String titulo){
        String sql = "SELECT * FROM livros WHERE UPPER(titulo) = UPPER(?)";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getInt("id")
                );
                livro.setAvailable(rs.getString("status"));
                return livro;
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar Livro: " + e.getMessage());
        }
        return null;
    }

}
