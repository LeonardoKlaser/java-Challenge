package repository;

import Models.Usuario;
import db.SQLiteConnection;
import Models.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public void adicionar(Livro livro){
        if(buscarLivro(livro.getIsbn()) != null){
            System.out.println("Livro ja existe!");
            return;
        }
        String sql = "INSERT INTO livros(titulo, autor, isbn, status) VALUES(?, ?, ?, ?)";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, livro.getTitle());
            stmt.setString(2, livro.getAuthor());
            stmt.setLong(3, livro.getIsbn());
            stmt.setString(4, livro.getAvailable());
            stmt.executeUpdate();
            System.out.println("Livro adicionado ao banco de dados");
        }catch (SQLException e){
            System.out.println("Erro ao adicionar Livro: " + e.getMessage());
        }
    }

    public Livro buscarLivro(Long isbn){
        String sql = "SELECT * FROM livros WHERE isbn = ?";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setLong(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getLong("isbn")
                );
                livro.setID(rs.getInt("id"));
                livro.setAvailable(rs.getString("status"));
                return livro;
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar Livro: " + e.getMessage());
        }
        return null;
    }

    public List<Livro> listarLivros (boolean isBorrowed, boolean isAvailable, Integer bookId, Integer readerId){
        List<Livro> livros = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        // JOIN se readerId for usado
        if (readerId != null || isBorrowed ) {
            sql.append("SELECT l.*, e.leitor_id as readerId FROM livros l");
            sql.append(" JOIN emprestimos e ON l.id = e.livro_id");
        }else{
            sql.append("SELECT l.*, e.leitor_id as readerId FROM livros l");
            sql.append(" LEFT JOIN emprestimos e ON l.id = e.livro_id AND e.data_devolucao IS NULL");
        }

        List<Object> params = new ArrayList<>();
        boolean whereStarted = false;

        if (bookId != null) {
            sql.append(whereStarted ? " AND" : " WHERE").append(" l.id = ?");
            params.add(bookId);
            whereStarted = true;
        }

        if(readerId != null){
            sql.append(whereStarted ? " AND" : " WHERE").append(" e.leitor_id = ?");
            params.add(readerId);
            whereStarted = true;
        }

        if (isBorrowed) {
            sql.append(whereStarted ? " AND" : " WHERE").append(" l.status = 'emprestado'");
            whereStarted = true;
        }

        if (isAvailable) {
            sql.append(whereStarted ? " AND" : " WHERE").append(" l.status = 'disponivel'");
        }

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                }
            }

            //verifica necessidade de mostrar user realacionado ao livro
            boolean testReader = false;
            if(isBorrowed || readerId != null || !isAvailable){
                testReader = true;
            }
            UsersRepository userRepo = testReader ? new UsersRepository() : null;


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getLong("isbn")
                );
                if(userRepo != null){
                    Integer bookReader = rs.getInt("readerId");
                    Usuario reader = userRepo.buscarPorId(bookReader);
                    livro.setReader(reader);
                }
                livro.setID(rs.getInt("id"));
                livro.setAvailable(rs.getString("status"));
                livros.add(livro);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
        }

        return livros;
    }


}
